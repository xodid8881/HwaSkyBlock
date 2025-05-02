package org.hwabeag.hwaskyblock.api

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.config.ConfigManager

class HwaSkyBlockAPIImpl : HwaSkyBlockAPI {

    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!

    override fun hasIsland(player: Player): Boolean { // 섬 보유 확인
        val name = player.name
        return PlayerConfig.getInt("$name.skyblock.possession_count") != 0
    }

    override fun hasOwner(player: Player, number: Int): Boolean { // 섬 리더 확인
        val name = player.name
        return SkyBlockConfig.getString("$number.leader") != name
    }

    override fun upgradeIsland(player: Player, number: Int, plusSize: Int) {
        val size = SkyBlockConfig.getInt("$number.size")
        SkyBlockConfig.set("$number.size", size+plusSize)
        ConfigManager.saveConfigs()
    }
}