package org.hwabaeg.hwaskyblock.schedules

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.hwabaeg.hwaskyblock.database.DatabaseManager

class UnloadBorderTask : BukkitRunnable() {

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val world = player.world
            val world_name = world.worldFolder.name
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                val size = DatabaseManager
                    .getSkyBlockData(id.toString(), "getSkyBlockSize")
                    ?.toString()?.toIntOrNull() ?: 0
                setWorldBorder(world.name, size)
            }
        }
    }

    fun setWorldBorder(worldName: String?, size: Int) {
        val world = worldName?.let { Bukkit.getWorld(it) }
        if (world != null) {
            val border = world.worldBorder
            border.center = world.spawnLocation
            border.size = size.toDouble()
            border.warningDistance = 10
            border.damageAmount = 0.1
        }
    }
}