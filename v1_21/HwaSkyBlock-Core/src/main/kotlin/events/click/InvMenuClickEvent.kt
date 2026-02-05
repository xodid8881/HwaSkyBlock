package org.hwabaeg.hwaskyblock.events.click

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
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockMenuGUI
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import org.hwabaeg.hwaskyblock.inventorys.geyser.GeyserMenuGUI
import java.util.*

class InvMenuClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
                )
            ) {
                e.isCancelled = true
                val clickitem = e.currentItem?.itemMeta?.displayName
                val possessionData = DatabaseManager.getUserData(
                    "$name.skyblock.possession",
                    player,
                    "getPlayerPossession"
                )
                if (possessionData != null) {
                    val possessionData: Map<*, *>? = DatabaseManager.getUserData(
                        "$name.skyblock.possession",
                        player,
                        "getPlayerPossession"
                    ) as? Map<*, *>
                    if (possessionData != null) {
                        for (key in possessionData.keys) {
                            val islandId = key.toString()
                            var displayName =
                                MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.geyser_my")
                            displayName = displayName?.replace("{number}", islandId) ?: continue
                            if (clickitem == ChatColor.translateAlternateColorCodes('&', displayName)) {
                                if (number.size > 1 && number[1] != null && number[1] == islandId) {
                                    val inv = GeyserMenuGUI(islandId)
                                    inv.open(player)
                                    return
                                }
                            }
                            displayName = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                            displayName = displayName?.replace("{number}", islandId) ?: continue
                            if (clickitem == ChatColor.translateAlternateColorCodes('&', displayName)) {
                                if (number.size > 1 && number[1] != null && number[1] == islandId) {
                                    if (e.click.isShiftClick && e.click.isLeftClick) {
                                        val leader = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockLeader"
                                        ) as? String
                                        if (leader == name) {
                                            val inv = HwaSkyBlockGlobalFragGUI(islandId)
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
                                    } else if (e.click.isShiftClick && e.click.isRightClick) {
                                        val leader = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockLeader"
                                        ) as? String

                                        if (leader == name) {
                                            DatabaseManager.setUserData(
                                                name,
                                                player,
                                                islandId,
                                                "setPlayerPos"
                                            )
                                            ConfigManager.saveConfigs()
                                            val inv = HwaSkyBlockSharerGUI(player, islandId)
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
                                } else if (e.click.isLeftClick) {
                                    player.closeInventory()
                                    val homeValue = DatabaseManager.getSkyBlockData(
                                        islandId,
                                        "getSkyBlockHome"
                                    ) as? Int ?: 0
                                    if (homeValue == 0) {
                                        val worldPath = "HwaSkyBlock.$islandId"
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
                                        val worldPath = "HwaSkyBlock.$islandId"
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
                                            islandId,
                                            "getSkyBlockHomeWorld"
                                        ) as? String
                                        val x = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeX"
                                        ) as? Double ?: 0.0
                                        val y = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeY"
                                        ) as? Double ?: 0.0
                                        val z = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeZ"
                                        ) as? Double ?: 0.0
                                        val yaw = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeYaw"
                                        ) as? Double ?: 0.0).toFloat()
                                        val pitch = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomePitch"
                                        ) as? Double ?: 0.0).toFloat()

                                        val location: Location? = if (worldName != null) {
                                            val world: World? = Bukkit.getWorld(worldName)
                                            if (world != null) Location(world, x, y, z, yaw, pitch) else null
                                        } else {
                                            null
                                        }
                                        player.teleport(Objects.requireNonNull<Location?>(location))
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
                val shareData = DatabaseManager.getUserData(
                    name,
                    player,
                    "getPlayerSharer"
                )
                if (shareData != null) {
                    val sharerData = DatabaseManager.getUserData(
                        name,
                        player,
                        "getPlayerSharer"
                    ) as? Map<*, *>

                    if (sharerData != null) {
                        for (key in sharerData.keys) {
                            val islandId = key.toString()
                            var displayName = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                            displayName = displayName?.replace("{number}", islandId) ?: continue

                            if (clickitem == ChatColor.translateAlternateColorCodes('&', displayName)) {
                                if (e.click.isLeftClick) {
                                    player.closeInventory()
                                    val homeValue = DatabaseManager.getSkyBlockData(
                                        islandId,
                                        "getSkyBlockHome"
                                    ) as? Int ?: 0

                                    if (homeValue == 0) {
                                        val worldPath = "HwaSkyBlock.$islandId"
                                        world = WorldCreator.name(worldPath).createWorld()
                                        val location = Objects.requireNonNull<World?>(world).spawnLocation
                                        player.teleport(Objects.requireNonNull<Location?>(location))
                                    } else {
                                        val worldName = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeWorld"
                                        ) as? String
                                        val x = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeX"
                                        ) as? Double ?: 0.0
                                        val y = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeY"
                                        ) as? Double ?: 0.0
                                        val z = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeZ"
                                        ) as? Double ?: 0.0
                                        val yaw = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomeYaw"
                                        ) as? Double ?: 0.0).toFloat()
                                        val pitch = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "getSkyBlockHomePitch"
                                        ) as? Double ?: 0.0).toFloat()

                                        val location: Location? = if (worldName != null) {
                                            val world: World? = Bukkit.getWorld(worldName)
                                            if (world != null) Location(world, x, y, z, yaw, pitch) else null
                                        } else {
                                            null
                                        }
                                        player.teleport(Objects.requireNonNull<Location?>(location))
                                    }
                                    return
                                }
                            }
                        }
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.previous_page"))
                        )
                    ) {
                        val page =
                            DatabaseManager.getUserData("$name.skyblock.page", player, "getPlayerPage") as? Int ?: 0
                        val plus = page - 1
                        DatabaseManager.setUserData("$name.skyblock.page", player, plus, "setPlayerPage")
                        ConfigManager.saveConfigs()
                        val inv = HwaSkyBlockMenuGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.next_page"))
                        )
                    ) {
                        val page =
                            DatabaseManager.getUserData("$name.skyblock.page", player, "getPlayerPage") as? Int ?: 0
                        val plus = page + 1
                        DatabaseManager.setUserData("$name.skyblock.page", player, plus, "setPlayerPage")
                        ConfigManager.saveConfigs()
                        val inv = HwaSkyBlockMenuGUI(player)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}