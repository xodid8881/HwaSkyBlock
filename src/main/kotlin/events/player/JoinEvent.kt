package org.hwabeag.hwaskyblock.events.player


import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.user.InsertUser

class JoinEvent : Listener {

    var Insert_User: InsertUser = InsertUser()
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.getPlayer()
        val name = player.name
        if ((Config.getString("database.type") == "mysql")) {
            Insert_User.UserInsert(player)
        } else {
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
}