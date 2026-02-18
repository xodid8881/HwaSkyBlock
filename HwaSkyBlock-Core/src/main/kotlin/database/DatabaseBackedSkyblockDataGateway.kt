package org.hwabaeg.hwaskyblock.database

import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.api.SkyblockDataGateway

class DatabaseBackedSkyblockDataGateway : SkyblockDataGateway {

    override fun getPlayerPossessionCount(player: Player): Int {
        val name = player.name
        return DatabaseManager.getUserData(
            "$name.skyblock.possession_count",
            player,
            "getPlayerPossessionCount"
        ) as? Int ?: 0
    }

    override fun getSkyblockLeader(islandNumber: Int): String? {
        return DatabaseManager.getSkyBlockData(
            islandNumber.toString(),
            "getSkyBlockLeader"
        ) as? String
    }

    override fun getSkyblockSize(islandNumber: Int): Int {
        return DatabaseManager.getSkyBlockData(
            islandNumber.toString(),
            "getSkyBlockSize"
        ) as? Int ?: 0
    }

    override fun setSkyblockSize(islandNumber: Int, size: Int) {
        DatabaseManager.setSkyBlockData(
            islandNumber.toString(),
            size,
            "setSkyBlockSize"
        )
    }

    override fun getSkyblockPoint(islandNumber: Int): Int {
        return DatabaseManager.getSkyBlockData(
            islandNumber.toString(),
            "getSkyBlockPoint"
        ) as? Int ?: 0
    }

    override fun setSkyblockPoint(islandNumber: Int, point: Int) {
        DatabaseManager.setSkyBlockData(
            islandNumber.toString(),
            point,
            "setSkyBlockPoint"
        )
    }
}
