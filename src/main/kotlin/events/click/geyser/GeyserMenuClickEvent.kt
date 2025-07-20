package org.hwabeag.hwaskyblock.events.click.geyser

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import java.util.*

class GeyserMenuClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.Companion.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.Companion.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            val name = player.name
            var world: World? = player.world
            val world_name = world!!.worldFolder.name
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (e.view.title == ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.global_use_list"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val islandId = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_move"))
                        )
                    ) {
                        player.closeInventory()
                        val homeValue = DatabaseManager.Companion.getSkyBlockData(
                            islandId.toString(),
                            "$islandId.home",
                            "getSkyBlockHome"
                        ) as? Int ?: 0
                        if (homeValue == 0) {
                            val worldPath = "worlds/HwaSkyBlock.$islandId"
                            world = Bukkit.getServer().getWorld(worldPath)
                            if (world == null) {
                                world = WorldCreator(worldPath).createWorld()
                                val location = Objects.requireNonNull<World?>(world).spawnLocation
                                player.teleport(location)
                                return
                            }
                            val location = Objects.requireNonNull<World?>(world).spawnLocation
                            player.teleport(location)
                            return
                        } else {
                            val worldPath = "worlds/HwaSkyBlock.$islandId"
                            world = Bukkit.getServer().getWorld(worldPath)
                            if (world == null) {
                                val createWorld = WorldCreator(worldPath).createWorld()
                                val location = Objects.requireNonNull<World?>(createWorld).spawnLocation
                                player.teleport(location)
                                return
                            } else {
                                val location = Objects.requireNonNull<World?>(world).spawnLocation
                                player.teleport(location)
                            }
                            val worldName = DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.world",
                                "getSkyBlockHomeWorld"
                            ) as? String
                            val x = DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.x",
                                "getSkyBlockHomeX"
                            ) as? Double ?: 0.0
                            val y = DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.y",
                                "getSkyBlockHomeY"
                            ) as? Double ?: 0.0
                            val z = DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.z",
                                "getSkyBlockHomeZ"
                            ) as? Double ?: 0.0
                            val yaw = (DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.yaw",
                                "getSkyBlockHomeYaw"
                            ) as? Double ?: 0.0).toFloat()
                            val pitch = (DatabaseManager.Companion.getSkyBlockData(
                                islandId.toString(),
                                "$islandId.home.pitch",
                                "getSkyBlockHomePitch"
                            ) as? Double ?: 0.0).toFloat()

                            val location: Location? = if (worldName != null) {
                                val world = Bukkit.getWorld(worldName)
                                if (world != null) Location(world, x, y, z, yaw, pitch) else null
                            } else {
                                null
                            }
                            player.teleport(Objects.requireNonNull<Location?>(location))
                            return
                        }
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_global"))
                        )
                    ) {
                        val leader = DatabaseManager.Companion.getSkyBlockData(
                            islandId.toString(),
                            "$islandId.leader",
                            "getSkyBlockLeader"
                        ) as? String
                        if (leader == name) {
                            var inv: HwaSkyBlockGlobalFragGUI? = null
                            inv = HwaSkyBlockGlobalFragGUI(islandId)
                            inv.open(player)
                        } else {
                            player.closeInventory()
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                            )
                            player.spigot()
                                .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                        }
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_sharer"))
                        )
                    ) {
                        val leader = DatabaseManager.Companion.getSkyBlockData(
                            islandId.toString(),
                            "$islandId.leader",
                            "getSkyBlockLeader"
                        ) as? String

                        if (leader == name) {
                            DatabaseManager.Companion.setUserData(
                                name,
                                player,
                                islandId,
                                "setSkyblockSetting"
                            )
                            ConfigManager.Companion.saveConfigs()
                            var inv: HwaSkyBlockSharerGUI? = null
                            inv = HwaSkyBlockSharerGUI(player, islandId)
                            inv.open(player)
                        } else {
                            player.closeInventory()
                            val message = ChatColor.translateAlternateColorCodes(
                                '&',
                                Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                            )
                            player.spigot()
                                .sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                        }
                        return
                    }
                }
            }
        }
    }
}