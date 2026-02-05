package org.hwabaeg.hwaskyblock.addon.custompoint

import org.hwabaeg.hwaskyblock.addon.custompoint.config.AddonConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.config.BlockPointConfig
import org.hwabaeg.hwaskyblock.addon.custompoint.hook.HookManager
import org.hwabaeg.hwaskyblock.addon.custompoint.listener.CustomBlockListener
import org.hwabaeg.hwaskyblock.addon.custompoint.listener.CustomCropsListener
import org.hwabaeg.hwaskyblock.addon.custompoint.listener.VanillaBlockListener
import org.hwabaeg.hwaskyblock.addon.custompoint.util.LogUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon

class SkyblockCustomPointAddon : HwaSkyBlockAddon, Listener {

    private lateinit var plugin: JavaPlugin
    private lateinit var hookManager: HookManager

    override fun onEnable(main: JavaPlugin) {
        plugin = main

        AddonConfig.load(plugin)
        BlockPointConfig.load(plugin)

        LogUtil.init(plugin.logger, AddonConfig.debug)

        hookManager = HookManager(plugin.logger)

        val pm = Bukkit.getPluginManager()

        pm.registerEvents(this, plugin)
        pm.registerEvents(VanillaBlockListener(), plugin)
        pm.registerEvents(CustomBlockListener(hookManager), plugin)
        pm.registerEvents(CustomCropsListener(), plugin)

        LogUtil.info("SkyblockCustomPointAddon enabled")
    }

    @EventHandler
    fun onServerLoad(event: ServerLoadEvent) {
        hookManager.init()
        hookManager.debugStatus()
    }

    override fun onDisable() {
        LogUtil.info("SkyblockCustomPointAddon disabled")
    }
}
