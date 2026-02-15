package addon.dailymission.mission

enum class MissionDifficulty(val key: String) {
    VERY_EASY("very_easy"),
    EASY("easy"),
    NORMAL("normal"),
    HARD("hard"),
    VERY_HARD("very_hard");

    companion object {
        fun fromKey(key: String): MissionDifficulty? {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) }
        }
    }
}
