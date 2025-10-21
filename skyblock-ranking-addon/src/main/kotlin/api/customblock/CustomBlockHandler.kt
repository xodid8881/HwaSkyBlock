package org.hwabaeg.hwaskyblock.api.customblock

import org.bukkit.Location
import org.bukkit.block.Block

interface CustomBlockHandler {
    fun isCustomBlock(block: Block): Boolean
    fun getCustomBlock(block: Block): Any?
    fun getCustomBlockId(block: Block): String?
    fun getCustomBlockDisplayName(block: Block): String?
    fun placeCustomBlock(location: Location, id: String): Boolean
    fun removeCustomBlock(block: Block): Boolean
}
