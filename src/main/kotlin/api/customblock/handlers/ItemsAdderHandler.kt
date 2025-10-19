package org.hwabaeg.hwaskyblock.api.customblock.handlers

import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Location
import org.bukkit.block.Block
import org.hwabaeg.hwaskyblock.api.customblock.CustomBlockHandler

class ItemsAdderHandler : CustomBlockHandler {
    override fun isCustomBlock(block: Block): Boolean {
        return try {
            CustomBlock.byAlreadyPlaced(block) != null
        } catch (_: Exception) { false }
    }

    override fun getCustomBlock(block: Block): Any? {
        return try {
            CustomBlock.byAlreadyPlaced(block)
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockId(block: Block): String? {
        return try {
            CustomBlock.byAlreadyPlaced(block)?.namespacedID
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockDisplayName(block: Block): String? {
        return try {
            val cb = CustomBlock.byAlreadyPlaced(block)
            cb?.itemStack?.itemMeta?.displayName
        } catch (_: Exception) { null }
    }

    override fun placeCustomBlock(location: Location, id: String): Boolean {
        return try {
            val cb = CustomBlock.getInstance(id)
            if (cb != null) {
                cb.place(location)
                true
            } else false
        } catch (_: Exception) { false }
    }

    override fun removeCustomBlock(block: Block): Boolean {
        return try {
            CustomBlock.remove(block as Location?)
            true
        } catch (_: Exception) { false }
    }
}
