package org.hwabeag.hwaskyblock.events.block

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.util.*

class BreakEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
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
            if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") != null) {
                val leader =
                    DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") as? String
                if (leader != name) {
                    val isSharer = DatabaseManager.getSkyBlockShareList(id.toString()).contains(name)

                    val hasBreakPermission = if (!isSharer) {
                        DatabaseManager.getSkyBlockData(id.toString(), "$id.break", "isSkyBlockBreak") as? Boolean
                            ?: false
                    } else {
                        DatabaseManager.getSkyBlockSharePermission(id.toString(), name, "break") ?: false
                    }

                    if (!hasBreakPermission) {
                        val message = ChatColor.translateAlternateColorCodes(
                            '&',
                            Prefix + (MessageConfig.getString("message-event.no_permission") ?: "")
                        )
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                        event.isCancelled = true
                        return
                    }

                    if (!isInRegion(event.block, id)) {
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
        val size = (DatabaseManager.getSkyBlockData(id.toString(), "$id.size", "getSkyBlockSize") as? Int) ?: 0
        return (x >= 0 && x < size) &&
                (y >= 0 && y < 256) &&
                (z >= 0 && z < size)

    }
}