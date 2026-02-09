package addon.dailymission.util

import org.bukkit.World
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.database.DatabaseManager

object IslandResolver {
    fun resolve(player: Player): String? = resolve(player.world)

    fun resolve(world: World): String? {
        val name = world.name
        if (!name.startsWith("HwaSkyBlock.")) return null

        val islandId = name.removePrefix("HwaSkyBlock.")
        if (islandId.isBlank()) return null

        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return null
        return islandId
    }
}
