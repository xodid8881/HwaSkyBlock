package org.hwabaeg.hwaskyblock.api.customblock.handlers

import org.bukkit.Location
import org.bukkit.block.Block
import org.hwabaeg.hwaskyblock.api.customblock.CustomBlockHandler

class NexoHandler : CustomBlockHandler {
    override fun isCustomBlock(block: Block): Boolean {
        return try {
            val clazz = Class.forName("com.nexomc.nexo.api.NexoBlocks")
            val method = clazz.getMethod("isCustomBlock", Block::class.java)
            method.invoke(null, block) as? Boolean ?: false
        } catch (_: Exception) { false }
    }

    override fun getCustomBlock(block: Block): Any? {
        return try {
            val clazz = Class.forName("com.nexomc.nexo.api.NexoBlocks")
            val method = clazz.getMethod("customBlockMechanic", Block::class.java)
            method.invoke(null, block)
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockId(block: Block): String? {
        return try {
            val mechanic = getCustomBlock(block) ?: return null
            val method = mechanic.javaClass.getMethod("getItemID")
            method.invoke(mechanic) as? String
        } catch (_: Exception) { null }
    }

    override fun getCustomBlockDisplayName(block: Block): String? {
        return try {
            val mechanic = getCustomBlock(block) ?: return null
            val item = mechanic.javaClass.getMethod("getItemStack").invoke(mechanic)
            val meta = item?.javaClass?.getMethod("getItemMeta")?.invoke(item)
            meta?.javaClass?.getMethod("getDisplayName")?.invoke(meta) as? String
        } catch (_: Exception) { null }
    }

    override fun placeCustomBlock(location: Location, id: String): Boolean {
        return try {
            val clazz = Class.forName("com.nexomc.nexo.api.NexoBlocks")
            val method = clazz.getMethod("place", String::class.java, Location::class.java)
            method.invoke(null, id, location)
            true
        } catch (_: Exception) { false }
    }

    override fun removeCustomBlock(block: Block): Boolean {
        return try {
            val clazz = Class.forName("com.nexomc.nexo.api.NexoBlocks")
            val method = clazz.getMethod("remove", Location::class.java)
            method.invoke(null, block.location)
            true
        } catch (_: Exception) { false }
    }
}
