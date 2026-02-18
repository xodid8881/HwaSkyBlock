package org.hwabaeg.hwaskyblock.events.click

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.inventorys.holder.SharerGuiHolder
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerClickEvent : Listener {

    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        val topIsGuiHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is SharerGuiHolder
        val topIsGuiTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
            '&',
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_setting"))
        )
        if (topIsGuiHolder || topIsGuiTitle) {
            e.isCancelled = true
        }
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            val name = player.name
            var world: World? = player.world
            val world_name = world!!.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val isGuiByHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is SharerGuiHolder
            val isGuiByTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_setting"))
            )
            if (isGuiByHolder || isGuiByTitle) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    val sharerList = DatabaseManager.getShareDataList(id.toString())
                    if (sharerList.isNotEmpty()) {
                        for (key in sharerList) {
                            var display_name = MessageConfig.getString("gui-slot-item-name.sharer_setting.sharer")
                            display_name = Objects.requireNonNull(display_name)?.replace("{name}", key)

                            if (clickitem == ChatColor.translateAlternateColorCodes('&', display_name.toString())) {

                                if (e.click == ClickType.SHIFT_LEFT) {
                                    val player_join =
                                        DatabaseManager.getUserDataByName(
                                            "$key.skyblock.sharer_join.$id",
                                            key,
                                            "getPlayerSharerJoin"
                                        ) as? Boolean ?: true
                                    DatabaseManager.setUserDataByName(
                                        "$key.skyblock.sharer_join.$id",
                                        key,
                                        !player_join,
                                        "setPlayerSharerJoin"
                                    )
                                    player.closeInventory()
                                    Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                                        val inv = HwaSkyBlockSharerGUI(player, id)
                                        inv.open(player)
                                    }, 2L)
                                    return
                                }

                                if (e.click == ClickType.SHIFT_RIGHT) {
                                    val block_break =
                                        DatabaseManager.getShareData("$id", key, "isUseBreak") as? Boolean ?: false
                                    DatabaseManager.setShareData(
                                        "$id",
                                        key,
                                        !block_break,
                                        "setUseBreak"
                                    )
                                    player.closeInventory()
                                    Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                                        val inv = HwaSkyBlockSharerGUI(player, id)
                                        inv.open(player)
                                    }, 2L)
                                    return
                                }

                                if (e.click == ClickType.LEFT) {
                                    val block_place =
                                        DatabaseManager.getShareData("$id", key, "isUsePlace") as? Boolean ?: false
                                    DatabaseManager.setShareData(
                                        "$id",
                                        key,
                                        !block_place,
                                        "setUsePlace"
                                    )
                                    player.closeInventory()
                                    Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                                        val inv = HwaSkyBlockSharerGUI(player, id)
                                        inv.open(player)
                                    }, 2L)
                                    return
                                }

                                if (e.click == ClickType.RIGHT) {
                                    DatabaseManager.setUserData(
                                        "$name.skyblock.setting",
                                        player,
                                        key,
                                        "setPlayerEvent"
                                    )
                                    player.closeInventory()
                                    Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                                        val inv = HwaSkyBlockSharerUseGUI(player, id)
                                        inv.open(player)
                                    }, 2L)
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
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerGUI(player, id)
                            inv.open(player)
                        }, 2L)
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
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerGUI(player, id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                }
            }
        }
    }
}

