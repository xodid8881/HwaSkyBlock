package addon.dailymission.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarFile
import kotlin.collections.get

object AddonConfig {

    private lateinit var config: FileConfiguration

    var enabled = true

    var databaseType = "sqlite"
    var sqliteFile = "dailymission.db"
    var mysqlHost = "localhost"
    var mysqlPort = 3306
    var mysqlDatabase = "hwaskyblock"
    var mysqlUser = "root"
    var mysqlPassword = ""

    var mainPointEnabled = true
    var mainPointRatio = 1.0

    var allowCreative = false
    var cropCooldownMs = 500L
    var allowPlacedBlocks = false
    var allowSpawnEgg = false
    var allowSpawner = true
    var allowCommandSpawn = false
    var blockDropReacquire = true
    var blockDropReacquireMs = 3000L

    var guiFillerEnabled = true
    var guiFillerMaterial = "BLACK_STAINED_GLASS_PANE"
    var guiIconVeryEasy = "WHEAT_SEEDS"
    var guiIconEasy = "OAK_LOG"
    var guiIconNormal = "IRON_INGOT"
    var guiIconHard = "DIAMOND"
    var guiIconVeryHard = "NETHER_STAR"
    var guiIconNone = "BOOK"
    var guiIconProgress = "WRITABLE_BOOK"
    var guiIconDone = "LIME_STAINED_GLASS_PANE"
    var guiIconSetVeryEasyNone = ""
    var guiIconSetVeryEasyProgress = ""
    var guiIconSetVeryEasyDone = ""
    var guiIconSetEasyNone = ""
    var guiIconSetEasyProgress = ""
    var guiIconSetEasyDone = ""
    var guiIconSetNormalNone = ""
    var guiIconSetNormalProgress = ""
    var guiIconSetNormalDone = ""
    var guiIconSetHardNone = ""
    var guiIconSetHardProgress = ""
    var guiIconSetHardDone = ""
    var guiIconSetVeryHardNone = ""
    var guiIconSetVeryHardProgress = ""
    var guiIconSetVeryHardDone = ""
    var guiSlotVeryEasy = 10
    var guiSlotEasy = 12
    var guiSlotNormal = 14
    var guiSlotHard = 16
    var guiSlotVeryHard = 22
    var guiSlotInfo = 26
    var guiRows = 3
    var guiCustomEnabled = false
    var guiCustomItems = emptyList<GuiCustomItem>()
    var guiDetailRows = 3
    var guiDetailBackSlot = 22
    var guiDetailBackMaterial = "ARROW"
    var guiDetailInfoSlot = 13
    var guiDetailFillerEnabled = true
    var guiDetailFillerMaterial = "BLACK_STAINED_GLASS_PANE"
    var guiDetailRewardSlot = -1
    var guiDetailStatusSlot = -1
    var guiDetailPointSlot = -1
    var guiDetailButtons = emptyList<GuiDetailButton>()

    fun load(plugin: JavaPlugin) {
        val dir = File(plugin.dataFolder, "addons/SkyblockDailyMissionAddon")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "config.yml")
        if (!file.exists()) extractFromJar(plugin, "config.yml", file)

