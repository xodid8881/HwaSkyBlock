package addon.dailymission.mission

enum class MissionType {
    BLOCK_BREAK,
    CROP_HARVEST,
    ENTITY_KILL,
    CRAFT,
    SMELT,
    SPECIAL;

    companion object {
        fun fromKey(key: String): MissionType? {
            return entries.firstOrNull { it.name.equals(key, ignoreCase = true) }
        }
    }
}
