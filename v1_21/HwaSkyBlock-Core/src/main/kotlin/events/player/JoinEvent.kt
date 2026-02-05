package org.hwabaeg.hwaskyblock.events.player

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.events.player.QuitEvent.Companion.LAST_WORLD

class JoinEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val name = player.name
        val hasSkyblockData = DatabaseManager.getUserData("$name.skyblock.setting", player, "getPlayerEvent") != null
        if (!hasSkyblockData) {
            DatabaseManager.insertUser(player)
            if (ConfigManager.getConfig("setting")?.getString("database.type") == "yml") {
                ConfigManager.saveConfigs()
            }
        }
        val lastWorld = LAST_WORLD[player.uniqueId]

        if (lastWorld != null) {
            val world = Bukkit.getWorld(lastWorld)

            if (world != null) {
                player.teleport(world.spawnLocation)
            } else {
                var worldName = Config.getString("main-spawn-world")
                val mainWorld = Bukkit.getWorld(worldName.toString())
                if (mainWorld == null) {
                    val safeWorld = Bukkit.getWorlds()[0]
                    player.teleport(safeWorld.spawnLocation)
                } else {
                    player.teleport(mainWorld.spawnLocation)
                }
            }

            LAST_WORLD.remove(player.uniqueId)
        }
    }
}