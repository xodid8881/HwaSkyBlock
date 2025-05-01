package org.hwabeag.hwaskyblock.schedules

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.scheduler.BukkitRunnable
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.util.*

class HwaSkyBlockTask : Runnable {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @Suppress("DEPRECATION")
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val name = player.name
            if (PlayerConfig.get("$name.skyblock") != null) {
                val world = player.world
                val world_name = world.worldFolder.getName()
                val number: Array<String?> =
                    world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (number[0] == "HwaSkyBlock") {
                    val block_to_id = number[1]
                    val weather = SkyBlockConfig.getString("$block_to_id.setting.weather")
                    val time = SkyBlockConfig.getString("$block_to_id.setting.time")
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
                    val player_chunk = PlayerConfig.getString("$name.skyblock.pos")
                    if (player_chunk != block_to_id) {
                        if (SkyBlockConfig.get("$block_to_id.leader") != null) {
                            val welcome_message = SkyBlockConfig.getString("$block_to_id.welcome_message")
                            player.sendMessage(
                                "$Prefix " + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(welcome_message)
                                )
                            )
                            val chunk_master = SkyBlockConfig.getString("$block_to_id.leader")
                            player.sendTitle(
                                Prefix,
                                ChatColor.translateAlternateColorCodes('&', "&r주인장 &f: &e$chunk_master")
                            )
                            PlayerConfig.set("$name.skyblock.pos", block_to_id)
                            ConfigManager.saveConfigs()
                        }
                    }
                    PlayerConfig.set("$name.skyblock.pos", block_to_id)
                    ConfigManager.saveConfigs()
                } else {
                    PlayerConfig.set("$name.skyblock.pos", world_name)
                    ConfigManager.saveConfigs()
                }
            }
        }
    }
}