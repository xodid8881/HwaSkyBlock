package org.hwabeag.hwaskyblock

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.hwabeag.hwaskyblock.api.HwaSkyBlockAPI
import org.hwabeag.hwaskyblock.api.HwaSkyBlockAPIImpl
import org.hwabeag.hwaskyblock.commands.HwaSkyBlockCommand
import org.hwabeag.hwaskyblock.commands.HwaSkyBlockSettingCommand
import org.hwabeag.hwaskyblock.config.ConfigManager
import org.hwabeag.hwaskyblock.events.block.BreakEvent
import org.hwabeag.hwaskyblock.events.block.PhysicsEvent
import org.hwabeag.hwaskyblock.events.block.PlaceEvent
import org.hwabeag.hwaskyblock.events.click.*
import org.hwabeag.hwaskyblock.events.entity.SpawnEvent
import org.hwabeag.hwaskyblock.events.player.JoinEvent
import org.hwabeag.hwaskyblock.events.player.MoveEvent
import org.hwabeag.hwaskyblock.events.player.UseEvent
import org.hwabeag.hwaskyblock.schedules.HwaSkyBlockTask
import org.hwabeag.hwaskyblock.schedules.UnloadBorderTask
import org.hwabeag.hwaskyblock.schedules.UnloadWorldTask
import java.io.*
import java.util.*

class HwaSkyBlock : JavaPlugin() {
    private fun registerEvents() {
        server.pluginManager.registerEvents(BreakEvent(), this)

        server.pluginManager.registerEvents(InvBuyClickEvent(), this)
        server.pluginManager.registerEvents(InvGlobalFragClickEvent(), this)
        server.pluginManager.registerEvents(InvGlobalUseClickEvent(), this)
        server.pluginManager.registerEvents(InvMenuClickEvent(), this)
        server.pluginManager.registerEvents(InvSettingClickEvent(), this)
        server.pluginManager.registerEvents(InvSharerClickEvent(), this)
        server.pluginManager.registerEvents(InvSharerUseClickEvent(), this)

        server.pluginManager.registerEvents(JoinEvent(), this)
        server.pluginManager.registerEvents(MoveEvent(), this)
        server.pluginManager.registerEvents(PhysicsEvent(), this)
        server.pluginManager.registerEvents(PlaceEvent(), this)
        server.pluginManager.registerEvents(SpawnEvent(), this)
        server.pluginManager.registerEvents(UseEvent(), this)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun registerCommands() {
        Objects.requireNonNull<PluginCommand?>(server.getPluginCommand("섬")).setExecutor(HwaSkyBlockCommand())
        Objects.requireNonNull<PluginCommand?>(server.getPluginCommand("섬설정"))
            .setExecutor(HwaSkyBlockSettingCommand())
    }

    @Suppress("DEPRECATION")
    override fun onEnable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Enable")
        saveResource("message.yml", false)
        saveDefaultConfig()
        configManager
        registerCommands()
        registerEvents()
        if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Vault 종속성이 발견되지 않아 비활성화됨!", description.name))
            server.pluginManager.disablePlugin(this)
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, HwaSkyBlockTask(), (20 * 2).toLong(), (20 * 2).toLong())
        Bukkit.getScheduler().runTaskTimer(this, UnloadWorldTask(), 0L, 400L)
        Bukkit.getScheduler().runTaskTimer(this, UnloadBorderTask(), 0L, 400L)
        api = HwaSkyBlockAPIImpl()
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration<Economy?>(Economy::class.java)
        if (rsp == null) {
            return false
        }
        economy = rsp.getProvider()
        return true
    }

    override fun onDisable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Disable")
        ConfigManager.saveConfigs()
    }

    companion object {
        lateinit var api: HwaSkyBlockAPI
            private set

        val configManager: ConfigManager by lazy { ConfigManager() }

        var economy: Economy? = null
            private set

        val plugin: HwaSkyBlock
            get() = getPlugin(HwaSkyBlock::class.java)

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
            "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
        )
        fun setRemoveIsland(id: String?) {
            for (world in Bukkit.getServer().worlds) {
                val worldName = world.worldFolder.name
                if (("HwaSkyBlock.$id") == worldName) {
                    for (player in world.players) {
                        val worldPath = ConfigManager.getConfig("setting")!!.getString("main-spawn-world")
                        val mainWorld = Bukkit.getServer().getWorld(Objects.requireNonNull<String?>(worldPath))
                        val spawnLocation = Objects.requireNonNull<World?>(mainWorld).spawnLocation
                        player.teleport(spawnLocation)
                    }
                    Bukkit.getServer().unloadWorld(world, true)
                }
            }
            val serverDir = System.getProperty("user.dir")
            val worldPath = "$serverDir/worlds/HwaSkyBlock.$id"
            deleteFileStructure(File(worldPath))
        }

        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun addIsland(player: Player, id: Int, filepath: String) {
            var world = Bukkit.getServer().getWorld(filepath)
            if (world == null) {
                world = WorldCreator(filepath).createWorld()
                val worldName = "worlds/HwaSkyBlock.$id"
                copyFileStructure(
                    Objects.requireNonNull<World?>(world).worldFolder,
                    File(Bukkit.getWorldContainer(), worldName)
                )
                WorldCreator(worldName).createWorld()
                val location = Objects.requireNonNull<World?>(Bukkit.getServer().getWorld("worlds/HwaSkyBlock.$id"))
                    .spawnLocation
                player.teleport(location)
            } else {
                val worldName = "worlds/HwaSkyBlock.$id"
                copyFileStructure(
                    Objects.requireNonNull<World?>(world).worldFolder,
                    File(Bukkit.getWorldContainer(), worldName)
                )
                WorldCreator(worldName).createWorld()
                val location = Objects.requireNonNull<World?>(Bukkit.getServer().getWorld("worlds/HwaSkyBlock.$id"))
                    .spawnLocation
                player.teleport(location)
            }
        }

        private fun copyFileStructure(source: File, target: File) {
            try {
                val ignore = ArrayList<String?>(mutableListOf<String?>("uid.dat", "session.lock"))
                if (!ignore.contains(source.name)) {
                    if (source.isDirectory) {
                        if (!target.exists()) if (!target.mkdirs()) throw IOException("세계 디렉토리를 만들 수 없습니다!")
                        val files = source.list()
                        for (file in files!!) {
                            val srcFile = File(source, file)
                            val destFile = File(target, file)
                            copyFileStructure(srcFile, destFile)
                        }
                    } else {
                        val `in`: InputStream = FileInputStream(source)
                        val out: OutputStream = FileOutputStream(target)
                        val buffer = ByteArray(1024)
                        var length: Int
                        while ((`in`.read(buffer).also { length = it }) > 0) out.write(buffer, 0, length)
                        `in`.close()
                        out.close()
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        private fun deleteFileStructure(target: File) {
            try {
                if (target.isDirectory) {
                    val files = target.list()
                    if (files != null) {
                        for (file in files) {
                            val subFile = File(target, file)
                            deleteFileStructure(subFile)
                        }
                    }
                }
                if (target.delete()) {
                    println(target.absolutePath + " 삭제 성공")
                } else {
                    println(target.absolutePath + " 삭제 실패")
                }
            } catch (e: Exception) {
                throw RuntimeException("파일 삭제 중 오류 발생", e)
            }
        }
    }
}