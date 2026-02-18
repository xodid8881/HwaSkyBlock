package org.hwabaeg.hwaskyblock.platform

import org.bukkit.command.Command

interface CommandRegistrar {
    fun register(fallbackPrefix: String, command: Command): Boolean
    fun unregister(command: Command): Boolean
    fun sync()
}
