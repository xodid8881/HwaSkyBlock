package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.entity.Player

class ItemPickupListener(
    private val antiAbuse: AntiAbuseGuard
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPickup(e: EntityPickupItemEvent) {
        val player = e.entity as? Player ?: return
        val itemId = e.item.uniqueId
        if (antiAbuse.isReacquire(itemId, player.uniqueId)) {
            e.isCancelled = true
        }
    }
}
