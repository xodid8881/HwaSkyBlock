package addon.dailymission.placeholder

import addon.dailymission.island.AssignedMission
import addon.dailymission.logic.MissionService
import addon.dailymission.mission.Mission
import addon.dailymission.mission.MissionDifficulty
import addon.dailymission.mission.TargetType
import addon.dailymission.storage.MissionDataStore
import addon.dailymission.util.IslandResolver
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.ConcurrentHashMap

class DailyMissionPlaceholderExpansion(
    private val plugin: JavaPlugin,
    private val missionService: MissionService,
    private val store: MissionDataStore
) : PlaceholderExpansion() {

    private val cacheTtlMs = 1000L
    private val snapshotCache = ConcurrentHashMap<String, CachedSnapshot>()
    private val difficultyOrder = listOf(
        MissionDifficulty.VERY_EASY,
        MissionDifficulty.EASY,
        MissionDifficulty.NORMAL,
        MissionDifficulty.HARD,
        MissionDifficulty.VERY_HARD
    )

    override fun getIdentifier(): String = "isdm"

    override fun getAuthor(): String = plugin.description.authors.joinToString(", ").ifBlank { "unknown" }

    override fun getVersion(): String = plugin.description.version

    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        player ?: return ""

        val key = params.trim().lowercase()
        if (key.isBlank()) return ""
        if (key == "on_island") return (IslandResolver.resolve(player) != null).toString()

        val islandId = IslandResolver.resolve(player) ?: return ""
        val snapshot = getSnapshot(islandId)

        if (key == "date") return snapshot.date
        if (key == "point") return snapshot.point.toString()
        if (key == "island_id") return islandId
        if (key.startsWith("recent_")) return resolveRecentField(player, key.removePrefix("recent_"))
        if (key == "missions") return difficultyOrder.joinToString(" | ") { diff ->
            val pair = snapshot.entries[diff]
            buildSummary(pair?.first, pair?.second)
        }

        val numbered = Regex("^mission_(\\d+)_(.+)$").matchEntire(key)
        if (numbered != null) {
            val index = numbered.groupValues[1].toIntOrNull() ?: return ""
            val field = numbered.groupValues[2]
            val diff = difficultyOrder.getOrNull(index - 1) ?: return ""
            return resolveField(snapshot, diff, field)
        }

        val parts = key.split("_")
        if (parts.size >= 3) {
            val field = parts.last()
            val difficultyKey = parts.dropLast(1).joinToString("_")
            val difficulty = MissionDifficulty.fromKey(difficultyKey) ?: return ""
            return resolveField(snapshot, difficulty, field)
        }

        return ""
    }

    private fun resolveRecentField(player: Player, field: String): String {
        val recent = missionService.getRecentMission(player.uniqueId) ?: return ""
        val mission = recent.mission
        val goal = recent.goal.coerceAtLeast(0)
        val current = recent.current.coerceAtLeast(0)
        val percent = if (goal <= 0) 0 else ((current * 100.0) / goal).toInt().coerceIn(0, 100)
        val remaining = (goal - current).coerceAtLeast(0)
        return when (field) {
            "summary" -> "${mission.name} (${current}/${goal})"
            "id" -> mission.id
            "name" -> mission.name
            "description" -> mission.description
            "difficulty" -> recent.difficulty.key
            "type" -> mission.type.name
            "target" -> formatTarget(mission)
            "goal", "amount" -> goal.toString()
            "current", "progress" -> current.toString()
            "reward" -> mission.reward.toString()
            "percent" -> percent.toString()
            "remaining" -> remaining.toString()
            "status" -> if (recent.completed) "done" else if (current > 0) "progress" else "none"
            "completed" -> recent.completed.toString()
            "island_id" -> recent.islandId
            else -> ""
        }
    }

    private fun resolveField(snapshot: Snapshot, difficulty: MissionDifficulty, field: String): String {
        val pair = snapshot.entries[difficulty]
        val assigned = pair?.first
        val mission = pair?.second
        if (assigned == null || mission == null) return ""

        val current = assigned.progress
        val goal = mission.amount.coerceAtLeast(0)
        val reward = mission.reward
        val percent = if (goal <= 0) 0 else ((current * 100.0) / goal).toInt().coerceIn(0, 100)
        val remaining = (goal - current).coerceAtLeast(0)

        return when (field) {
            "id" -> mission.id
            "name" -> mission.name
            "description" -> mission.description
            "type" -> mission.type.name
            "target" -> formatTarget(mission)
            "goal", "amount" -> goal.toString()
            "current", "progress" -> current.toString()
            "reward" -> reward.toString()
            "percent" -> percent.toString()
            "remaining" -> remaining.toString()
            "status" -> statusKey(assigned)
            "completed" -> assigned.completed.toString()
            "summary" -> buildSummary(assigned, mission)
            else -> ""
        }
    }

    private fun formatTarget(mission: Mission): String {
        return when (mission.target.type) {
            TargetType.ANY -> "ANY"
            TargetType.MATERIAL -> "MATERIAL:${mission.target.value ?: ""}"
            TargetType.ENTITY -> "ENTITY:${mission.target.value ?: ""}"
        }
    }

    private fun statusKey(assigned: AssignedMission): String {
        return when {
            assigned.completed -> "done"
            assigned.progress > 0 -> "progress"
            else -> "none"
        }
    }

    private fun buildSummary(assigned: AssignedMission?, mission: Mission?): String {
        if (assigned == null || mission == null) return ""
        return "${mission.name} (${assigned.progress}/${mission.amount})"
    }

    private fun getSnapshot(islandId: String): Snapshot {
        val now = System.currentTimeMillis()
        val cached = snapshotCache[islandId]
        if (cached != null && now - cached.createdAt <= cacheTtlMs) {
            return cached.snapshot
        }

        val assignments = missionService.getTodayAssignments(islandId)
        val entries = mutableMapOf<MissionDifficulty, Pair<AssignedMission, Mission?>>()
        for ((difficulty, assigned) in assignments) {
            entries[difficulty] = assigned to missionService.getMissionById(assigned.missionId)
        }

        val snapshot = Snapshot(
            date = LocalDate.now(ZoneId.systemDefault()).toString(),
            point = store.getMissionPoint(islandId),
            entries = entries
        )
        snapshotCache[islandId] = CachedSnapshot(now, snapshot)
        return snapshot
    }

    private data class Snapshot(
        val date: String,
        val point: Int,
        val entries: Map<MissionDifficulty, Pair<AssignedMission, Mission?>>
    )

    private data class CachedSnapshot(
        val createdAt: Long,
        val snapshot: Snapshot
    )
}
