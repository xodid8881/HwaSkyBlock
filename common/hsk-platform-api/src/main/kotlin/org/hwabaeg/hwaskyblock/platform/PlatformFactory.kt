package org.hwabaeg.hwaskyblock.platform

import org.bukkit.Server

object PlatformFactory {
    fun createCommandRegistrar(server: Server): CommandRegistrar {
        val minor = detectMinor(server)
        val candidates = candidateMinors(minor)
        for (m in candidates) {
            val instance = newInstanceOrNull(
                "org.hwabaeg.hwaskyblock.platform.v1_${m}.ReflectiveCommandRegistrar",
                arrayOf(Server::class.java),
                arrayOf(server)
            )
            if (instance is CommandRegistrar) return instance
        }
        error("No CommandRegistrar implementation found for Bukkit ${server.bukkitVersion}")
    }

    fun createMaterialResolver(server: Server): MaterialResolver {
        val minor = detectMinor(server)
        val candidates = candidateMinors(minor)
        for (m in candidates) {
            val instance = newInstanceOrNull(
                "org.hwabaeg.hwaskyblock.platform.v1_${m}.BukkitMaterialResolver",
                emptyArray(),
                emptyArray()
            )
            if (instance is MaterialResolver) return instance
        }
        error("No MaterialResolver implementation found for Bukkit ${server.bukkitVersion}")
    }

    fun createInventoryViewResolver(server: Server): InventoryViewResolver {
        val minor = detectMinor(server)
        val candidates = candidateMinors(minor)
        for (m in candidates) {
            val instance = newInstanceOrNull(
                "org.hwabaeg.hwaskyblock.platform.v1_${m}.BukkitInventoryViewResolver",
                emptyArray(),
                emptyArray()
            )
            if (instance is InventoryViewResolver) return instance
        }
        error("No InventoryViewResolver implementation found for Bukkit ${server.bukkitVersion}")
    }

    private fun detectMinor(server: Server): Int {
        val version = server.bukkitVersion
        val match = Regex("""(\d+)\.(\d+)""").find(version)
        val minor = match?.groupValues?.getOrNull(2)?.toIntOrNull() ?: 21
        return minor.coerceIn(16, 21)
    }

    private fun candidateMinors(minor: Int): List<Int> {
        val normalized = minor.coerceIn(16, 21)
        val fallback = (21 downTo 16).toList()
        return listOf(normalized) + fallback.filter { it != normalized }
    }

    private fun newInstanceOrNull(
        className: String,
        parameterTypes: Array<Class<*>>,
        args: Array<Any>
    ): Any? {
        return runCatching {
            val clazz = Class.forName(className)
            val constructor = clazz.getDeclaredConstructor(*parameterTypes)
            constructor.newInstance(*args)
        }.getOrNull()
    }
}
