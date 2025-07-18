package org.hwabeag.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockGlobalUseGUI(player: Player) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            36,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.global_use_list"))
        )
        initItemSetting(player)
    }

    private fun initItemSetting(player: Player) {
        val world = player.world
        val world_name = world.worldFolder.getName()
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val id = number[1]

        val player_door =
            DatabaseManager.getSkyBlockData("$id", "$id.use.door", "isSkyBlockDoor") as? Boolean ?: false
        val player_chest =
            DatabaseManager.getSkyBlockData("$id", "$id.use.chest", "isSkyBlockChest") as? Boolean ?: false
        val player_barrel =
            DatabaseManager.getSkyBlockData("$id", "$id.use.barrel", "isSkyBlockBarrel") as? Boolean ?: false
        val player_hopper =
            DatabaseManager.getSkyBlockData("$id", "$id.use.hopper", "isSkyBlockHopper") as? Boolean ?: false
        val player_furnace =
            DatabaseManager.getSkyBlockData("$id", "$id.use.furnace", "isSkyBlockFurnace") as? Boolean ?: false
        val player_blast_furnace =
            DatabaseManager.getSkyBlockData("$id", "$id.use.blast_furnace", "isSkyBlockBlastFurnace") as? Boolean
                ?: false
        val player_shulker_box =
            DatabaseManager.getSkyBlockData("$id", "$id.use.shulker_box", "isSkyBlockShulkerBox") as? Boolean
                ?: false

        val player_trapdoor =
            DatabaseManager.getSkyBlockData("$id", "$id.use.trapdoor", "isSkyBlockTrapdoor") as? Boolean ?: false
        val player_button =
            DatabaseManager.getSkyBlockData("$id", "$id.use.button", "isSkyBlockButton") as? Boolean ?: false
        val player_anvil =
            DatabaseManager.getSkyBlockData("$id", "$id.use.anvil", "isSkyBlockAnvil") as? Boolean ?: false
        val player_farm =
            DatabaseManager.getSkyBlockData("$id", "$id.use.farm", "isSkyBlockFarm") as? Boolean ?: false
        val player_beacon =
            DatabaseManager.getSkyBlockData("$id", "$id.use.beacon", "isSkyBlockBeacon") as? Boolean ?: false
        val player_minecart =
            DatabaseManager.getSkyBlockData("$id", "$id.use.minecart", "isSkyBlockMinecart") as? Boolean ?: false
        val player_boat =
            DatabaseManager.getSkyBlockData("$id", "$id.use.boat", "isSkyBlockBoat") as? Boolean ?: false


        var item = ItemStack(Material.OAK_DOOR, 1)
        var itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_DOOR"))
            )
        )
        var loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.OAK_DOOR-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_door) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_DOOR-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_DOOR-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(10, item)

        item = ItemStack(Material.CHEST, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.CHEST"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.CHEST-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_chest) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.CHEST-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.CHEST-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(11, item)

        item = ItemStack(Material.BARREL, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BARREL"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.BARREL-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_barrel) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BARREL-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BARREL-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(12, item)

        item = ItemStack(Material.HOPPER, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.HOPPER"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.HOPPER-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_hopper) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.HOPPER-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.HOPPER-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(13, item)

        item = ItemStack(Material.FURNACE, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.FURNACE"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.FURNACE-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_furnace) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.FURNACE-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.FURNACE-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(14, item)

        item = ItemStack(Material.BLAST_FURNACE, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BLAST_FURNACE"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.BLAST_FURNACE-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_blast_furnace) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BLAST_FURNACE-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BLAST_FURNACE-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(15, item)

        item = ItemStack(Material.SHULKER_BOX, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SHULKER_BOX"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.SHULKER_BOX-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_shulker_box) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SHULKER_BOX-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SHULKER_BOX-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(16, item)

        item = ItemStack(Material.OAK_TRAPDOOR, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_TRAPDOOR"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.OAK_TRAPDOOR-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_trapdoor) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_TRAPDOOR-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_TRAPDOOR-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(19, item)

        item = ItemStack(Material.OAK_BUTTON, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BUTTON"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.OAK_BUTTON-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_button) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BUTTON-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BUTTON-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(20, item)

        item = ItemStack(Material.ANVIL, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.ANVIL"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.ANVIL-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_anvil) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.ANVIL-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.ANVIL-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(21, item)

        item = ItemStack(Material.SWEET_BERRIES, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SWEET_BERRIES"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.SWEET_BERRIES-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_farm) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SWEET_BERRIES-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SWEET_BERRIES-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(22, item)

        item = ItemStack(Material.BEACON, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BEACON"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.BEACON-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_beacon) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BEACON-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BEACON-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(23, item)

        item = ItemStack(Material.MINECART, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.MINECART"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.MINECART-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_minecart) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.MINECART-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.MINECART-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(24, item)

        item = ItemStack(Material.OAK_BOAT, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BOAT"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.global_use_list.OAK_BOAT-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (player_boat) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BOAT-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BOAT-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(25, item)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}