package org.hwabaeg.hwaskyblock.api.customblock

import org.hwabaeg.hwaskyblock.api.customblock.handlers.ItemsAdderHandler
import org.hwabaeg.hwaskyblock.api.customblock.handlers.NexoHandler
import org.hwabaeg.hwaskyblock.api.customblock.handlers.OraxenHandler

object CustomBlockManager {
    private var handler: CustomBlockHandler? = null

    fun init() {
        val pm = org.bukkit.Bukkit.getPluginManager()
        handler = when {
            pm.getPlugin("ItemsAdder") != null -> ItemsAdderHandler()
            pm.getPlugin("Oraxen") != null -> OraxenHandler()
            pm.getPlugin("Nexo") != null -> NexoHandler()
            else -> null
        }
    }

    fun getHandler(): CustomBlockHandler? = handler
}

