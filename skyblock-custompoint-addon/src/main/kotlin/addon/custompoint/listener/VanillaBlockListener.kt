package org.hwabaeg.hwaskyblock.addon.custompoint.listener

import org.hwabaeg.hwaskyblock.addon.custompoint.config.BlockPointConfig
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

class VanillaBlockListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        handle(
            event.player,
            event.block,
            event
        )
    }

    private fun handle(
        player: Player,
        block: Block,
        event: Cancellable
    ) {

        // 1️⃣ Skyblock 섬 ID 추출
        val islandId = PointValidator.extractIslandId(block.world.name) ?: return

        // 2️⃣ 이벤트 검증
        if (event.isCancelled) return

        // 3️⃣ 중복 지급 방지
        if (!PointDuplicateGuard.canProcess(player, block)) return

        // 4️⃣ block-point.yml 에 정의된 바닐라 블록만
        val point = BlockPointConfig.getPoint(block.type) ?: return
        if (point <= 0) return

        // 5️⃣ 포인트 지급
        IslandPointService.addPoint(
            islandId,
            point
        )
    }
}
