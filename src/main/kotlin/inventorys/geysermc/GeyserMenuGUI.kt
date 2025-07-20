package org.hwabeag.hwaskyblock.inventorys.geyser

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.util.*

class GeyserMenuGUI(skyblockId: String) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            27,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.geyser_menu_list"))
        )
        initItemSetting(skyblockId)
    }

    private fun initItemSetting(skyblockId: String) {
        var item = ItemStack(Material.ENDER_EYE, 1)
        var itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_move"))
            )
        )
        var loreList = ArrayList<String?>()
        for (lore in MessageConfig.getStringList("gui-slot-item-name.geyser_menu.sky_world_move-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(14, item)


        item = ItemStack(Material.BOOK, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_global"))
            )
        )
        loreList = ArrayList<String?>()
        for (lore in MessageConfig.getStringList("gui-slot-item-name.geyser_menu.sky_world_global-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(12, item)


        item = ItemStack(Material.BOOK, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.geyser_menu.sky_world_sharer"))
            )
        )
        loreList = ArrayList<String?>()
        for (lore in MessageConfig.getStringList("gui-slot-item-name.geyser_menu.sky_world_sharer-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(16, item)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}