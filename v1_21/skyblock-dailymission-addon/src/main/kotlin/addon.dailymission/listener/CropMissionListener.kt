package addon.dailymission.listener

import addon.dailymission.logic.MissionProgressProcessor
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class CropMissionListener(
    private val processor: MissionProgressProcessor
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        val block = e.clickedBlock ?: return
        if (!isMatureCrop(block)) return
        processor.onCropHarvest(e.player, block.type)
    }

    private fun isMatureCrop(block: Block): Boolean {
        val data = block.blockData
        if (data !is Ageable) return false
        return data.age >= data.maximumAge
    }
}
