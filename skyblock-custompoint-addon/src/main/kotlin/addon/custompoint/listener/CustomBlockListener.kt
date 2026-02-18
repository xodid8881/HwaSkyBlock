package org.hwabaeg.hwaskyblock.addon.custompoint.listener

import org.hwabaeg.hwaskyblock.addon.custompoint.config.AddonConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.config.BlockPointConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.hook.HookManager
import org.hwabaeg.hwaskyblock.addon.custompoint.point.IslandPointService
import org.hwabaeg.hwaskyblock.addon.custompoint.point.PointDuplicateGuard
import org.hwabaeg.hwaskyblock.addon.custompoint.point.PointValidator
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerHarvestBlockEvent

class CustomBlockListener(
    private val hookManager: HookManager
) : Listener {

    /* ===============================
       Block Break (Vanilla + CustomBlock)
       =============================== */

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!AddonConfig.enabled) return
        if (!AddonConfig.enableVanillaBlock && !AddonConfig.enableCustomBlock) return

        handle(event.player, event.block, event)
    }

    /* ===============================
       Crop Harvest (Vanilla Crop ONLY)
       =============================== */

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onCropHarvest(event: PlayerHarvestBlockEvent) {
        if (!AddonConfig.enabled) return
        if (!AddonConfig.enableCustomBlock) return   // ❗ enableCustomCrops ❌

        handle(event.player, event.harvestedBlock, event)
    }

    /* ===============================
       Core Logic
       =============================== */

    private fun handle(
        player: Player,
        block: Block,
        event: Cancellable
    ) {

        // 1️⃣ Skyblock 섬 ID
        val islandId = PointValidator.extractIslandId(block.world.name) ?: return

        // 2️⃣ 이벤트 + 권한 검증
        if (!PointValidator.canGivePoint(player, islandId, event)) return

        // 3️⃣ Block ID 해석 (Vanilla / CustomBlock)
        val blockId = hookManager.resolveBlockId(block)

        // 4️⃣ 중복 지급 방지 (Vanilla만)
        if (blockId.startsWith("minecraft:")) {
            if (!PointDuplicateGuard.canProcess(player, block)) return
        }

        // 5️⃣ 포인트 조회
        val point = when {
            blockId.startsWith("minecraft:") ->
                BlockPointConfig.getPoint(block.type)

            else ->
                BlockPointConfig.getPoint(blockId)
        } ?: return

        if (point <= 0) return

        // 6️⃣ 포인트 지급
        IslandPointService.addPoint(islandId, point)
    }
}
