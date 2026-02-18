package org.hwabaeg.hwaskyblock

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabaeg.hwaskyblock.addon.AddonLoader
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAPI
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAPIImpl
import org.hwabaeg.hwaskyblock.commands.HwaSkyBlockCommand
import org.hwabaeg.hwaskyblock.commands.HwaSkyBlockSettingCommand
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.DatabaseBackedSkyblockDataGateway
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
import org.hwabaeg.hwaskyblock.events.player.QuitEvent
import org.hwabaeg.hwaskyblock.events.player.UseEvent
import org.hwabaeg.hwaskyblock.schedules.HwaSkyBlockTask
import org.hwabaeg.hwaskyblock.schedules.PlayerPermissionTask
import org.hwabaeg.hwaskyblock.schedules.UnloadBorderTask
import org.hwabaeg.hwaskyblock.schedules.UnloadWorldTask
import org.hwabaeg.hwaskyblock.world.IslandWorlds
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class HwaSkyBlock : JavaPlugin() {
    private enum class EconomySetupResult {
        OK,
        VAULT_PLUGIN_MISSING,
        VAULT_PLUGIN_DISABLED,
        ECONOMY_PROVIDER_MISSING
    }

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
        server.pluginManager.registerEvents(QuitEvent(), this)
        server.pluginManager.registerEvents(PhysicsEvent(), this)
        server.pluginManager.registerEvents(PlaceEvent(), this)
        server.pluginManager.registerEvents(SpawnEvent(), this)
        server.pluginManager.registerEvents(UseEvent(), this)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun registerCommands() {
        val mainCommand = HwaSkyBlockCommand()
        Objects.requireNonNull<PluginCommand?>(server.getPluginCommand("섬")).apply {
            setExecutor(mainCommand)
            tabCompleter = mainCommand
        }

        val settingCommand = HwaSkyBlockSettingCommand()
        Objects.requireNonNull<PluginCommand?>(server.getPluginCommand("섬설정")).apply {
            setExecutor(settingCommand)
            tabCompleter = settingCommand
        }
    }

    lateinit var db: SQLiteManager
    private lateinit var addonLoader: AddonLoader
    private var economyRetryTask: BukkitTask? = null
    private var runtimeStarted: Boolean = false

    @Suppress("DEPRECATION")
    override fun onEnable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Enable")

        addonLoader = AddonLoader(this)
        addonLoader.loadAll()

        saveDefaultConfig()
        saveResource("message.yml", false)
        ConfigManager.setupConfigs(this)
        logLoadedGuiTitles()
        configureIslandWorldPrefix()

        when (val dbType = ConfigManager.getConfig("setting")!!.getString("database.type")) {
            "mysql" -> MySQLManager.init(this)
            "sqlite" -> SQLiteManager.init(this)
            else -> error("지원하지 않는 데이터베이스 타입입니다: $dbType")
        }


        registerCommands()
        registerEvents()
        onlineNameCache.clear()
        for (player in server.onlinePlayers) {
            onlineNameCache.add(player.name)
        }

        when (val economyResult = setupEconomy()) {
            EconomySetupResult.OK -> startRuntime()
            EconomySetupResult.VAULT_PLUGIN_MISSING -> {
                logger.severe("[${description.name}] Vault 플러그인을 찾지 못해 비활성화됩니다.")
                server.pluginManager.disablePlugin(this)
                return
            }
            EconomySetupResult.VAULT_PLUGIN_DISABLED -> {
                logger.severe("[${description.name}] Vault 플러그인이 비활성화 상태라 비활성화됩니다.")
                server.pluginManager.disablePlugin(this)
                return
            }
            EconomySetupResult.ECONOMY_PROVIDER_MISSING -> {
                logger.warning("[${description.name}] Vault는 감지되었지만 Economy Provider가 아직 없습니다.")
                logger.warning("[${description.name}] Economy Provider 등록 대기 후 재시도합니다.")
                startEconomyRetry()
            }
        }
    }

    private fun startRuntime() {
        if (runtimeStarted) return
        runtimeStarted = true
        HwaSkyBlockTask().runTaskTimerAsynchronously(this, 40L, 40L)
        UnloadWorldTask().runTaskTimer(this, 0L, 400L)
        UnloadBorderTask().runTaskTimer(this, 0L, 400L)
        PlayerPermissionTask().runTaskTimer(this, 0L, 50L)

        api = HwaSkyBlockAPIImpl(DatabaseBackedSkyblockDataGateway())
        DatabaseManager().loadAllSkyblocks()
    }

    private fun startEconomyRetry(maxAttempts: Int = 20, periodTicks: Long = 20L) {
        economyRetryTask?.cancel()
        var attempts = 0
        economyRetryTask = server.scheduler.runTaskTimer(this, Runnable {
            attempts++
            when (val result = setupEconomy()) {
                EconomySetupResult.OK -> {
                    logger.info("[${description.name}] Economy Provider 감지 완료. 초기화를 계속 진행합니다.")
                    economyRetryTask?.cancel()
                    economyRetryTask = null
                    startRuntime()
                }
                EconomySetupResult.VAULT_PLUGIN_MISSING,
                EconomySetupResult.VAULT_PLUGIN_DISABLED -> {
                    logger.severe("[${description.name}] Vault 상태가 유효하지 않아 비활성화됩니다. 상태: $result")
                    economyRetryTask?.cancel()
                    economyRetryTask = null
                    server.pluginManager.disablePlugin(this)
                }
                EconomySetupResult.ECONOMY_PROVIDER_MISSING -> {
                    if (attempts >= maxAttempts) {
                        logger.severe("[${description.name}] Economy Provider 대기 시간 초과로 비활성화됩니다.")
                        logger.severe("[${description.name}] EssentialsX Economy/HwaEconomy 등의 provider 활성화를 확인하세요.")
                        val providers = server.servicesManager.getRegistrations(Economy::class.java)
                            ?.mapNotNull { it.provider?.javaClass?.name }
                            .orEmpty()
                        if (providers.isNotEmpty()) {
                            logger.severe("[${description.name}] 감지된 Economy Provider: ${providers.joinToString()}")
                        } else {
                            logger.severe("[${description.name}] 감지된 Economy Provider가 없습니다.")
                        }
                        economyRetryTask?.cancel()
                        economyRetryTask = null
                        server.pluginManager.disablePlugin(this)
                    }
                }
            }
        }, periodTicks, periodTicks)
    }

    private fun setupEconomy(): EconomySetupResult {
        val vaultPlugin = server.pluginManager.getPlugin("Vault")
        if (vaultPlugin == null || !vaultPlugin.isEnabled) {
            return if (vaultPlugin == null) {
                EconomySetupResult.VAULT_PLUGIN_MISSING
            } else {
                EconomySetupResult.VAULT_PLUGIN_DISABLED
            }
        }

        val rsp = server.servicesManager.getRegistration(Economy::class.java)
        if (rsp?.provider == null) {
            return EconomySetupResult.ECONOMY_PROVIDER_MISSING
        }

        economy = rsp.provider
        return EconomySetupResult.OK
    }

    private fun configureIslandWorldPrefix() {
        val setting = ConfigManager.getConfig("setting")
        val explicit = setting?.getString("sky-block-world-prefix")
        val sectionPrefix = setting
            ?.getConfigurationSection("sky-block-world")
            ?.getKeys(false)
            ?.firstOrNull()
        IslandWorlds.configure(explicit ?: sectionPrefix)
        logger.info("[${description.name}] Island world prefix: ${IslandWorlds.prefix()}")
    }

    private fun logLoadedGuiTitles() {
        val message = ConfigManager.getConfig("message")
        if (message == null) {
            logger.warning("[${description.name}] message.yml load failed")
            return
        }

        val keys = listOf(
            "gui-name.buy",
            "gui-name.global_setting",
            "gui-name.global_use_list",
            "gui-name.sharer_setting",
            "gui-name.sharer_use_list",
            "gui-name.sky_block_menu_list",
            "gui-name.world_setting",
            "gui-name.geyser_menu_list"
        )

        for (key in keys) {
            val value = message.getString(key)
            logger.info("[${description.name}] $key = ${value ?: "<null>"}")
        }
    }

    override fun onDisable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Disable")
        economyRetryTask?.cancel()
        economyRetryTask = null
        ConfigManager.saveConfigs()
        if (this::addonLoader.isInitialized) addonLoader.disableAll()
    }

    companion object {
        lateinit var api: HwaSkyBlockAPI
            private set

        private lateinit var addonLoader: AddonLoader

        var economy: Economy? = null
            private set

        val onlineNameCache: MutableSet<String> = ConcurrentHashMap.newKeySet()

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
