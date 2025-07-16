package org.hwabeag.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockMenuGUI(player: Player) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!

    init {
        inv = Bukkit.createInventory(
            null,
            54,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
        )
        initItemSetting(player)
    }

    private fun initItemSetting(player: Player) {
        val name = player.name
        var N = 0
        var Page = 1
        if (PlayerConfig.getConfigurationSection("$name.skyblock.possession") != null) {
            for (key in Objects.requireNonNull<ConfigurationSection?>(PlayerConfig.getConfigurationSection("$name.skyblock.possession"))
                .getKeys(false)) {
                val PlayerPage = PlayerConfig.getInt("$name.skyblock.page")
                if (Page == PlayerPage) {
                    val item = ItemStack(Material.GRASS_BLOCK, 1)
                    val itemMeta = item.itemMeta
                    var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                    display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key)
                    itemMeta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name))
                    val loreList = ArrayList<String?>()
                    val world_name = player.world.worldFolder.getName()
                    val number: Array<String?> =
                        world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (number[0] == "HwaSkyBlock") {
                        val id = number[1]
                        if (id == key) {
                            for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.my-lore")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                            }
                        }
                    }
                    for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.sharer-lore")) {
                        loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                    }
                    itemMeta?.lore = loreList
                    item.itemMeta = itemMeta
                    inv.setItem(N, item)
                }
                N = N + 1
                if (N >= 44) {
                    Page = Page + 1
                    N = 0
                }
            }
        }
        if (PlayerConfig.getConfigurationSection("$name.skyblock.sharer") != null) {
            for (key in Objects.requireNonNull<ConfigurationSection?>(PlayerConfig.getConfigurationSection("$name.skyblock.sharer"))
                .getKeys(false)) {
                val PlayerPage = PlayerConfig.getInt("$name.skyblock.page")
                if (Page == PlayerPage) {
                    val item = ItemStack(Material.GRASS_BLOCK, 1)
                    val itemMeta = item.itemMeta
                    var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                    display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key)
                    itemMeta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name))
                    val loreList = ArrayList<String?>()
                    for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.sharer-lore")) {
                        loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                    }
                    itemMeta?.lore = loreList
                    item.itemMeta = itemMeta
                    inv.setItem(N, item)
                }
                N = N + 1
                if (N >= 44) {
                    Page = Page + 1
                    N = 0
                }
            }
        }

        var item = ItemStack(Material.PAPER, 1)
        var itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.previous_page"))
            )
        )
        var loreList = ArrayList<String?>()
        for (lore in MessageConfig.getStringList("gui-slot-item-name.previous_page-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(45, item)


        item = ItemStack(Material.PAPER, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.next_page"))
            )
        )
        loreList = ArrayList<String?>()
        for (lore in MessageConfig.getStringList("gui-slot-item-name.next_page-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(53, item)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}