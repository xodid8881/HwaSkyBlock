package org.hwabaeg.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockMenuGUI(player: Player) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            54,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
        )
        initItemSetting(player)
    }

    fun isBedrockPlayer(player: Player): Boolean {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    }

    private fun initItemSetting(player: Player) {
        val name = player.name
        var N = 0
        var Page = 1
        val rawPossession = DatabaseManager.getUserData("$name.skyblock.possession", player, "getSkyblockPossession")
        val possessionData = (rawPossession as? Map<*, *>)?.mapNotNull {
            val key = it.key?.toString()
            val value = it.value as? Boolean
            if (key != null && value == true) key to value else null
        }?.toMap()

        if (!possessionData.isNullOrEmpty()) {
            for ((key, _) in possessionData) {
                val PlayerPage =
                    DatabaseManager.getUserData("$name.skyblock.page", player, "getSkyblockPage") as? Int ?: 1
                if (Page == PlayerPage) {
                    val item = ItemStack(Material.GRASS_BLOCK, 1)
                    val itemMeta = item.itemMeta
                    var display_name: String? = null
                    if (isBedrockPlayer(player)) {
                        var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.geyser_my")
                    } else {
                        var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                    }
                    display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key)
                    itemMeta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name))
                    val loreList = ArrayList<String?>()
                    val world_name = player.world.worldFolder.getName()
                    val number: Array<String?> =
                        world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (number[0] == "HwaSkyBlock") {
                        val id = number[1]
                        if (id == key) {
                            if (isBedrockPlayer(player)) {
                                for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.geyser_my-lore")) {
                                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                                }
                            } else {
                                for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.my-lore")) {
                                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                                }
                            }
                        }
                    }
                    itemMeta?.lore = loreList
                    item.itemMeta = itemMeta
                    inv.setItem(N, item)
                }
                N++
                if (N >= 44) {
                    Page++
                    N = 0
                }
            }
        }
        val sharerData = DatabaseManager.getUserData("$name.skyblock.sharer", player, "getSkyblockSharer") as? Map<*, *>
        if (sharerData != null) {
            for (key in sharerData.keys) {
                val PlayerPage =
                    DatabaseManager.getUserData("$name.skyblock.page", player, "getSkyblockPage") as? Int ?: 0
                if (Page == PlayerPage) {
                    val item = ItemStack(Material.GRASS_BLOCK, 1)
                    val itemMeta = item.itemMeta
                    var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                    display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key.toString())
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