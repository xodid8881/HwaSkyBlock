package addon.dailymission.mission

enum class TargetType {
    ANY,
    MATERIAL,
    ENTITY
}

data class MissionTarget(
    val type: TargetType,
    val value: String? = null
) {
    fun matchesMaterial(materialKey: String): Boolean {
        if (type == TargetType.ANY) return true
        return type == TargetType.MATERIAL && value != null &&
            value.equals(materialKey, ignoreCase = true)
    }

    fun matchesEntity(entityKey: String): Boolean {
        if (type == TargetType.ANY) return true
        return type == TargetType.ENTITY && value != null &&
            value.equals(entityKey, ignoreCase = true)
    }

    companion object {
        fun parse(raw: String?): MissionTarget {
            if (raw.isNullOrBlank() || raw.equals("ANY", ignoreCase = true)) {
                return MissionTarget(TargetType.ANY, null)
            }
            val parts = raw.split(":", limit = 2)
            if (parts.size != 2) return MissionTarget(TargetType.ANY, null)

            val type = when (parts[0].uppercase()) {
                "MATERIAL" -> TargetType.MATERIAL
                "ENTITY" -> TargetType.ENTITY
                else -> TargetType.ANY
            }
            val value = parts[1].trim()
            return MissionTarget(type, value)
        }
    }
}
