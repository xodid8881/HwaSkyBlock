package org.hwabaeg.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockGlobalFragGUI(key: String?) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            27,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.global_setting"))
        )
        initItemSetting(key)
    }

    private fun initItemSetting(id: String?) {
        val player_join =
            DatabaseManager.getSkyBlockData(id.toString(), "$id.join", "isSkyBlockJoin") as? Boolean ?: false
        val block_break =
            DatabaseManager.getSkyBlockData(id.toString(), "$id.break", "isSkyBlockBreak") as? Boolean ?: false
        val block_place =
            DatabaseManager.getSkyBlockData(id.toString(), "$id.place", "isSkyBlockPlace") as? Boolean ?: false
        val pvp_place = DatabaseManager.getSkyBlockData(id.toString(), "$id.pvp", "isSkyBlockPvp") as? Boolean ?: false

        var item = ItemStack(Material.SPYGLASS, 1)
        var itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.join"))
            )
        )
        var loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_setting.join-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_join) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.join-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.join-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(11, item)

        item = ItemStack(Material.WOODEN_AXE, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.break"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_setting.break-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (block_break) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.break-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.break-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(12, item)

        item = ItemStack(Material.SCAFFOLDING, 1)
        itemMeta = item.getItemMeta()
        loreList = ArrayList<String?>()
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.place"))
            )
        )
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_setting.place-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (block_place) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.place-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.place-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(13, item)

        item = ItemStack(Material.CHEST, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.use"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_setting.use-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(14, item)

        item = ItemStack(Material.DIAMOND_SWORD, 1)
        itemMeta = item.getItemMeta()
        loreList = ArrayList<String?>()
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.pvp"))
            )
        )
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_setting.pvp-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (pvp_place) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.pvp-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.pvp-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(15, item)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}