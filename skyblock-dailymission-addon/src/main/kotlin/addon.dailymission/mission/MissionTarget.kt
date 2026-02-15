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
        if (type != TargetType.MATERIAL || value == null) return false

        val target = value.uppercase()
        val material = materialKey.uppercase()
        if (target == "ORE" || target == "ANY_ORE" || target == "ALL_ORE") {
            return material in ORE_MATERIALS
        }

        return target == material
    }

    fun matchesEntity(entityKey: String): Boolean {
        if (type == TargetType.ANY) return true
        return type == TargetType.ENTITY && value != null &&
            value.equals(entityKey, ignoreCase = true)
    }

    companion object {
        private val ORE_MATERIALS = setOf(
            "COAL_ORE",
            "DEEPSLATE_COAL_ORE",
            "IRON_ORE",
            "DEEPSLATE_IRON_ORE",
            "COPPER_ORE",
            "DEEPSLATE_COPPER_ORE",
            "GOLD_ORE",
            "DEEPSLATE_GOLD_ORE",
            "REDSTONE_ORE",
            "DEEPSLATE_REDSTONE_ORE",
            "LAPIS_ORE",
            "DEEPSLATE_LAPIS_ORE",
            "DIAMOND_ORE",
            "DEEPSLATE_DIAMOND_ORE",
            "EMERALD_ORE",
            "DEEPSLATE_EMERALD_ORE",
            "NETHER_GOLD_ORE",
            "NETHER_QUARTZ_ORE",
            "ANCIENT_DEBRIS"
        )

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
