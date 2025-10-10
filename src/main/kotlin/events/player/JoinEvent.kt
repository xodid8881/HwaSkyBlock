package org.hwabaeg.hwaskyblock.events.player

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager

class JoinEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val name = player.name
        val hasSkyblockData = DatabaseManager.getUserData("$name.skyblock.setting", player, "getPlayerSetting") != null
        if (!hasSkyblockData) {
            DatabaseManager.insertUser(player)
            if (ConfigManager.getConfig("setting")?.getString("database.type") == "yml") {
                ConfigManager.saveConfigs()
            }
        }
    }
}