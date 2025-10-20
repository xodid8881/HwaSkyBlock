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
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockGlobalUseGUI
import java.util.*

class InvGlobalUseClickEvent : Listener {
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
            player.name
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
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_DOOR"))
                        )
                    ) {
                        val current =
                            DatabaseManager.getSkyBlockData(id.toString(), "isSkyBlockDoor") as? Boolean
                                ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), !current, "setSkyBlockDoor")
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.CHEST"))
                        )
                    ) {
                        val current = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockChest"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), !current, "setSkyBlockChest")
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BARREL"))
                        )
                    ) {
                        val current = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockBarrel"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), !current, "setSkyBlockBarrel")
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.HOPPER"))
                        )
                    ) {
                        val current = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockHopper"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), !current, "setSkyBlockHopper")
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.FURNACE"))
                        )
                    ) {
                        val current = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockFurnace"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !current,
                            "setSkyBlockFurnace"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BLAST_FURNACE"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockBlastFurnace"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockBlastFurnace"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SHULKER_BOX"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockShulkerBox"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockShulkerBox"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_TRAPDOOR"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockTrapdoor"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockTrapdoor"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BUTTON"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockButton"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockButton"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.ANVIL"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockAnvil"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockAnvil"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SWEET_BERRIES"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockFarm"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockFarm"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BEACON"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockBeacon"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockBeacon"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.MINECART"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockMinecart"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockMinecart"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BOAT"))
                        )
                    ) {
                        val currentValue = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockBoat"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !currentValue,
                            "setSkyBlockBoat"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockGlobalUseGUI(player)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                }
            }
        }
    }
}