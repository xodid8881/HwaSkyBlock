package org.hwabaeg.hwaskyblock.platform.v1_21

import org.hwabaeg.hwaskyblock.platform.InventoryViewResolver

class BukkitInventoryViewResolver : InventoryViewResolver {
    override fun title(view: Any?): String {
        if (view == null) return ""
        val byOriginal = invokeNoArg(view, "getOriginalTitle") as? String
        if (!byOriginal.isNullOrEmpty()) return byOriginal
        val byTitle = invokeNoArg(view, "getTitle") as? String
        if (!byTitle.isNullOrEmpty()) return byTitle
        return invokeNoArg(view, "title")?.toString().orEmpty()
    }

    override fun topHolder(view: Any?): Any? {
        val topInventory = invokeNoArg(view, "getTopInventory") ?: return null
        val holderFalse = invokeOneArg(topInventory, "getHolder", false)
        if (holderFalse != null) return holderFalse
        val holderTrue = invokeOneArg(topInventory, "getHolder", true)
        if (holderTrue != null) return holderTrue
        val holderNoArg = invokeNoArg(topInventory, "getHolder")
        if (holderNoArg != null) return holderNoArg
        return readFieldRecursive(topInventory, "holder")
    }

    private fun invokeNoArg(target: Any?, methodName: String): Any? {
        if (target == null) return null
        return runCatching {
            target.javaClass.methods
                .firstOrNull { it.name == methodName && it.parameterCount == 0 }
                ?.invoke(target)
        }.getOrNull()
    }

    private fun invokeOneArg(target: Any?, methodName: String, arg: Any): Any? {
        if (target == null) return null
        return runCatching {
            target.javaClass.methods
                .firstOrNull { it.name == methodName && it.parameterCount == 1 }
                ?.invoke(target, arg)
        }.getOrNull()
    }

    private fun readFieldRecursive(target: Any?, fieldName: String): Any? {
        if (target == null) return null
        var current: Class<*>? = target.javaClass
        while (current != null) {
            val field = runCatching { current.getDeclaredField(fieldName) }.getOrNull()
            if (field != null) {
                return runCatching {
                    field.isAccessible = true
                    field.get(target)
                }.getOrNull()
            }
            current = current.superclass
        }
        return null
    }
}
