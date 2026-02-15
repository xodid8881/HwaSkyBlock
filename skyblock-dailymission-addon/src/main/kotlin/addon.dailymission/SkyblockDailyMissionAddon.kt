package addon.dailymission

import addon.dailymission.antiabuse.AntiAbuseGuard
import addon.dailymission.command.CommandRegistrar
import addon.dailymission.command.IslandMissionCommand
import addon.dailymission.command.IslandMissionReloadCommand
import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.gui.IslandMissionGUI
import addon.dailymission.listener.BlockMissionListener
import addon.dailymission.listener.BlockPlaceListener
import addon.dailymission.listener.CombatMissionListener
import addon.dailymission.listener.CraftMissionListener
import addon.dailymission.listener.CropMissionListener
import addon.dailymission.listener.ItemDropListener
import addon.dailymission.listener.ItemPickupListener
import addon.dailymission.listener.MissionGuiListener
import addon.dailymission.listener.SmeltMissionListener
import addon.dailymission.listener.SpawnMissionListener
import addon.dailymission.logic.MissionProgressProcessor
import addon.dailymission.logic.MissionService
import addon.dailymission.mission.MissionRegistry
import addon.dailymission.placeholder.DailyMissionPlaceholderExpansion
import addon.dailymission.storage.MissionDataStore
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import java.util.logging.Logger

class SkyblockDailyMissionAddon : HwaSkyBlockAddon, Listener {

    private lateinit var plugin: JavaPlugin
    private lateinit var log: Logger

    private lateinit var dataStore: MissionDataStore
    private lateinit var registry: MissionRegistry
    private lateinit var missionService: MissionService
    private lateinit var progressProcessor: MissionProgressProcessor
    private lateinit var gui: IslandMissionGUI
    private lateinit var antiAbuse: AntiAbuseGuard
    private var placeholderExpansion: DailyMissionPlaceholderExpansion? = null

    override fun onEnable(main: JavaPlugin) {
        plugin = main
        log = Logger.getLogger("HwaSkyBlock:DailyMission")

        AddonConfig.load(plugin)
        MessageConfig.load(plugin)
        if (!AddonConfig.enabled) {
            log.info("DailyMissionAddon is disabled by config")
            return
        }

        registry = MissionRegistry.Companion.load(plugin)
        dataStore = MissionDataStore.Companion.create(AddonConfig, plugin)
        missionService = MissionService(AddonConfig, registry, dataStore)
        antiAbuse = AntiAbuseGuard(AddonConfig)
        progressProcessor = MissionProgressProcessor(AddonConfig, missionService)
        gui = IslandMissionGUI(AddonConfig, missionService, dataStore)

        registerListeners()
        registerCommands()
        registerPlaceholders()

        log.info("SkyblockDailyMissionAddon enabled")
    }

    override fun onDisable() {
        placeholderExpansion?.unregister()
        placeholderExpansion = null
        if (this::dataStore.isInitialized) dataStore.close()
        log.info("SkyblockDailyMissionAddon disabled")
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(BlockMissionListener(progressProcessor, antiAbuse), plugin)
        pm.registerEvents(CropMissionListener(progressProcessor), plugin)
        pm.registerEvents(CombatMissionListener(progressProcessor, antiAbuse), plugin)
        pm.registerEvents(CraftMissionListener(progressProcessor), plugin)
        pm.registerEvents(SmeltMissionListener(progressProcessor), plugin)
        pm.registerEvents(MissionGuiListener(gui), plugin)
        pm.registerEvents(BlockPlaceListener(antiAbuse), plugin)
        pm.registerEvents(SpawnMissionListener(antiAbuse), plugin)
        pm.registerEvents(ItemDropListener(antiAbuse), plugin)
        pm.registerEvents(ItemPickupListener(antiAbuse), plugin)
    }

    private fun registerCommands() {
        val registrar = CommandRegistrar(Bukkit.getServer())

        registrar.register(
            IslandMissionCommand(
                AddonConfig,
                missionService,
                dataStore,
                gui
            )
        )

        registrar.register(
            IslandMissionReloadCommand(
                AddonConfig,
                missionService,
                dataStore,
                registry,
                plugin
            )
        )
    }

    private fun registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            log.info("PlaceholderAPI not found. PAPI placeholders are disabled.")
            return
        }

        placeholderExpansion = DailyMissionPlaceholderExpansion(plugin, missionService, dataStore)
        val registered = placeholderExpansion?.register() == true
        if (registered) {
            log.info("DailyMission PAPI placeholders registered with identifier: isdm")
        } else {
            log.warning("Failed to register DailyMission PAPI placeholders.")
        }
    }
}
