package addon.dailymission.logic

import addon.dailymission.mission.Mission

data class ProgressUpdate(
    val mission: Mission,
    val current: Int,
    val goal: Int,
    val completedNow: Boolean
)
