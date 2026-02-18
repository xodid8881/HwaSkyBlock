package org.hwabaeg.hwaskyblock.events.click.geyser

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.inventorys.holder.GeyserMenuGuiHolder
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import org.hwabaeg.hwaskyblock.world.IslandWorlds
import java.util.*

class GeyserMenuClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        val topIsGuiHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is GeyserMenuGuiHolder
        val topIsGuiTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
            '&',
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.geyser_menu_list"))
        )
        if (topIsGuiHolder || topIsGuiTitle) {
            e.isCancelled = true
        }
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            val name = player.name
            var world: World? = player.world
            val world_name = world!!.worldFolder.name
            val islandId = IslandWorlds.extractIslandId(world_name)
            val isGuiByHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is GeyserMenuGuiHolder
            val isGuiByTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.geyser_menu_list"))
            )
            if (isGuiByHolder || isGuiByTitle) {
                e.isCancelled = true
                if (islandId != null) {
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_move"))
                        )
                    ) {
                        player.closeInventory()
                        val homeValue = DatabaseManager.getSkyBlockData(
                            islandId.toString(),
                            "getSkyBlockHome"
                        ) as? Int ?: 0
                        if (homeValue == 0) {
                            val worldPath = IslandWorlds.worldName(islandId)
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
                            val worldPath = IslandWorlds.worldName(islandId)
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
                            val worldName = DatabaseManager.getSkyBlockData(
                                islandId.toString(),
                                "getSkyBlockHomeWorld"
                            ) as? String
                            val x = DatabaseManager.getSkyBlockData(
                                islandId.toString(),
                                "getSkyBlockHomeX"
                            ) as? Double ?: 0.0
                            val y = DatabaseManager.getSkyBlockData(
                                islandId.toString(),
                                "getSkyBlockHomeY"
                            ) as? Double ?: 0.0
                            val z = DatabaseManager.getSkyBlockData(
                                islandId.toString(),
                                "getSkyBlockHomeZ"
                            ) as? Double ?: 0.0
                            val yaw = (DatabaseManager.getSkyBlockData(
                                islandId.toString(),
                                "getSkyBlockHomeYaw"
                            ) as? Double ?: 0.0).toFloat()
                            val pitch = (DatabaseManager.getSkyBlockData(
                                islandId.toString(),
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
                        val leader = DatabaseManager.getSkyBlockData(
                            islandId.toString(),
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
                        val leader = DatabaseManager.getSkyBlockData(
                            islandId.toString(),
                            "getSkyBlockLeader"
                        ) as? String

                        if (leader == name) {
                            DatabaseManager.setUserData(
                                name,
                                player,
                                islandId,
                                "setPlayerEvent"
                            )
                            ConfigManager.saveConfigs()
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

