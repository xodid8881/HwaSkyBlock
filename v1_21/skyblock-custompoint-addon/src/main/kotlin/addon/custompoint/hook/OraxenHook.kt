package org.hwabaeg.hwaskyblock.addon.custompoint.hook

import org.bukkit.block.Block
import java.util.logging.Logger

class OraxenHook(
    private val log: Logger
) {

    private var enabled = false

    fun hook(): Boolean {
        enabled = try {
            Class.forName("io.th0rgal.oraxen.api.OraxenBlocks")
            log.info("[CustomPointAddon] Oraxen detected")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
        return enabled
    }

    fun isEnabled(): Boolean = enabled

    fun getBlockId(block: Block): String? {
        if (!enabled) return null

        return try {
            val blocksClass = Class.forName("io.th0rgal.oraxen.api.OraxenBlocks")

            val isOraxenBlockMethod =
                blocksClass.getMethod("isOraxenBlock", Block::class.java)

            val isOraxen = isOraxenBlockMethod.invoke(null, block) as Boolean
            if (!isOraxen) return null

            val getOraxenBlockMethod =
                blocksClass.getMethod("getOraxenBlock", Block::class.java)

            val oraxenBlock = getOraxenBlockMethod.invoke(null, block) ?: return null

            val itemIdMethod = oraxenBlock.javaClass.getMethod("getItemID")
            itemIdMethod.invoke(oraxenBlock) as? String

        } catch (e: Exception) {
            log.warning("[CustomPointAddon] Failed to read Oraxen block: ${e.message}")
            null
        }
    }
}
