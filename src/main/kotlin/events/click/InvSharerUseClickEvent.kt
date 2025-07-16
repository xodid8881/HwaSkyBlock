package org.hwabeag.hwaskyblock.events.click

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerUseClickEvent : Listener {

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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_use_list"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    val user_name =
                        DatabaseManager.getUserData("$name.skyblock.setting", player, "getSkyblockSetting") as? String

                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.door",
                                "getSkyblockSharerUseDoor"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.door",
                                false,
                                "setSkyblockSharerUseDoor"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.door",
                                true,
                                "setSkyblockSharerUseDoor"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.CHEST"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.chest",
                                "getSkyblockSharerUseChest"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.chest",
                                false,
                                "setSkyblockSharerUseChest"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.chest",
                                true,
                                "setSkyblockSharerUseChest"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BARREL"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.barrel",
                                "getSkyblockSharerUseBarrel"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.barrel",
                                false,
                                "setSkyblockSharerUseBarrel"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.barrel",
                                true,
                                "setSkyblockSharerUseBarrel"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.HOPPER"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.hopper",
                                "getSkyblockSharerUseHopper"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.hopper",
                                false,
                                "setSkyblockSharerUseHopper"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.hopper",
                                true,
                                "setSkyblockSharerUseHopper"
                            )
                        }

                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.FURNACE"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.furnace",
                                "getSkyblockSharerUseFurnace"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.furnace",
                                false,
                                "setSkyblockSharerUseFurnace"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.furnace",
                                true,
                                "setSkyblockSharerUseFurnace"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.blast_furnace",
                                "getSkyblockSharerUseBlastFurnace"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.blast_furnace",
                                false,
                                "setSkyblockSharerUseBlastFurnace"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.blast_furnace",
                                true,
                                "setSkyblockSharerUseBlastFurnace"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.shulker_box",
                                "getSkyblockSharerUseShulkerBox"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.shulker_box",
                                false,
                                "setSkyblockSharerUseShulkerBox"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.shulker_box",
                                true,
                                "setSkyblockSharerUseShulkerBox"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.trapdoor",
                                "getSkyblockSharerUseTrapdoor"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.trapdoor",
                                false,
                                "setSkyblockSharerUseTrapdoor"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.trapdoor",
                                true,
                                "setSkyblockSharerUseTrapdoor"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.button",
                                "getSkyblockSharerUseButton"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.button",
                                false,
                                "setSkyblockSharerUseButton"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.button",
                                true,
                                "setSkyblockSharerUseButton"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.ANVIL"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.anvil",
                                "getSkyblockSharerUseAnvil"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.anvil",
                                false,
                                "setSkyblockSharerUseAnvil"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.anvil",
                                true,
                                "setSkyblockSharerUseAnvil"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.farm",
                                "getSkyblockSharerUseFarm"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.farm",
                                false,
                                "setSkyblockSharerUseFarm"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.farm",
                                true,
                                "setSkyblockSharerUseFarm"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BEACON"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.beacon",
                                "getSkyblockSharerUseBeacon"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.beacon",
                                false,
                                "setSkyblockSharerUseBeacon"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.beacon",
                                true,
                                "setSkyblockSharerUseBeacon"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.MINECART"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.minecart",
                                "getSkyblockSharerUseMinecart"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.minecart",
                                false,
                                "setSkyblockSharerUseMinecart"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.minecart",
                                true,
                                "setSkyblockSharerUseMinecart"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT"))
                        )
                    ) {
                        if (DatabaseManager.getSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.boat",
                                "getSkyblockSharerUseBoat"
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.boat",
                                false,
                                "setSkyblockSharerUseBoat"
                            )
                        } else {
                            DatabaseManager.setSkyBlockData(
                                "$id",
                                "$id.sharer.$user_name.use.boat",
                                true,
                                "setSkyblockSharerUseBoat"
                            )
                        }
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                    }
                }
            }
        }
    }
}