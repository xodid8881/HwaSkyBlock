package org.hwabaeg.hwaskyblock.events.player

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.events.player.QuitEvent.Companion.LAST_WORLD

class JoinEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val name = player.name
        HwaSkyBlock.onlineNameCache.add(name)

        val hasSkyblockData =
            DatabaseManager.getUserData("$name.skyblock.setting", player, "getPlayerEvent") != null

        if (!hasSkyblockData) {
            DatabaseManager.insertUser(player)
            if (ConfigManager.getConfig("setting")?.getString("database.type") == "yml") {
                ConfigManager.saveConfigs()
            }
        }

        val lastWorld = LAST_WORLD[player.uniqueId]

        // ✅ 서버 재시작 대응 로직
        if (lastWorld == null) {

            // DB에 저장된 섬 월드명 (예시)
            val islandWorldName =
                DatabaseManager.getUserData("$name.skyblock.world", player, "getPlayerEvent") as? String

            val islandWorld = islandWorldName?.let { Bukkit.getWorld(it) }

            if (islandWorld == null) {
                val worldName = Config.getString("main-spawn-world")
                val mainWorld = Bukkit.getWorld(worldName.toString())
                val safeWorld = mainWorld ?: Bukkit.getWorlds()[0]
                player.teleport(safeWorld.spawnLocation)
            }

            return
        }

        // 기존 로직 유지
        val world = Bukkit.getWorld(lastWorld)

        if (world != null) {
            player.teleport(world.spawnLocation)
        } else {
            val worldName = Config.getString("main-spawn-world")
            val mainWorld = Bukkit.getWorld(worldName.toString())
            val safeWorld = mainWorld ?: Bukkit.getWorlds()[0]
            player.teleport(safeWorld.spawnLocation)
        }

        LAST_WORLD.remove(player.uniqueId)
    }
}
