package addon.dailymission.mission

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.collections.get
import kotlin.random.Random

class MissionRegistry(
    private val missionsByDifficulty: Map<MissionDifficulty, List<Mission>>,
    private val missionsById: Map<String, Mission>
) {
    fun getById(id: String): Mission? = missionsById[id]

    fun getRandom(difficulty: MissionDifficulty, seed: Long): Mission? {
        val list = missionsByDifficulty[difficulty].orEmpty().filter { it.enabled }
        if (list.isEmpty()) return null
        val rng = Random(seed)
        return list[rng.nextInt(list.size)]
    }

    fun listByDifficulty(difficulty: MissionDifficulty): List<Mission> =
        missionsByDifficulty[difficulty].orEmpty().filter { it.enabled }

    companion object {
        fun load(plugin: JavaPlugin): MissionRegistry {
            val dir = File(plugin.dataFolder, "addons/SkyblockDailyMissionAddon")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "config.yml")
            val cfg = YamlConfiguration.loadConfiguration(file)
            return fromConfig(cfg)
        }

        private fun fromConfig(cfg: FileConfiguration): MissionRegistry {
            val byDifficulty = mutableMapOf<MissionDifficulty, MutableList<Mission>>()
            val byId = mutableMapOf<String, Mission>()

            val missionsSection = cfg.getConfigurationSection("missions") ?: return MissionRegistry(byDifficulty, byId)

            for (difficultyKey in missionsSection.getKeys(false)) {
                val diff = MissionDifficulty.fromKey(difficultyKey) ?: continue
                val list = missionsSection.getMapList(difficultyKey)

                for (raw in list) {
                    val id = raw["id"]?.toString()?.trim().orEmpty()
                    if (id.isBlank()) continue

                    val name = raw["name"]?.toString() ?: id
                    val description = raw["description"]?.toString() ?: ""
                    val type = MissionType.fromKey(raw["type"]?.toString() ?: "") ?: continue
                    val target = MissionTarget.parse(raw["target"]?.toString())
                    val amount = raw["amount"]?.toString()?.toIntOrNull() ?: 1
                    val reward = raw["reward"]?.toString()?.toIntOrNull() ?: 0
                    val enabled = raw["enabled"]?.toString()?.toBooleanStrictOrNull() ?: true

                    val mission = Mission(
                        id = id,
                        name = name,
                        description = description,
                        difficulty = diff,
                        type = type,
                        target = target,
                        amount = amount,
                        reward = reward,
                        enabled = enabled
                    )

                    byDifficulty.getOrPut(diff) { mutableListOf() }.add(mission)
                    byId[id] = mission
                }
            }

            return MissionRegistry(byDifficulty, byId)
        }
    }
}
