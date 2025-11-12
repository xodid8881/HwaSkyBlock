package org.hwabaeg.hwaskyblock.api

import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.database.DatabaseManager

class HwaSkyBlockAPIImpl : HwaSkyBlockAPI {

    override fun hasIsland(player: Player): Boolean {
        val name = player.name
        val count =
            DatabaseManager.getUserData("$name.skyblock.possession_count", player, "getPlayerPossessionCount") as? Int
                ?: 0
        return count != 0
    }

    override fun hasOwner(player: Player, island_number: Int): Boolean {
        val name = player.name
        val leader = DatabaseManager.getSkyBlockData(
            island_number.toString(),
            "getSkyBlockLeader"
        ) as? String
        return leader == name
    }

    override fun upgradeIsland(player: Player, island_number: Int, plus_size: Int) {
        val id = island_number.toString()
        val current = DatabaseManager.getSkyBlockData(id, "getSkyBlockSize") as? Int ?: 0
        DatabaseManager.setSkyBlockData(id, current + plus_size, "setSkyBlockSize")
    }

    override fun addIslandPoint(player: Player, island_number: Int, point: Int) {
        val id = island_number.toString()
        val current = DatabaseManager.getSkyBlockData(id, "getSkyBlockPoint") as? Int ?: 0
        DatabaseManager.setSkyBlockData(id, current + point, "setSkyBlockPoint")
    }
}