package org.hwabaeg.hwaskyblock.platform.v1_16

import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.hwabaeg.hwaskyblock.platform.CommandRegistrar

class ReflectiveCommandRegistrar(
    private val server: Server,
) : CommandRegistrar {

    private val commandMap: CommandMap? by lazy { fetchCommandMap() }

    override fun register(fallbackPrefix: String, command: Command): Boolean {
        val map = commandMap ?: return false
        map.register(fallbackPrefix, command)
        return true
    }

    override fun unregister(command: Command): Boolean {
        val map = commandMap ?: return false
        runCatching { command.unregister(map) }
        removeKnownEntries(map, command)
        return true
    }

    override fun sync() {
        runCatching {
            val method = server.javaClass.methods.firstOrNull {
                it.name == "syncCommands" && it.parameterCount == 0
            } ?: return
            method.invoke(server)
        }
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
                if (iterator.next().value == command) {
                    iterator.remove()
                }
            }
        }
    }
}
