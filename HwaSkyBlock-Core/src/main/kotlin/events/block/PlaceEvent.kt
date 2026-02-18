package org.hwabaeg.hwaskyblock.events.block

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class PlaceEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.getPlayer()
        player.name
        val block = event.getBlock()
        val world = block.world
        val world_name = world.worldFolder.name
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val id = number[1]
            val leader = DatabaseManager.getSkyBlockData(id.toString(), "getSkyBlockLeader") as? String
            if (leader != null && leader != player.name) {
                val isSharer = DatabaseManager.getShareDataList(id.toString()).contains(player.name)
                val hasPlacePermission = if (!isSharer) {
                    DatabaseManager.getSkyBlockData(id.toString(), "isSkyBlockPlace") as? Boolean ?: false
                } else {
                    DatabaseManager.getShareData(id.toString(), player.name, "isUsePlace") as? Boolean ?: false
                }
                if (!hasPlacePermission) {
                    val message = ChatColor.translateAlternateColorCodes(
                        '&',
                        Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
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

    private fun isInRegion(block: Block, id: String?): Boolean {
        val x = block.x
        val y = block.y
        val z = block.z
        val size = DatabaseManager.getSkyBlockData(id.toString(), "getSkyBlockSize") as? Int ?: 0
        return (x >= 0 && x < size) &&
                (y >= 0 && y < 256) &&
                (z >= 0 && z < size)
    }
}
