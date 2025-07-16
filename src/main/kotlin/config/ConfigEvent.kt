package org.hwabeag.hwaskyblock.config

import database.user.SelectUser
import database.utils.hwaskyblock_skyblock
import database.utils.hwaskyblock_user
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.skyblock.SelectSkyblock
import java.util.*

class ConfigEvent {
    companion object {

        var config: FileConfiguration = ConfigManager.getConfig("setting")!!
        var skyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
        var playerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
        var Prefix: String = ChatColor.translateAlternateColorCodes(
            '&',
            Objects.requireNonNull<String?>(config.getString("hwaskyblock-system.prefix"))
        )

        var User_Select: SelectUser = SelectUser()
        var SkyBlock_Select: SelectSkyblock = SelectSkyblock()

        var Select_SkyBlock_List: HashMap<String?, hwaskyblock_skyblock?> = HashMap<String?, hwaskyblock_skyblock?>()
        var Select_User_List: HashMap<String?, hwaskyblock_user?> = HashMap<String?, hwaskyblock_user?>()

        fun updateConfig(
            location: String,
            data: String,
            value: Any,
            player: Player? = null,
            skyblockId: String? = null
        ) {
            val dbType = config.getString("database.type") ?: "yml"

            when (dbType.lowercase()) {
                "yml" -> {
                    when (location) {
                        "setting" -> config.set(data, value)
                        "skyblock" -> skyBlockConfig.set(data, value)
                        "player" -> playerConfig.set(data, value)
                    }
                }

                "mysql" -> {
                    when (location) {
                        "player" -> {
                            if (player != null && User_Select.UserSelect(player) == 0) {
                            }
                        }
                        "skyblock" -> {
                            if (skyblockId != null && SkyBlock_Select.SelectSkyBlock(skyblockId) == 0) {
                            }
                        }
                    }
                }

                else -> {
                    println("지원하지 않는 DB 타입입니다: $dbType")
                }
            }
        }

    }
}
