package addon.dailymission.logic

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.island.AssignedMission
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import addon.dailymission.mission.Mission
import addon.dailymission.mission.MissionDifficulty
import addon.dailymission.mission.MissionRegistry
import addon.dailymission.mission.MissionTarget
import addon.dailymission.mission.MissionType
import addon.dailymission.storage.MissionDataStore
import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import java.time.LocalDate
import java.time.ZoneId
import kotlin.collections.iterator
import kotlin.math.roundToInt

class MissionService(
    private val config: AddonConfig,
    private var registry: MissionRegistry,
    private val store: MissionDataStore
) {
    fun reloadRegistry(newRegistry: MissionRegistry) {
        registry = newRegistry
    }

    fun getMissionById(id: String): Mission? = registry.getById(id)

    fun getTodayAssignments(islandId: String): Map<MissionDifficulty, AssignedMission> {
        val date = today()
        val existing = store.getAssignments(islandId, date).associateBy { it.difficulty }.toMutableMap()

        for (difficulty in MissionDifficulty.entries) {
            if (existing.containsKey(difficulty)) continue
            val seed = (islandId + date + difficulty.key).hashCode().toLong()
            val mission = registry.getRandom(difficulty, seed) ?: continue
            store.insertAssignment(islandId, date, difficulty, mission.id)
            existing[difficulty] = AssignedMission(difficulty, mission.id, 0, false)
        }

        return existing
    }

    fun recordProgress(
        islandId: String,
        type: MissionType,
        targetKey: String,
        amount: Int,
        player: Player
    ): List<ProgressUpdate> {
        if (amount <= 0) return emptyList()
        val date = today()
        val assignments = getTodayAssignments(islandId)
        val updates = mutableListOf<ProgressUpdate>()

        for ((difficulty, assigned) in assignments) {
            if (assigned.completed) continue

            val mission = registry.getById(assigned.missionId) ?: continue
            if (mission.type != type) continue
            if (!matchesTarget(mission.target, type, targetKey)) continue

            val total = store.addProgress(islandId, date, difficulty, amount)
            val completedNow = total >= mission.amount
            if (completedNow) {
                store.markCompleted(islandId, date, difficulty)
                reward(islandId, mission, player)
                notifyIsland(islandId, mission)
            }
            updates.add(
                ProgressUpdate(
                    mission = mission,
                    current = total,
                    goal = mission.amount,
                    completedNow = completedNow
                )
            )
        }
        return updates
    }

    private fun matchesTarget(target: MissionTarget, type: MissionType, key: String): Boolean {
        return when (type) {
            MissionType.BLOCK_BREAK,
            MissionType.CROP_HARVEST,
            MissionType.CRAFT,
            MissionType.SMELT -> target.matchesMaterial(key)
            MissionType.ENTITY_KILL -> target.matchesEntity(key)
            MissionType.SPECIAL -> true
        }
    }

    private fun reward(islandId: String, mission: Mission, player: Player) {
        store.addMissionPoint(islandId, mission.reward)

        if (config.mainPointEnabled) {
            val mainPoint = (mission.reward * config.mainPointRatio).roundToInt()
            val islandNumber = islandId.toIntOrNull()
            if (islandNumber != null) {
                HwaSkyBlock.api.addIslandPoint(player, islandNumber, mainPoint)
            }
        }
    }

    private fun notifyIsland(islandId: String, mission: Mission) {
        val worldName = "HwaSkyBlock.$islandId"
        val world = Bukkit.getWorld(worldName) ?: return
        val msg = MessageConfig.broadcastComplete
            .replace("%name%", mission.name)
            .replace("%reward%", mission.reward.toString())
        val colored = ChatColor.translateAlternateColorCodes('&', msg)

        for (p in world.players) {
            p.sendMessage(colored)
            p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            p.spawnParticle(Particle.HAPPY_VILLAGER, p.location, 25, 0.5, 1.0, 0.5)
        }
    }

    private fun today(): String =
        LocalDate.now(ZoneId.systemDefault()).toString()
}
