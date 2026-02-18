package org.hwabaeg.hwaskyblock.platform.v1_18

import org.hwabaeg.hwaskyblock.platform.InventoryViewResolver

class BukkitInventoryViewResolver : InventoryViewResolver {
    override fun title(view: Any?): String {
        if (view == null) return ""
        val byTitle = invokeNoArg(view, "getTitle") as? String
        if (!byTitle.isNullOrEmpty()) return byTitle
        return (invokeNoArg(view, "getOriginalTitle") as? String).orEmpty()
    }

    override fun topHolder(view: Any?): Any? {
        val topInventory = invokeNoArg(view, "getTopInventory") ?: return null
        return invokeNoArg(topInventory, "getHolder")
    }

    private fun invokeNoArg(target: Any?, methodName: String): Any? {
        if (target == null) return null
        return runCatching {
            target.javaClass.methods
                .firstOrNull { it.name == methodName && it.parameterCount == 0 }
                ?.invoke(target)
        }.getOrNull()
    }
}
