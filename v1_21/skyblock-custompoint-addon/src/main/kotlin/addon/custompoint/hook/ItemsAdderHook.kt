package org.hwabaeg.hwaskyblock.addon.custompoint.hook

import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.block.Block
import java.util.logging.Logger

class ItemsAdderHook(
    private val log: Logger
) {

    fun hook(): Boolean {
        return try {
            Class.forName("dev.lone.itemsadder.api.CustomBlock")
            log.info("[CustomPointAddon] ItemsAdder detected")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getBlockId(block: Block): String? {
        val customBlock = CustomBlock.byAlreadyPlaced(block) ?: return null
        return customBlock.namespacedID
    }
}
