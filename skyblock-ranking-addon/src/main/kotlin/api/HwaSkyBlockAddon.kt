package org.hwabaeg.hwaskyblock.api

import org.bukkit.plugin.java.JavaPlugin

interface HwaSkyBlockAddon {
    fun onEnable(main: JavaPlugin)

    fun onDisable()
}
