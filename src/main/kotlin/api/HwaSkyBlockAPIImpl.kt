package org.hwabeag.hwaskyblock.api

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager

class HwaSkyBlockAPIImpl : HwaSkyBlockAPI {

    override fun hasIsland(player: Player): Boolean {
        val name = player.name
        val count = DatabaseManager.getUserData("$name.skyblock.possession_count", player, "getPlayerPossessionCount") as? Int ?: 0
        return count != 0
    }

    override fun hasOwner(player: Player, island_number: Int): Boolean {
        val name = player.name
        val leader = DatabaseManager.getSkyBlockData(island_number.toString(), "$island_number.leader", "getSkyBlockLeader") as? String
        return leader == name
    }

    override fun upgradeIsland(player: Player, island_number: Int, plus_size: Int) {
        val id = island_number.toString()
        val current = DatabaseManager.getSkyBlockData(id, "$id.size", "getSkyBlockSize") as? Int ?: 0
        DatabaseManager.setSkyBlockData(id, "$id.size", current + plus_size, "setSkyBlockSize")
    }
}