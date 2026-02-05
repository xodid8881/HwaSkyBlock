package org.hwabaeg.hwaskyblock.events.block

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager

class PhysicsEvent : Listener {
    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        val world = event.getBlock().world
        val world_name = world.worldFolder.name
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val block_to_id = number[1]
            val water_physics = DatabaseManager.getSkyBlockData(
                block_to_id.toString(),
                "isSkyBlockWaterPhysics"
            ) as? Boolean ?: true
            val lava_physics = DatabaseManager.getSkyBlockData(
                block_to_id.toString(),
                "isSkyBlockLavaPhysics"
            ) as? Boolean ?: true
            if (!water_physics) {
                if (event.getBlock().type == Material.WATER) {
                    event.isCancelled = true
                }
            }
            if (!lava_physics) {
                if (event.getBlock().type == Material.LAVA) {
                    event.isCancelled = true
                }
            }
        }
    }
}