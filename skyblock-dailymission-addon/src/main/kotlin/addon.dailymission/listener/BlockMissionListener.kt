package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import addon.dailymission.logic.MissionProgressProcessor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockMissionListener(
    private val processor: MissionProgressProcessor,
    private val antiAbuse: AntiAbuseGuard? = null
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBreak(e: BlockBreakEvent) {
        if (antiAbuse?.isPlaced(e.block.location) == true) return
        processor.onBlockBreak(e.player, e.block.type)
        antiAbuse?.removePlaced(e.block.location)
    }
}
