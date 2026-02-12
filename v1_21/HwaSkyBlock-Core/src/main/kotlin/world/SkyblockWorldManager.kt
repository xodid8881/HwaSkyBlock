package org.hwabaeg.hwaskyblock.world

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Semaphore
import java.util.stream.Collectors

object SkyblockWorldManager {

    private val creatingIslands = ConcurrentHashMap<Int, Boolean>()
    private val removingIslands = ConcurrentHashMap<String, Boolean>()
    private val createSemaphore = Semaphore(2)

    fun cleanupIncompleteIslands() {
        val root = Bukkit.getWorldContainer().toPath()

        Files.list(root).use { stream ->
            stream.filter { Files.isDirectory(it) }
                .filter { Files.exists(it.resolve(".creating")) }
                .forEach { dir ->
                    Bukkit.getLogger().warning(
                        "[SkyblockWorldManager] Incomplete island detected: ${dir.fileName}, cleaning up."
                    )
                    deleteFileStructure(dir)
                }
        }
    }

    fun addIsland(player: Player, id: Int, templatePath: String) {

        if (creatingIslands.putIfAbsent(id, true) != null) {
            SchedulerUtil.runSync {
                player.sendMessage("§c이미 해당 섬이 생성 중입니다. 잠시만 기다려주세요.")
            }
            return
        }

        SchedulerUtil.runAsync {
            createSemaphore.acquire()

            val worldName = "HwaSkyBlock.$id"
            val worldsRoot = Bukkit.getWorldContainer().toPath() // == worlds/
            val target = worldsRoot.resolve(worldName)
            val marker = target.resolve(".creating")
            var bar: BossBar? = null

            try {
                // 1. worlds/ 아래에 월드 디렉토리 생성
                Files.createDirectories(target)
                Files.createFile(marker)

                bar = SchedulerUtil.runSyncReturn {
                    showProgressBar(
                        player,
                        "Generating your island...",
                        0.0,
                        BarColor.GREEN,
                        BarStyle.SEGMENTED_12
                    )
                }

                sendAction(player, "§aCopying world data...")
                copyFileStructureWithProgress(Paths.get(templatePath), target, bar)

                SchedulerUtil.runSync {
                    val world = WorldCreator(worldName).createWorld()
                        ?: throw IllegalStateException("World load failed: $worldName")

                    player.teleport(world.spawnLocation)

                    bar.progress = 1.0
                    bar.setTitle("§aIsland Ready!")
                    SchedulerUtil.runLater(100L) { bar.removeAll() }

                    Bukkit.getPluginManager()
                        .callEvent(SkyblockIslandCreateEvent(player, id))
                }

                Files.deleteIfExists(marker)

            } catch (e: Exception) {
                Bukkit.getLogger().severe(
                    "[SkyblockWorldManager] Island creation failed ($id): ${e.message}"
                )

                try {
                    deleteFileStructure(target)
                } catch (_: Exception) {}

                SchedulerUtil.runSync {
                    bar?.removeAll()
                    player.sendMessage("§c섬 생성 중 오류가 발생했습니다. 관리자에게 문의하세요.")
                }

            } finally {
                creatingIslands.remove(id)
                createSemaphore.release()
            }
        }
    }

    fun setRemoveIsland(id: String?) {
        if (id == null) return
        if (removingIslands.putIfAbsent(id, true) != null) return

        SchedulerUtil.runAsync {
            try {
                val targets = Bukkit.getWorlds()
                    .filter { it.name == "HwaSkyBlock.$id" }

                if (targets.isNotEmpty()) {
                    SchedulerUtil.runSync {
                        val mainWorldName =
                            ConfigManager.getConfig("setting")
                                ?.getString("main-spawn-world") ?: "world"

                        val spawn = Bukkit.getWorld(mainWorldName)?.spawnLocation

                        for (world in targets) {
                            for (player in world.players) {
                                spawn?.let { player.teleport(it) }
                                sendAction(player, "§cDeleting island...")
                                val bar = showProgressBar(
                                    player,
                                    "Deleting island...",
                                    1.0,
                                    BarColor.RED,
                                    BarStyle.SEGMENTED_10
                                )
                                SchedulerUtil.runLater(60L) { bar.removeAll() }
                            }
                            Bukkit.unloadWorld(world, true)
                        }
                    }
                }

                val path = Bukkit.getWorldContainer()
                    .toPath()
                    .resolve("HwaSkyBlock.$id")

                deleteFileStructure(path)

                SchedulerUtil.runSync {
                    Bukkit.getPluginManager()
                        .callEvent(SkyblockIslandRemoveEvent(id))
                }

            } finally {
                removingIslands.remove(id)
            }
        }
    }

