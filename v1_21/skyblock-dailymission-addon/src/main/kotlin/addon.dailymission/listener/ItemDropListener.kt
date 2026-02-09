package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ItemDropListener(
    private val antiAbuse: AntiAbuseGuard
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onDrop(e: PlayerDropItemEvent) {
        antiAbuse.recordDrop(e.itemDrop.uniqueId, e.player.uniqueId)
    }
}
