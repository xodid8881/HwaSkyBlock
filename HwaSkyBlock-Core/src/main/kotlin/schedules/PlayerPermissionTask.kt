package org.hwabaeg.hwaskyblock.schedules

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.scheduler.BukkitRunnable
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class PlayerPermissionTask : BukkitRunnable() {

    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    private var LastIsland: HashMap<UUID, String?> = HashMap()

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            val name = player.name
            val world = player.world
            val world_name = world.worldFolder.name
            val number: Array<String?> =
                world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val block_to_id = number[1]

                val last_id = LastIsland[player.uniqueId]
                if (block_to_id == last_id) {
                    continue
                }
                LastIsland[player.uniqueId] = block_to_id

                val leader = DatabaseManager.getSkyBlockData(
                    block_to_id.toString(),
                    "getSkyBlockLeader"
                ) as? String
                if (leader != null && leader != name) {
                    val isSharer = (DatabaseManager.getShareData(
                        block_to_id.toString(),
                        "",
                        null
                    ) as? List<*>)?.contains(name) == true
                    val hasJoinPermission = if (isSharer) {
                        DatabaseManager.getShareData(block_to_id.toString(), name, "isUseJoin") as? Boolean
                            ?: true
                    } else {
                        DatabaseManager.getSkyBlockData(
                            block_to_id.toString(),
                            "isSkyBlockJoin"
                        ) as? Boolean ?: true
                    }
                    if (!hasJoinPermission) {
                        var worldName = Config.getString("main-spawn-world")
                        val world = Bukkit.getWorld(worldName.toString())
                        if (world == null) {
                            val safeWorld = Bukkit.getWorlds()[0]
                            player.teleport(safeWorld.spawnLocation)
                        } else {
                            player.teleport(world.spawnLocation)
                        }
                        val message = ChatColor.translateAlternateColorCodes(
                            '&',
                            Prefix + Objects.requireNonNull(MessageConfig.getString("message-event.no_permission"))
                        )
                        player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent(message)
                        )
                    }
                }
            }
        }
    }
}
