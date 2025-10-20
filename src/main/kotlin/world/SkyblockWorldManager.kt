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
import java.util.stream.Collectors
import kotlin.io.path.isDirectory

object SkyblockWorldManager {

    fun setRemoveIsland(id: String?) {
        if (id == null) return

        SchedulerUtil.runAsync {
            val targets = Bukkit.getWorlds().filter { it.worldFolder.name == "HwaSkyBlock.$id" }

            if (targets.isNotEmpty()) {
                SchedulerUtil.runSync {
                    val mainWorldName = ConfigManager.getConfig("setting")!!.getString("main-spawn-world") ?: "world"
                    val mainWorld = Bukkit.getWorld(mainWorldName)
                    val spawn = mainWorld?.spawnLocation

                    for (world in targets) {
                        for (player in world.players) {
                            if (spawn != null) player.teleport(spawn)
                            sendAction(player, "§cDeleting island...")
                            val bar = showProgressBar(player, "Deleting island...", 1.0, BarColor.RED, BarStyle.SEGMENTED_10)
                            SchedulerUtil.runLater(60L) { bar.removeAll() }
                        }
                        Bukkit.unloadWorld(world, true)
                    }
                }
            }

            val path = Paths.get(System.getProperty("user.dir"), "worlds", "HwaSkyBlock.$id")
            deleteFileStructure(path)

            SchedulerUtil.runSync {
                Bukkit.getPluginManager().callEvent(SkyblockIslandRemoveEvent(id))
            }
        }
    }

    fun addIsland(player: Player, id: Int, filepath: String) {
        SchedulerUtil.runAsync {
            val worldName = "HwaSkyBlock.$id"
            val source = Paths.get(filepath)
            val target = Paths.get(Bukkit.getWorldContainer().path, worldName)

            val bar: BossBar = SchedulerUtil.runSyncReturn {
                showProgressBar(player, "Generating your island...", 0.0, BarColor.GREEN, BarStyle.SEGMENTED_12)
            }

            if (!Files.exists(target)) {
                sendAction(player, "§aCopying world data...")
                copyFileStructureWithProgress(source, target, player, bar)
            }

            SchedulerUtil.runSync {
                val world = WorldCreator(worldName).createWorld()
                if (world != null) player.teleport(world.spawnLocation)

                bar.setProgress(1.0)
                bar.setTitle("§aIsland Ready!")

                SchedulerUtil.runLater(100L) { bar.removeAll() }
                Bukkit.getPluginManager().callEvent(SkyblockIslandCreateEvent(player, id))
            }
        }
    }

    private fun copyFileStructureWithProgress(source: Path, target: Path, player: Player, bar: BossBar) {
        val ignore = setOf("uid.dat", "session.lock")

        val files = Files.walk(source).use { stream ->
            stream.filter { !it.isDirectory() && !ignore.contains(it.fileName.toString()) }
                .collect(Collectors.toList())
        }
        val total = files.size.coerceAtLeast(1)
        var count = 0

        Files.walk(source).use { stream ->
            stream.filter { it.isDirectory() }.forEach { dir ->
                val dest = target.resolve(source.relativize(dir))
                if (!Files.exists(dest)) Files.createDirectories(dest)
            }
        }

        for (src in files) {
            val dest = target.resolve(source.relativize(src))
            try {
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
                count++
                if (count % 10 == 0 || count == total) {
                    val percent = (count.toDouble() / total.toDouble()).coerceIn(0.0, 1.0)
                    SchedulerUtil.runSync {
                        bar.setProgress(percent)
                        bar.setTitle("§aCopying world... §f${(percent * 100).toInt()}%")
                    }
                }
            } catch (e: Exception) {
                Bukkit.getLogger().warning("⚠️ Copy failed: ${src.fileName} (${e.message})")
            }
        }
    }

    private fun deleteFileStructure(path: Path) {
        if (!Files.exists(path)) return
        try {
            Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                    try { Files.deleteIfExists(file) } catch (e: IOException) {
                        Bukkit.getLogger().warning("⚠️ Delete failed (file): ${file.fileName} (${e.message})")
                    }
                    return FileVisitResult.CONTINUE
                }
                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    try { Files.deleteIfExists(dir) } catch (e: IOException) {
                        Bukkit.getLogger().warning("⚠️ Delete failed (dir): ${dir.fileName} (${e.message})")
                    }
                    return FileVisitResult.CONTINUE
                }
            })
            Bukkit.getLogger().info("🗑️ ${path.fileName} delete completed")
        } catch (e: Exception) {
            Bukkit.getLogger().warning("⚠️ Folder delete error: ${e.message}")
        }
    }

    private fun sendAction(player: Player, message: String) {
        SchedulerUtil.runSync {
            try {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
            } catch (e: NoClassDefFoundError) {
                player.sendMessage(message)
            }
        }
    }

    private fun showProgressBar(
        player: Player,
        title: String,
        progress: Double,
        color: BarColor,
        style: BarStyle
    ): BossBar {
        val bar = Bukkit.createBossBar(title, color, style)
        bar.setProgress(progress)
        bar.addPlayer(player)
        return bar
    }
}

class SkyblockIslandCreateEvent(
    val player: Player,
    val id: Int
) : Event() {
    override fun getHandlers(): HandlerList = handlerList
    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}

class SkyblockIslandRemoveEvent(
    val id: String
) : Event() {
    override fun getHandlers(): HandlerList = handlerList
    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}

private object SchedulerUtil {

    fun runSync(task: () -> Unit) {
        if (Bukkit.isPrimaryThread()) task()
        else Bukkit.getScheduler().runTask(HwaSkyBlock.plugin, Runnable { task() })
    }

    fun runAsync(task: () -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(HwaSkyBlock.plugin, Runnable { task() })
    }

    fun runLater(delay: Long, task: () -> Unit) {
        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable { task() }, delay)
    }

    fun <T> runSyncReturn(task: () -> T): T {
        return if (Bukkit.isPrimaryThread()) task()
        else {
            val future = CompletableFuture<T>()
            Bukkit.getScheduler().runTask(HwaSkyBlock.plugin, Runnable {
                try { future.complete(task()) } catch (t: Throwable) { future.completeExceptionally(t) }
            })
            future.get()
        }
    }
}
