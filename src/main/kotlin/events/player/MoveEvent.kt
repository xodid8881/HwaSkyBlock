package org.hwabaeg.hwaskyblock.events.player

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class MoveEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
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
            val leader = DatabaseManager.getSkyBlockData(
                block_to_id.toString(),
                "$block_to_id.leader",
                "getSkyBlockLeader"
            ) as? String
            if (leader != null && leader != name) {
                val isSharer = (DatabaseManager.getShareData(
                    block_to_id.toString(),
                    "",
                    "getShareList",
                    null
                ) as? List<*>)?.contains(name) == true
                val hasJoinPermission = if (isSharer) {
                    DatabaseManager.getShareData(block_to_id.toString(), name, "can_join", "isUseJoin") as? Boolean
                        ?: true
                } else {
                    DatabaseManager.getSkyBlockData(
                        block_to_id.toString(),
                        "$block_to_id.join",
                        "isSkyBlockJoin"
                    ) as? Boolean ?: true
                }
                if (!hasJoinPermission) {
                    blockFrom?.let {
                        player.teleport(it.location.add(0.0, 1.0, 0.0))
                    }
                    val message = ChatColor.translateAlternateColorCodes(
                        '&',
                        Prefix + Objects.requireNonNull(MessageConfig.getString("message-event.no_permission"))
                    )
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                    event.isCancelled = true
                }
            }
        }
    }
}