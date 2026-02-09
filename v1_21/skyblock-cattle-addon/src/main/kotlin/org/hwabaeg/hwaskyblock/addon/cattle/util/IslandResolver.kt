package org.hwabaeg.hwaskyblock.addon.cattle.util

import org.bukkit.World
import org.hwabaeg.hwaskyblock.database.DatabaseManager

object IslandResolver {
    fun resolve(world: World): String? {
        val worldName = world.name
        if (!worldName.startsWith("HwaSkyBlock.")) return null

        val islandId = worldName.removePrefix("HwaSkyBlock.")
        if (islandId.isBlank()) return null

        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return null
        if (leader.toString().isBlank()) return null
        return islandId
    }
}
