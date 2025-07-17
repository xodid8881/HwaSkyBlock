package org.hwabeag.hwaskyblock.schedules

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.scheduler.BukkitRunnable
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockTask : Runnable {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @Suppress("DEPRECATION")
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val name = player.name
            val skyblockId = DatabaseManager.getUserData("$name.skyblock", player, null)
            if (skyblockId != null) {
                val world = player.world
                val world_name = world.worldFolder.getName()
                val number: Array<String?> =
                    world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (number[0] == "HwaSkyBlock") {
                    val block_to_id = number[1]
                    val weather = DatabaseManager.getSkyBlockData(block_to_id.toString(), "$block_to_id.setting.weather", "getSkyBlockWeather") as? String
                    val time = DatabaseManager.getSkyBlockData(block_to_id.toString(), "$block_to_id.setting.time", "getSkyBlockTime") as? String
                    object : BukkitRunnable() {
                        override fun run() {
                            if (weather == "clear") {
                                world.clearWeatherDuration = Int.Companion.MAX_VALUE
                            } else {
                                world.clearWeatherDuration = 0
                            }
                            if (weather == "rainy") {
                                world.setStorm(true)
                                world.weatherDuration = Int.Companion.MAX_VALUE
                            } else {
                                world.setStorm(false)
                                world.weatherDuration = 0
                            }
                            if (weather == "thunder") {
                                world.thunderDuration = Int.Companion.MAX_VALUE
                                world.setStorm(true)
                            } else {
                                world.thunderDuration = 0
                            }
                            world.thunderDuration = 0
                            world.weatherDuration = 0

                            if (time == "morn") {
                                val time_l: Long = 1000
                                world.time = time_l
                            }
                            if (time == "noon") {
                                val time_l: Long = 6000
                                world.time = time_l
                            }
                            if (time == "evening") {
                                val time_l: Long = 18000
                                world.time = time_l
                            }
                        }
                    }.runTask(HwaSkyBlock.plugin)
                    val player_chunk = DatabaseManager.getUserData("$name.skyblock.pos", player, "getPlayerPos") as? String
                    if (player_chunk != block_to_id) {
                        if (DatabaseManager.getSkyBlockData(block_to_id.toString(), "$block_to_id.leader", "getSkyBlockLeader") != null) {
                            val welcome_message = DatabaseManager.getSkyBlockData(block_to_id.toString(), "$block_to_id.welcome_message", "getSkyBlockWelcomeMessage") as? String
                            player.sendMessage(
                                "$Prefix " + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(welcome_message)
                                )
                            )
                            val chunk_master = DatabaseManager.getSkyBlockData(block_to_id.toString(), "$block_to_id.leader", "getSkyBlockLeader") as? String

                            player.sendTitle(
                                Prefix,
                                ChatColor.translateAlternateColorCodes('&', "&r주인장 &f: &e$chunk_master")
                            )
                            DatabaseManager.setUserData("$name.skyblock.pos", player, block_to_id.toString(), "setPlayerPos")
                        }
                    }
                    DatabaseManager.setUserData("$name.skyblock.pos", player, block_to_id.toString(), "setPlayerPos")
                } else {
                    DatabaseManager.setUserData("$name.skyblock.pos", player, world_name, "setPlayerPos")
                }
            }
        }
    }
}