package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.Server
import org.bukkit.command.Command
import org.hwabaeg.hwaskyblock.platform.PlatformFactory

class CommandRegistrar(private val server: Server) {
    private val delegate = PlatformFactory.createCommandRegistrar(server)

    fun register(command: Command) {
        if (delegate.register("hwaskyblock", command)) {
            delegate.sync()
        }
    }

    fun unregister(command: Command) {
        if (delegate.unregister(command)) {
            delegate.sync()
        }
    }
}
