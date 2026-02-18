package addon.dailymission.listener

import addon.dailymission.logic.MissionProgressProcessor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.FurnaceExtractEvent
import org.bukkit.inventory.ItemStack

class SmeltMissionListener(
    private val processor: MissionProgressProcessor
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onExtract(e: FurnaceExtractEvent) {
        processor.onSmelt(e.player, ItemStack(e.itemType), e.itemAmount)
    }
}
