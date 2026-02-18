package org.hwabaeg.hwaskyblock.world

object IslandWorlds {
    @Volatile
    private var currentPrefix: String = "HwaSkyBlock."

    fun configure(prefix: String?) {
        currentPrefix = normalizePrefix(prefix)
    }

    fun prefix(): String = currentPrefix

    fun worldName(islandId: String): String = "${currentPrefix}${islandId}"

    fun isIslandWorldName(worldName: String): Boolean {
        return extractIslandId(worldName) != null
    }

    fun extractIslandId(worldName: String): String? {
        if (!worldName.startsWith(currentPrefix)) return null
        val raw = worldName.removePrefix(currentPrefix)
        if (raw.isBlank()) return null
        return raw.substringBefore("_").takeIf { it.isNotBlank() }
    }

    private fun normalizePrefix(prefix: String?): String {
        val trimmed = prefix?.trim().orEmpty()
        if (trimmed.isBlank()) return "HwaSkyBlock."
        return if (trimmed.endsWith(".")) trimmed else "$trimmed."
    }
}
