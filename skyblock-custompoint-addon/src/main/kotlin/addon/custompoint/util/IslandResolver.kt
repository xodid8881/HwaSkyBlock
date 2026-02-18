package org.hwabaeg.hwaskyblock.addon.custompoint.util

import org.bukkit.World
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.world.IslandWorlds

object IslandResolver {

    fun resolve(player: Player): String? {
        return resolve(player.world)
    }

    fun resolve(world: World): String? {
        val islandId = IslandWorlds.extractIslandId(world.name) ?: return null

        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return null

        return islandId
    }
}
