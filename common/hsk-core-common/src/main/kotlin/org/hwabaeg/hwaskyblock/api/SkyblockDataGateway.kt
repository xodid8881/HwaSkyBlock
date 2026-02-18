package org.hwabaeg.hwaskyblock.api

import org.bukkit.entity.Player

interface SkyblockDataGateway {
    fun getPlayerPossessionCount(player: Player): Int
    fun getSkyblockLeader(islandNumber: Int): String?
    fun getSkyblockSize(islandNumber: Int): Int
    fun setSkyblockSize(islandNumber: Int, size: Int)
    fun getSkyblockPoint(islandNumber: Int): Int
    fun setSkyblockPoint(islandNumber: Int, point: Int)
}
