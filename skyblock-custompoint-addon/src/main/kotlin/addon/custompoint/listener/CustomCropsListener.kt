package org.hwabaeg.hwaskyblock.addon.custompoint.listener

import net.momirealms.customcrops.api.event.CropBreakEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.hwabaeg.hwaskyblock.addon.custompoint.config.AddonConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.config.BlockPointConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.point.IslandPointService
import org.hwabaeg.hwaskyblock.addon.custompoint.point.PointValidator

class CustomCropsListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCropBreak(event: CropBreakEvent) {

        if (!AddonConfig.enabled || !AddonConfig.enableCustomCrops) return

        val player = event.entityBreaker() as? Player ?: return

        val location = event.location()
        val world = location.world ?: return

        val islandId = PointValidator.extractIslandId(world.name) ?: return

        // ⚠️ CustomCrops에서는 중복 가드 사용 안 함

        val cropId = event.cropConfig().id()
        val stageId = event.cropStageItemID()

        val blockId = "customcrops:$cropId:$stageId"

        val point = BlockPointConfig.getPoint(blockId) ?: return
        if (point <= 0) return

        IslandPointService.addPoint(islandId, point)
    }
}