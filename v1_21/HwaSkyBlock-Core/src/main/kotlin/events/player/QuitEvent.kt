package org.hwabaeg.hwaskyblock.events.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class QuitEvent : Listener {

    companion object {
        val LAST_WORLD: HashMap<UUID, String> = HashMap()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val worldName = player.world.worldFolder.name

        if (worldName.startsWith("HwaSkyBlock.")) {
            LAST_WORLD[player.uniqueId] = worldName
        }
    }
}
