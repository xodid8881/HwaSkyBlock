package org.hwabaeg.hwaskyblock.compat

import org.bukkit.inventory.InventoryView

fun inventoryViewTitle(view: Any?): String {
    val invView = view as? InventoryView ?: return ""
    return runCatching { invView.title }.getOrNull().orEmpty()
}

fun inventoryViewTopHolder(view: Any?): Any? {
    val invView = view as? InventoryView ?: return null
    return runCatching { invView.topInventory.holder }.getOrNull()
}
