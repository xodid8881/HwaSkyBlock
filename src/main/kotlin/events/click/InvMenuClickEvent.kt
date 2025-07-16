package org.hwabeag.hwaskyblock.events.click

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockMenuGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import java.util.*

class InvMenuClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.Companion.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.Companion.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    fun isBedrockPlayer(player: Player): Boolean {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            val name = player.name
            var world: World? = player.world
            val world_name = world!!.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (e.view.title == ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
                )
            ) {
                e.isCancelled = true
                val clickitem = e.currentItem?.getItemMeta()?.getDisplayName()
                val possessionData = DatabaseManager.getUserData(name, player, "getSkyblockPossession")
                if (possessionData != null) {
                    val possessionData =
                        DatabaseManager.getUserData(name, player, "getSkyblockPossession") as? Map<*, *>
                    if (possessionData != null) {
                        for (key in possessionData.keys) {
                            val islandId = key.toString()
                            var displayName = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                            displayName = displayName?.replace("{number}", islandId) ?: continue
                            if (clickitem == ChatColor.translateAlternateColorCodes('&', displayName)) {
                                if (e.click == ClickType.SHIFT_LEFT) {
                                    val leader = DatabaseManager.getSkyBlockData(
                                        islandId,
                                        "$islandId.leader",
                                        "getSkyBlockLeader"
                                    ) as? String
                                    if (leader == name) {
                                        var inv: HwaSkyBlockGlobalFragGUI? = null
                                        inv = HwaSkyBlockGlobalFragGUI(islandId)
                                        inv.open(player)
                                    } else {
                                        e.inventory.clear()
                                        player.closeInventory()
                                        val message = ChatColor.translateAlternateColorCodes(
                                            '&',
                                            Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                                        )
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                                    }
                                    return
                                }
                                if (e.click == ClickType.SHIFT_RIGHT) {
                                    val leader = DatabaseManager.getSkyBlockData(
                                        islandId,
                                        "$islandId.leader",
                                        "getSkyBlockLeader"
                                    ) as? String

                                    if (leader == name) {
                                        DatabaseManager.setUserData(
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
                                        e.inventory.clear()
                                        player.closeInventory()
                                        val message = ChatColor.translateAlternateColorCodes(
                                            '&',
                                            Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                                        )
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                                    }
                                    return
                                }
                                if (e.click == ClickType.LEFT) {
                                    e.inventory.clear()
                                    player.closeInventory()
                                    val homeValue = DatabaseManager.getSkyBlockData(
                                        islandId,
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
                                        val worldName = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.world",
                                            "getSkyBlockHomeWorld"
                                        ) as? String
                                        val x = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.x",
                                            "getSkyBlockHomeX"
                                        ) as? Double ?: 0.0
                                        val y = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.y",
                                            "getSkyBlockHomeY"
                                        ) as? Double ?: 0.0
                                        val z = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.z",
                                            "getSkyBlockHomeZ"
                                        ) as? Double ?: 0.0
                                        val yaw = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.yaw",
                                            "getSkyBlockHomeYaw"
                                        ) as? Double ?: 0.0).toFloat()
                                        val pitch = (DatabaseManager.getSkyBlockData(
                                            islandId,
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
                                }
                            }
                        }
                    }
                }
                val shareData = DatabaseManager.getUserData(
                    name,
                    player,
                    "getSkyblockSharer"
                )
                if (shareData != null) {
                    val sharerData = DatabaseManager.getUserData(
                        name,
                        player,
                        "getSkyblockSharer"
                    ) as? Map<*, *>

                    if (sharerData != null) {
                        for (key in sharerData.keys) {
                            val islandId = key.toString()
                            var displayName = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                            displayName = displayName?.replace("{number}", islandId) ?: continue

                            if (clickitem == ChatColor.translateAlternateColorCodes('&', displayName)) {
                                if (e.click == ClickType.LEFT) {
                                    e.inventory.clear()
                                    player.closeInventory()
                                    val homeValue = DatabaseManager.getSkyBlockData(
                                        islandId,
                                        "$islandId.home",
                                        "getSkyBlockHome"
                                    ) as? Int ?: 0

                                    if (homeValue == 0) {
                                        val worldPath = "worlds/HwaSkyBlock.$islandId"
                                        world = WorldCreator.name(worldPath).createWorld()
                                        val location = Objects.requireNonNull<World?>(world).spawnLocation
                                        player.teleport(Objects.requireNonNull<Location?>(location))
                                    } else {
                                        val worldName = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.world",
                                            "getSkyBlockHomeWorld"
                                        ) as? String
                                        val x = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.x",
                                            "getSkyBlockHomeX"
                                        ) as? Double ?: 0.0
                                        val y = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.y",
                                            "getSkyBlockHomeY"
                                        ) as? Double ?: 0.0
                                        val z = DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.z",
                                            "getSkyBlockHomeZ"
                                        ) as? Double ?: 0.0
                                        val yaw = (DatabaseManager.getSkyBlockData(
                                            islandId,
                                            "$islandId.home.yaw",
                                            "getSkyBlockHomeYaw"
                                        ) as? Double ?: 0.0).toFloat()
                                        val pitch = (DatabaseManager.getSkyBlockData(
                                            islandId,
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
                            DatabaseManager.getUserData("$name.skyblock.page", player, "getSkyblockPage") as? Int ?: 0
                        val plus = page - 1
                        DatabaseManager.setUserData("$name.skyblock.page", player, plus, "setSkyblockPage")
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockMenuGUI? = null
                        inv = HwaSkyBlockMenuGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.next_page"))
                        )
                    ) {
                        val page =
                            DatabaseManager.getUserData("$name.skyblock.page", player, "getSkyblockPage") as? Int ?: 0
                        val plus = page + 1
                        DatabaseManager.setUserData("$name.skyblock.page", player, plus, "setSkyblockPage")
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockMenuGUI? = null
                        inv = HwaSkyBlockMenuGUI(player)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}