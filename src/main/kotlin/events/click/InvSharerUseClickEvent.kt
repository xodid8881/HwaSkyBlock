package org.hwabaeg.hwaskyblock.events.click

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerUseClickEvent : Listener {

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
                        DatabaseManager.getUserData("$name.skyblock.setting", player, "getSkyblockSetting").toString()

                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.door",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.door",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.door",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.CHEST"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.chest",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.chest",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.chest",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BARREL"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.barrel",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.barrel",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.barrel",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.HOPPER"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.hopper",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.hopper",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.hopper",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.FURNACE"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.furnace",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.furnace",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.furnace",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.blast_furnace",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.blast_furnace",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.blast_furnace",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.shulker_box",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.shulker_box",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.shulker_box",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.trapdoor",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.trapdoor",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.trapdoor",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.button",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.button",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.button",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.ANVIL"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.anvil",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.anvil",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.anvil",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.farm",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.farm",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.farm",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BEACON"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.beacon",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.beacon",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.beacon",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.MINECART"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.minecart",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.minecart",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.minecart",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT"))
                        )
                    ) {
                        if (DatabaseManager.getShareData(
                                "$id",
                                user_name,
                                "use.boat",
                                null
                            ) as? Boolean ?: false
                        ) {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.boat",
                                false,
                                null
                            )
                        } else {
                            DatabaseManager.setShareData(
                                "$id",
                                user_name,
                                "use.boat",
                                true,
                                null
                            )
                        }
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSharerUseGUI(player, user_name)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                }
            }
        }
    }
}
