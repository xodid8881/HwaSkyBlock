package org.hwabaeg.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockSharerGUI(player: Player, key: String?) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            54,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_setting"))
        )
        initItemSetting(player, key)
    }

    private fun getHead(player: Player, name: String): ItemStack {
        val world = player.world
        val world_name = world.worldFolder.getName()
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val id = number[1]

        val player_join =
            DatabaseManager.getShareData("$id", name, "can_join", null) as? Boolean ?: false
        val block_break =
            DatabaseManager.getShareData("$id", name, "can_break", null) as? Boolean ?: false
        val block_place =
            DatabaseManager.getShareData("$id", name, "can_place", null) as? Boolean ?: false

        val item = ItemStack(Material.PLAYER_HEAD, 1, 3.toShort())
        val skull = item.itemMeta as SkullMeta
        var display_name = MessageConfig.getString("gui-slot-item-name.sharer_setting.sharer")
        display_name = Objects.requireNonNull<String?>(display_name).replace("{name}", name)
        skull.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name))
        skull.owner = name
        val loreList = ArrayList<String?>()
        val player_exact = Bukkit.getServer().getPlayerExact(name)
        if (player_exact != null) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.online-lore"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.offline-lore"))
                )
            )
        }

        loreList.add("")
        loreList.add(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.join"))
            )
        )
        if (player_join) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.join-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.join-false"))
                )
            )
        }

        loreList.add("")
        loreList.add(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.break"))
            )
        )
        if (block_break) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.break-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.break-false"))
                )
            )
        }

        loreList.add("")
        loreList.add(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.place"))
            )
        )
        if (block_place) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.place-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.place-false"))
                )
            )
        }

        loreList.add("")
        loreList.add(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_setting.use"))
            )
        )

        skull.lore = loreList
        item.itemMeta = skull
        return item
    }

    private fun initItemSetting(player: Player, id: String?) {
        val name = player.name
        var N = 0
        var Page = 1
        val sharerData = DatabaseManager.getSharePermissionsMap("$id") as? Map<*, *>
        if (sharerData != null) {
            for (key in sharerData.keys) {
                val PlayerPage =
                    DatabaseManager.getUserData("$name.skyblock.page", player, "getSkyblockPage") as? Int ?: 0
                if (Page == PlayerPage) {
                    val item = getHead(player, key.toString())
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
        itemMeta?.setLore(loreList)
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
