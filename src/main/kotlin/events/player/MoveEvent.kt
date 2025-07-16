package org.hwabeag.hwaskyblock.events.player

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.util.*

class MoveEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.getPlayer()
        val name = player.name
        val from = event.from
        val to = event.to
        val blockFrom = from.world?.getBlockAt(from)
        val blockTo = to?.world?.getBlockAt(to)
        val world = blockTo?.world
        val world_name = world?.worldFolder?.getName()
        val number: Array<String?> = world_name?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }!!.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val block_to_id = number[1]
            if (SkyBlockConfig.getString("$block_to_id.leader") != null) {
                if (SkyBlockConfig.getString("$block_to_id.leader") != name) {
                    if (SkyBlockConfig.getString("$block_to_id.sharer.$name") == null) {
                        if (!SkyBlockConfig.getBoolean("$block_to_id.join")) {
                            if (blockFrom != null) {
                                player.teleport(blockFrom.location.add(0.0, 1.0, 0.0))
                            }
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
                            )
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                            event.setCancelled(true)
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean("$block_to_id.sharer.$name.join")) {
                            if (blockFrom != null) {
                                player.teleport(blockFrom.location.add(0.0, 1.0, 0.0))
                            }
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
                            )
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                            event.setCancelled(true)
                        }
                    }
                }
            }
        }
    }
}