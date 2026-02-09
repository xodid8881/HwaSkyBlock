package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandMap

class CommandRegistrar(private val server: Server) {
    private val commandMap: CommandMap? by lazy { fetchCommandMap() }

    fun register(command: Command) {
        val map = commandMap ?: return
        map.register("hwaskyblock", command)
    }

    private fun fetchCommandMap(): CommandMap? {
        return try {
            val field = server.javaClass.getDeclaredField("commandMap")
            field.isAccessible = true
            field.get(server) as? CommandMap
        } catch (_: Exception) {
            null
        }
    }
}
