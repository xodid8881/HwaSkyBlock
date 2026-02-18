package org.hwabaeg.hwaskyblock.platform

interface InventoryViewResolver {
    fun title(view: Any?): String
    fun topHolder(view: Any?): Any?
}
