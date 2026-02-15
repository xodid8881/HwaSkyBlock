package org.hwabaeg.hwaskyblock.addon.custompoint.util

import org.bukkit.World
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.database.DatabaseManager

object IslandResolver {

    fun resolve(player: Player): String? {
        return resolve(player.world)
    }

    fun resolve(world: World): String? {
        val worldName = world.name

        if (!worldName.startsWith("HwaSkyBlock.")) {
            return null
        }

        val islandId = worldName.removePrefix("HwaSkyBlock.")
        if (islandId.isBlank()) return null

        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return null

        return islandId
    }
}
