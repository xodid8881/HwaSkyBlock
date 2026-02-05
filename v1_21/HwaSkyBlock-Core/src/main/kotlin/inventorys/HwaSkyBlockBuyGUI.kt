package org.hwabaeg.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class HwaSkyBlockBuyGUI : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    init {
        inv = Bukkit.createInventory(
            null,
            27,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.buy"))
        )
        initItemSetting()
    }

    private fun initItemSetting() {
        for (world_name in Objects.requireNonNull<ConfigurationSection?>(Config.getConfigurationSection("sky-block-world"))
            .getKeys(false)) {
            val item_type = Config.getString("sky-block-world.$world_name.item-type")
            val item_name = Config.getString("sky-block-world.$world_name.item-name")
            val item_slot = Config.getInt("sky-block-world.$world_name.item-slot")
            val loreList = ArrayList<String?>()
            for (lore in Config.getStringList("sky-block-world.$world_name.item-lore")) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
            }
            val item = ItemStack(
                Objects.requireNonNull<Material?>(
                    Material.getMaterial(
                        Objects.requireNonNull<String?>(item_type)
                    )
                ), 1
            )
            val itemMeta = item.itemMeta
            itemMeta?.setDisplayName(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(item_name)
                )
            )
            itemMeta?.lore = loreList
            item.itemMeta = itemMeta
            inv.setItem(item_slot, item)
        }
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}