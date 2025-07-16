package org.hwabeag.hwaskyblock.events

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabeag.hwaskyblock.database.config.ConfigManager

class JoinEvent : Listener {
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.getPlayer()
        val name = player.name
        if (PlayerConfig.get("$name.skyblock") == null) {
            PlayerConfig.addDefault("$name.skyblock.possession_count", 0)
            PlayerConfig.set("$name.skyblock.possession_count", 0)
            PlayerConfig.set("$name.skyblock.pos", 0)
            PlayerConfig.set("$name.skyblock.page", 1)
            PlayerConfig.set("$name.skyblock.setting", "")
            ConfigManager.saveConfigs()
        }
    }
}