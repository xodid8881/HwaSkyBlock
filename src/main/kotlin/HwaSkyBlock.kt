package org.hwabaeg.hwaskyblock

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAPI
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAPIImpl
import org.hwabaeg.hwaskyblock.commands.HwaSkyBlockCommand
import org.hwabaeg.hwaskyblock.commands.HwaSkyBlockSettingCommand
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.hwabaeg.hwaskyblock.database.sqlite.SQLiteManager
import org.hwabaeg.hwaskyblock.events.block.BreakEvent
import org.hwabaeg.hwaskyblock.events.block.PhysicsEvent
import org.hwabaeg.hwaskyblock.events.block.PlaceEvent
import org.hwabaeg.hwaskyblock.events.click.*
import org.hwabaeg.hwaskyblock.events.click.geyser.GeyserMenuClickEvent
import org.hwabaeg.hwaskyblock.events.entity.SpawnEvent
import org.hwabaeg.hwaskyblock.events.player.JoinEvent
import org.hwabaeg.hwaskyblock.events.player.MoveEvent
import org.hwabaeg.hwaskyblock.events.player.UseEvent
import org.hwabaeg.hwaskyblock.schedules.HwaSkyBlockTask
import org.hwabaeg.hwaskyblock.schedules.UnloadBorderTask
import org.hwabaeg.hwaskyblock.schedules.UnloadWorldTask
import java.io.*
import java.util.*

class HwaSkyBlock : JavaPlugin() {

    private fun registerEvents() {
        server.pluginManager.registerEvents(BreakEvent(), this)

        server.pluginManager.registerEvents(GeyserMenuClickEvent(), this)
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

    lateinit var db: SQLiteManager

    @Suppress("DEPRECATION")
    override fun onEnable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Enable")

        saveDefaultConfig()
        saveResource("message.yml", false)
        ConfigManager.setupConfigs(this)

        when (val dbType = ConfigManager.getConfig("setting")!!.getString("database.type")) {
            "mysql" -> MySQLManager.init(this)
            "sqlite" -> SQLiteManager.init(this)
            else -> error("지원하지 않는 데이터베이스 타입입니다: $dbType")
        }


        registerCommands()
        registerEvents()

        if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Vault 종속성이 발견되지 않아 비활성화됨!", description.name))
            server.pluginManager.disablePlugin(this)
            return
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, HwaSkyBlockTask(), 20L * 2, 20L * 2)
        Bukkit.getScheduler().runTaskTimer(this, UnloadWorldTask(), 0L, 400L)
        Bukkit.getScheduler().runTaskTimer(this, UnloadBorderTask(), 0L, 400L)

        api = HwaSkyBlockAPIImpl()
    }

    private fun setupEconomy(): Boolean {
        val vaultPlugin = server.pluginManager.getPlugin("Vault")
        if (vaultPlugin == null || !vaultPlugin.isEnabled) {
            return false
        }

        val rsp = server.servicesManager.getRegistration(Economy::class.java)
        if (rsp?.provider == null) {
            return false
        }

        economy = rsp.provider
        return true
    }

    override fun onDisable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Disable")
        ConfigManager.saveConfigs()
    }

    companion object {
        lateinit var api: HwaSkyBlockAPI
            private set

        var economy: Economy? = null
            private set

        val plugin: HwaSkyBlock
            get() = getPlugin(HwaSkyBlock::class.java)

        fun isBedrockPlayer(player: Player): Boolean {
            val plugin = Bukkit.getPluginManager().getPlugin("floodgate")
            if (plugin == null || !plugin.isEnabled) return false

            return try {
                FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
            } catch (e: NoClassDefFoundError) {
                false
            } catch (e: Exception) {
                false
            }
        }
    }
}