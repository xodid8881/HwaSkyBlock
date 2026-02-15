package org.hwabaeg.hwaskyblock.addon.custompoint.point

import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.*

object PointDuplicateGuard {

    private val recent = HashMap<String, Long>()
    private const val EXPIRE_MS = 100L

    fun canProcess(player: Player, block: Block): Boolean {
        val key = "${player.uniqueId}:${block.world.name}:${block.x}:${block.y}:${block.z}"
        val now = System.currentTimeMillis()

        val last = recent[key]
        if (last != null && now - last < EXPIRE_MS) {
            return false
        }

        recent[key] = now
        return true
    }
}
