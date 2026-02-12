package org.hwabaeg.hwaskyblock.addon.custompoint.point

import org.hwabaeg.hwaskyblock.database.DatabaseManager

object IslandPointService {

    fun addPoint(
        islandId: String,
        point: Int
    ) {
        if (point <= 0) return

        val current = getCurrentPoint(islandId)
        val total = current + point

        DatabaseManager.setSkyBlockData(
            islandId,
            total,
            "setSkyBlockPoint"
        )
    }

    private fun getCurrentPoint(islandId: String): Int {
        return when (val raw = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockPoint")) {
            is Number -> raw.toInt()
            is String -> raw.toIntOrNull() ?: 0
            else -> 0
        }
    }
}
