package org.hwabaeg.hwaskyblock.addon.custompoint.point

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.hwabaeg.hwaskyblock.database.DatabaseManager

object PointValidator {

    fun canGivePoint(
        player: Player,
        islandId: String,
        event: Cancellable
    ): Boolean {

        if (event.isCancelled) {
            return false
        }

        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return false

        val playerName = player.name

        if (leader.toString() != playerName) {

            val shareList = DatabaseManager.getShareDataList(islandId)
            if (!shareList.contains(playerName)) {
                return false
            }

            val canJoin = DatabaseManager.getShareData(
                islandId,
                playerName,
                "isUseJoin"
            ) as? Boolean ?: false

            if (!canJoin) {
                return false
            }
        }

        return true
    }

    fun extractIslandId(worldName: String): String? {

        if (!worldName.startsWith("HwaSkyBlock.")) return null

        return worldName.removePrefix("HwaSkyBlock.").takeIf { it.isNotBlank() }
    }
}
