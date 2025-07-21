package org.hwabeag.hwaskyblock.schedules

import org.bukkit.Bukkit
import java.io.File

class UnloadWorldTask : Runnable {
    override fun run() {
        for (world in Bukkit.getServer().worlds) {
            val worldName = world.name
            if (worldName.contains("HwaSkyBlock.")) {
                val worldDir = File(Bukkit.getServer().worldContainer, worldName)

                if (!worldDir.exists()) {
                    Bukkit.getLogger().warning("There is no world directory for " + worldName)
                    continue
                }

                if (world.players.isEmpty()) {
                    try {
                        Bukkit.getServer().unloadWorld(world, true)
                        Bukkit.getLogger().info("Successfully unloaded the world: $worldName")
                    } catch (e: Exception) {
                        Bukkit.getLogger().severe("World unload failed: $worldName")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}