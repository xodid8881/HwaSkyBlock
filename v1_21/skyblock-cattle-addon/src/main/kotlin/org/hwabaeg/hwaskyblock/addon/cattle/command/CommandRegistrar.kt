package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandMap

class CommandRegistrar(private val server: Server) {
    private val commandMap: CommandMap? by lazy { fetchCommandMap() }

    fun register(command: Command) {
        val map = commandMap ?: return
        map.register("hwaskyblock", command)
        syncCommands()
    }

    fun unregister(command: Command) {
        val map = commandMap ?: return
        runCatching { command.unregister(map) }
        removeKnownEntries(map, command)
        syncCommands()
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

    @Suppress("UNCHECKED_CAST")
    private fun removeKnownEntries(map: CommandMap, command: Command) {
        runCatching {
            val field = map.javaClass.getDeclaredField("knownCommands")
            field.isAccessible = true
            val known = field.get(map) as? MutableMap<String, Command> ?: return
            val iterator = known.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.value == command) {
                    iterator.remove()
                }
            }
        }
    }

    private fun syncCommands() {
        runCatching {
            val method = server.javaClass.methods.firstOrNull { it.name == "syncCommands" && it.parameterCount == 0 }
                ?: return
            method.invoke(server)
        }
    }
}