    private fun copyFileStructureWithProgress(
        source: Path,
        target: Path,
        bar: BossBar?
    ) {
        val ignore = setOf("uid.dat", "session.lock")

        val files = Files.walk(source).use { stream ->
            stream.filter { !Files.isDirectory(it) }
                .filter { !ignore.contains(it.fileName.toString()) }
                .collect(Collectors.toList())
        }

        val total = files.size.coerceAtLeast(1)
        var count = 0

        Files.walk(source).use { stream ->
            stream.filter { Files.isDirectory(it) }.forEach { dir ->
                val dest = target.resolve(source.relativize(dir))
                if (!Files.exists(dest)) Files.createDirectories(dest)
            }
        }

        for (src in files) {
            val dest = target.resolve(source.relativize(src))
            try {
                Files.copy(
                    src,
                    dest,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
                )
                count++

                if (count % 10 == 0 || count == total) {
                    val percent = (count.toDouble() / total).coerceIn(0.0, 1.0)
                    SchedulerUtil.runSync {
                        bar?.apply {
                            progress = percent
                            setTitle("§aCopying world... §f${(percent * 100).toInt()}%")
                        }
                    }
                }

            } catch (e: Exception) {
                Bukkit.getLogger().warning(
                    "[SkyblockWorldManager] Copy failed: ${src.fileName} (${e.message})"
                )
            }
        }
    }

    private fun deleteFileStructure(path: Path) {
        if (!Files.exists(path)) return

        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                Files.deleteIfExists(file)
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                Files.deleteIfExists(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun sendAction(player: Player, message: String) {
        SchedulerUtil.runSync {
            try {
                player.spigot()
                    .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
            } catch (_: NoClassDefFoundError) {
                player.sendMessage(message)
            }
        }
    }

    private fun showProgressBar(
        player: Player,
        title: String,
        progress: Double,
        color: BarColor,
        style: BarStyle,
    ): BossBar {
        val bar = Bukkit.createBossBar(title, color, style)
        bar.progress = progress
        bar.addPlayer(player)
        return bar
    }
}

class SkyblockIslandCreateEvent(
    val player: Player,
    val id: Int,
) : Event() {
    override fun getHandlers(): HandlerList = handlerList
    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}

class SkyblockIslandRemoveEvent(
    val id: String,
) : Event() {
    override fun getHandlers(): HandlerList = handlerList
    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}

private object SchedulerUtil {

    fun runSync(task: () -> Unit) {
        if (Bukkit.isPrimaryThread()) task()
        else Bukkit.getScheduler()
            .runTask(HwaSkyBlock.plugin, Runnable { task() })
    }

    fun runAsync(task: () -> Unit) {
        Bukkit.getScheduler()
            .runTaskAsynchronously(HwaSkyBlock.plugin, Runnable { task() })
    }

    fun runLater(delay: Long, task: () -> Unit) {
        Bukkit.getScheduler()
            .runTaskLater(HwaSkyBlock.plugin, Runnable { task() }, delay)
    }

    fun <T> runSyncReturn(task: () -> T): T {
        return if (Bukkit.isPrimaryThread()) task()
        else {
            val future = CompletableFuture<T>()
            Bukkit.getScheduler().runTask(HwaSkyBlock.plugin, Runnable {
                try {
                    future.complete(task())
                } catch (t: Throwable) {
                    future.completeExceptionally(t)
                }
            })
            future.get()
        }
    }
}