        config = YamlConfiguration.loadConfiguration(file)
        read()
    }

    private fun read() {
        enabled = config.getBoolean("enabled", true)

        databaseType = config.getString("database.type", "sqlite")!!.lowercase()
        sqliteFile = config.getString("database.sqlite.file", "dailymission.db")!!

        mysqlHost = config.getString("database.mysql.host", "localhost")!!
        mysqlPort = config.getInt("database.mysql.port", 3306)
        mysqlDatabase = config.getString("database.mysql.database", "hwaskyblock")!!
        mysqlUser = config.getString("database.mysql.user", "root")!!
        mysqlPassword = config.getString("database.mysql.password", "")!!

        mainPointEnabled = config.getBoolean("main-point.enabled", true)
        mainPointRatio = config.getDouble("main-point.ratio", 1.0)

        allowCreative = config.getBoolean("mission.allow-creative", false)
        cropCooldownMs = config.getLong("mission.crop-cooldown-ms", 500L)
        allowPlacedBlocks = config.getBoolean("mission.block-break.allow-placed", false)
        allowSpawnEgg = config.getBoolean("mission.entity.allow-spawn-egg", false)
        allowSpawner = config.getBoolean("mission.entity.allow-spawner", true)
        allowCommandSpawn = config.getBoolean("mission.entity.allow-command", false)
        blockDropReacquire = config.getBoolean("mission.item-pickup.block-drop-reacquire", true)
        blockDropReacquireMs = config.getLong("mission.item-pickup.block-drop-reacquire-ms", 3000L)

        guiFillerEnabled = config.getBoolean("gui.filler.enabled", true)
        guiFillerMaterial = config.getString("gui.filler.material", "BLACK_STAINED_GLASS_PANE")!!
        guiIconVeryEasy = config.getString("gui.icon.very-easy", "WHEAT_SEEDS")!!
        guiIconEasy = config.getString("gui.icon.easy", "OAK_LOG")!!
        guiIconNormal = config.getString("gui.icon.normal", "IRON_INGOT")!!
        guiIconHard = config.getString("gui.icon.hard", "DIAMOND")!!
        guiIconVeryHard = config.getString("gui.icon.very-hard", "NETHER_STAR")!!
        guiIconNone = config.getString("gui.icon.status.none", "BOOK")!!
        guiIconProgress = config.getString("gui.icon.status.progress", "WRITABLE_BOOK")!!
        guiIconDone = config.getString("gui.icon.status.done", "LIME_STAINED_GLASS_PANE")!!
        guiIconSetVeryEasyNone = config.getString("gui.icon.set.very-easy.none", "")!!
        guiIconSetVeryEasyProgress = config.getString("gui.icon.set.very-easy.progress", "")!!
        guiIconSetVeryEasyDone = config.getString("gui.icon.set.very-easy.done", "")!!
        guiIconSetEasyNone = config.getString("gui.icon.set.easy.none", "")!!
        guiIconSetEasyProgress = config.getString("gui.icon.set.easy.progress", "")!!
        guiIconSetEasyDone = config.getString("gui.icon.set.easy.done", "")!!
        guiIconSetNormalNone = config.getString("gui.icon.set.normal.none", "")!!
        guiIconSetNormalProgress = config.getString("gui.icon.set.normal.progress", "")!!
        guiIconSetNormalDone = config.getString("gui.icon.set.normal.done", "")!!
        guiIconSetHardNone = config.getString("gui.icon.set.hard.none", "")!!
        guiIconSetHardProgress = config.getString("gui.icon.set.hard.progress", "")!!
        guiIconSetHardDone = config.getString("gui.icon.set.hard.done", "")!!
        guiIconSetVeryHardNone = config.getString("gui.icon.set.very-hard.none", "")!!
        guiIconSetVeryHardProgress = config.getString("gui.icon.set.very-hard.progress", "")!!
        guiIconSetVeryHardDone = config.getString("gui.icon.set.very-hard.done", "")!!

        guiSlotVeryEasy = config.getInt("gui.slot.very-easy", 10)
        guiSlotEasy = config.getInt("gui.slot.easy", 12)
        guiSlotNormal = config.getInt("gui.slot.normal", 14)
        guiSlotHard = config.getInt("gui.slot.hard", 16)
        guiSlotVeryHard = config.getInt("gui.slot.very-hard", 22)
        guiSlotInfo = config.getInt("gui.slot.info", 26)
        guiRows = config.getInt("gui.rows", 3)

        guiCustomEnabled = config.getBoolean("gui.custom.enabled", false)
        val rawItems = config.getMapList("gui.custom.items")
        guiCustomItems = rawItems.mapNotNull { map ->
            val slot = map["slot"]?.toString()?.toIntOrNull() ?: return@mapNotNull null
            val material = map["material"]?.toString() ?: return@mapNotNull null
            val name = map["name"]?.toString()
            val lore = (map["lore"] as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
            val glow = map["glow"]?.toString()?.toBooleanStrictOrNull() ?: false
            val type = map["type"]?.toString()
            GuiCustomItem(slot, material, name, lore, glow, type)
        }

        guiDetailRows = config.getInt("gui.detail.rows", 3)
        guiDetailBackSlot = config.getInt("gui.detail.back-slot", 22)
        guiDetailBackMaterial = config.getString("gui.detail.back-material", "ARROW")!!
        guiDetailInfoSlot = config.getInt("gui.detail.info-slot", 13)
        guiDetailFillerEnabled = config.getBoolean("gui.detail.filler.enabled", true)
        guiDetailFillerMaterial = config.getString("gui.detail.filler.material", "BLACK_STAINED_GLASS_PANE")!!
        guiDetailRewardSlot = config.getInt("gui.detail.reward-slot", -1)
        guiDetailStatusSlot = config.getInt("gui.detail.status-slot", -1)
        guiDetailPointSlot = config.getInt("gui.detail.point-slot", -1)

        val rawButtons = config.getMapList("gui.detail.buttons")
        guiDetailButtons = rawButtons.mapNotNull { map ->
            val slot = map["slot"]?.toString()?.toIntOrNull() ?: return@mapNotNull null
            val material = map["material"]?.toString() ?: return@mapNotNull null
            val name = map["name"]?.toString()
            val lore = (map["lore"] as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
            val glow = map["glow"]?.toString()?.toBooleanStrictOrNull() ?: false
            val action = map["action"]?.toString() ?: "NONE"
            val value = map["value"]?.toString()
            GuiDetailButton(slot, material, name, lore, glow, action, value)
        }
    }

    private fun extractFromJar(plugin: JavaPlugin, path: String, outFile: File) {
        val jarFile = File(
            AddonConfig::class.java
                .protectionDomain
                .codeSource
                .location
                .toURI()
        )

        JarFile(jarFile).use { jar ->
            jar.getJarEntry(path)?.let { entry ->
                jar.getInputStream(entry).use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } ?: plugin.logger.severe(
                "[SkyblockDailyMissionAddon] $path not found in addon jar"
            )
        }
    }
}

data class GuiCustomItem(
    val slot: Int,
    val material: String,
    val name: String?,
    val lore: List<String>,
    val glow: Boolean,
    val type: String?
)

data class GuiDetailButton(
    val slot: Int,
    val material: String,
    val name: String?,
    val lore: List<String>,
    val glow: Boolean,
    val action: String,
    val value: String?
)
