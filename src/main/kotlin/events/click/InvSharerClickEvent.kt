package org.hwabeag.hwaskyblock.events.click

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerClickEvent : Listener {

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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_setting"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    val sharerData = DatabaseManager.getSharePermissionsMap(id.toString()) as? Map<*, *>
                    if (sharerData != null) {
                        for (key in sharerData.keys) {
                            var display_name = MessageConfig.getString("gui-slot-item-name.sharer_setting.sharer")
                            display_name = Objects.requireNonNull(display_name)?.replace("{name}", key.toString())

                            if (clickitem == ChatColor.translateAlternateColorCodes('&', display_name.toString())) {

                                if (e.click == ClickType.SHIFT_LEFT) {
                                    val player_join = (sharerData[key] as? Map<*, *>)?.get("join") as? Boolean ?: false
                                    DatabaseManager.setShareData(
                                        "$id",
                                        key.toString(),
                                        "join",
                                        !player_join,
                                        "setSkyblockSharerJoin"
                                    )
                                    val inv = HwaSkyBlockSharerGUI(player, id)
                                    inv.open(player)
                                    return
                                }

                                if (e.click == ClickType.SHIFT_RIGHT) {
                                    val block_break = (sharerData[key] as? Map<*, *>)?.get("break") as? Boolean ?: false
                                    DatabaseManager.setShareData(
                                        "$id",
                                        key.toString(),
                                        "break",
                                        !block_break,
                                        "setSkyblockSharerBreak"
                                    )
                                    val inv = HwaSkyBlockSharerGUI(player, id)
                                    inv.open(player)
                                    return
                                }

                                if (e.click == ClickType.LEFT) {
                                    val block_place = (sharerData[key] as? Map<*, *>)?.get("place") as? Boolean ?: false
                                    DatabaseManager.setShareData(
                                        "$id",
                                        key.toString(),
                                        "place",
                                        !block_place,
                                        "setSkyblockSharerPlace"
                                    )
                                    val inv = HwaSkyBlockSharerGUI(player, id)
                                    inv.open(player)
                                    return
                                }

                                if (e.click == ClickType.RIGHT) {
                                    DatabaseManager.setUserData(
                                        "$name.skyblock.setting",
                                        player,
                                        key.toString(),
                                        "setSkyblockSetting"
                                    )
                                    val inv = HwaSkyBlockSharerUseGUI(player, key.toString())
                                    inv.open(player)
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
                        var inv: HwaSkyBlockSharerGUI? = null
                        inv = HwaSkyBlockSharerGUI(player, id)
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
                        var inv: HwaSkyBlockSharerGUI? = null
                        inv = HwaSkyBlockSharerGUI(player, id)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}