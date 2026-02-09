package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener(
    private val antiAbuse: AntiAbuseGuard
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlace(e: BlockPlaceEvent) {
        antiAbuse.recordPlaced(e.block.location)
    }
}
