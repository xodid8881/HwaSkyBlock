package org.hwabeag.hwaskyblock.events.player

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager

class JoinEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.getPlayer()
        val name = player.name
        val hasSkyblockData = DatabaseManager.getUserData("$name.skyblock.setting", player, "getPlayerSetting") != null
        if (!hasSkyblockData) {
            DatabaseManager.setUserData("$name.skyblock.possession_count", player, 0, "setPlayerPossessionCount")
            DatabaseManager.setUserData("$name.skyblock.pos", player, 0, "setPlayerPos")
            DatabaseManager.setUserData("$name.skyblock.page", player, 1, "setPlayerPage")
            DatabaseManager.setUserData("$name.skyblock.setting", player, "", "setPlayerSetting")
            if (Config.getString("database.type") == "yml") {
                ConfigManager.saveConfigs()
            }
        }
    }
}