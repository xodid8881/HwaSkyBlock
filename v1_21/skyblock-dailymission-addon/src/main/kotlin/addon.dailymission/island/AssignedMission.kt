package addon.dailymission.island

import addon.dailymission.mission.MissionDifficulty

data class AssignedMission(
    val difficulty: MissionDifficulty,
    val missionId: String,
    val progress: Int,
    val completed: Boolean
)
