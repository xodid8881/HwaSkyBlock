package addon.dailymission.mission

data class Mission(
    val id: String,
    val name: String,
    val description: String,
    val difficulty: MissionDifficulty,
    val type: MissionType,
    val target: MissionTarget,
    val amount: Int,
    val reward: Int,
    val enabled: Boolean
)
