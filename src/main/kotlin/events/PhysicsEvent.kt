package org.hwabeag.hwaskyblock.events

import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPhysicsEvent
import org.hwabeag.hwaskyblock.config.ConfigManager

class PhysicsEvent : Listener {
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!

    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        val world = event.getBlock().world
        val world_name = world.worldFolder.getName()
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val block_to_id = number[1]
            val water_physics = SkyBlockConfig.getBoolean("$block_to_id.setting.water_physics")
            val lava_physics = SkyBlockConfig.getBoolean("$block_to_id.setting.lava_physics")
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