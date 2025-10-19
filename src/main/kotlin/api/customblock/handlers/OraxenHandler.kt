package org.hwabaeg.hwaskyblock.api.customblock.handlers

import io.th0rgal.oraxen.api.OraxenBlocks
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.hwabaeg.hwaskyblock.api.customblock.CustomBlockHandler

class OraxenHandler : CustomBlockHandler {
    override fun isCustomBlock(block: Block): Boolean {
        return try {
            OraxenBlocks.isOraxenBlock(block)
        } catch (_: Exception) { false }
    }

    override fun getCustomBlock(block: Block): Any? {
        return try {
            OraxenBlocks.getBlockMechanic(block)
                ?: OraxenBlocks.getOraxenBlock(block.blockData)
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockId(block: Block): String? {
        return try {
            OraxenBlocks.getBlockMechanic(block)?.itemID
                ?: OraxenBlocks.getOraxenBlock(block.blockData)?.itemID
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockDisplayName(block: Block): String? {
        return try {
            val mechanic = OraxenBlocks.getBlockMechanic(block)
                ?: OraxenBlocks.getOraxenBlock(block.blockData)
            val item: ItemStack? = mechanic?.let {
                when {
                    it.javaClass.methods.any { m -> m.name == "getItem" } ->
                        it.javaClass.getMethod("getItem").invoke(it) as? ItemStack
                    it.javaClass.methods.any { m -> m.name == "getItemStack" } ->
                        it.javaClass.getMethod("getItemStack").invoke(it) as? ItemStack
                    else -> null
                }
            }
            item?.itemMeta?.displayName
        } catch (_: Exception) { null }
    }

    override fun placeCustomBlock(location: Location, id: String): Boolean {
        return try {
            OraxenBlocks.place(id, location)
            true
        } catch (_: Exception) { false }
    }

    override fun removeCustomBlock(block: Block): Boolean {
        return try {
            block.type = Material.AIR
            true
        } catch (_: Exception) { false }
    }
}
