package org.hwabaeg.hwaskyblock.api

import org.bukkit.entity.Player

class HwaSkyBlockAPIImpl(
    private val gateway: SkyblockDataGateway,
) : HwaSkyBlockAPI {

    override fun hasIsland(player: Player): Boolean {
        return gateway.getPlayerPossessionCount(player) != 0
    }

    override fun hasOwner(player: Player, island_number: Int): Boolean {
        return gateway.getSkyblockLeader(island_number) == player.name
    }

    override fun upgradeIsland(player: Player, island_number: Int, plus_size: Int) {
        val current = gateway.getSkyblockSize(island_number)
        gateway.setSkyblockSize(island_number, current + plus_size)
    }

    override fun addIslandPoint(player: Player, island_number: Int, point: Int) {
        val current = gateway.getSkyblockPoint(island_number)
        gateway.setSkyblockPoint(island_number, current + point)
    }
}
