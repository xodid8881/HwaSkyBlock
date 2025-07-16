package org.hwabeag.hwaskyblock.events.block

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.util.*

class BreakEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        val name = player.name
        val block = event.getBlock()
        val world = block.world
        val world_name = world.worldFolder.name
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val id = number[1]
            if (SkyBlockConfig.getString("$id.leader") != null) {
                if (SkyBlockConfig.getString("$id.leader") != name) {
                    if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                        if (!SkyBlockConfig.getBoolean("$id.break")) {
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
                            )
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                            event.isCancelled = true
                            return
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean("$id.sharer.$name.break")) {
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
                            )
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                            event.isCancelled = true
                            return
                        }
                    }
                    if (!isInRegion(event.getBlock(), id)) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }


    private fun isInRegion(block: Block, id: String?): Boolean {
        val x = block.x
        val y = block.y
        val z = block.z
        return (x >= 0 && x < SkyBlockConfig.getInt(id + ".size")) &&
                (y >= 0 && y < 256) &&
                (z >= 0 && z < SkyBlockConfig.getInt(id + ".size"))
    }
}