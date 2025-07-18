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

class HwaSkyBlockSettingGUI(key: String?) : Listener {
    private val inv: Inventory

    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            27,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.world_setting"))
        )
        initItemSetting(key)
    }

    private fun initItemSetting(id: String?) {
        val monster_spawn = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.monster_spawn", "isSkyBlockMonsterSpawn") as? Boolean ?: false
        val animal_spawn = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.animal_spawn", "isSkyBlockAnimalSpawn") as? Boolean ?: false
        val weather = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.weather", "isSkyBlockWeather") as? String ?: "clear"
        val time = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.time", "isSkyBlockTime") as? String ?: "morn"
        val water_physics = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.water_physics", "isSkyBlockWaterPhysics") as? Boolean ?: false
        val lava_physics = DatabaseManager.getSkyBlockData(id.toString(), "$id.setting.lava_physics", "isSkyBlockLavaPhysics") as? Boolean ?: false

        var item = ItemStack(Material.ZOMBIE_HEAD, 1)
        var itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn"))
            )
        )
        var loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.monster_spawn-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (monster_spawn) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(10, item)

        item = ItemStack(Material.PIG_SPAWN_EGG, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.animal_spawn-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (animal_spawn) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(11, item)

        item = ItemStack(Material.SCAFFOLDING, 1)
        itemMeta = item.itemMeta
        loreList = ArrayList<String?>()
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather"))
            )
        )
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.weather-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (weather == "clear") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather-clear"))
                )
            )
        } else if (weather == "rainy") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather-rainy"))
                )
            )
        } else if (weather == "thunder") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather-thunder"))
                )
            )
        } else if (weather == "basic") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather-basic"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(12, item)

        item = ItemStack(Material.CLOCK, 1)
        itemMeta = item.itemMeta
        loreList = ArrayList<String?>()
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time"))
            )
        )
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.time-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (time == "morn") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time-morn"))
                )
            )
        } else if (time == "noon") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time-noon"))
                )
            )
        } else if (time == "evening") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time-evening"))
                )
            )
        } else if (time == "basic") {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time-basic"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(14, item)

        item = ItemStack(Material.WATER_BUCKET, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.water_physics-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (water_physics) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(15, item)

        item = ItemStack(Material.LAVA_BUCKET, 1)
        itemMeta = item.itemMeta
        itemMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics"))
            )
        )
        loreList = ArrayList<String?>()
        for (key in MessageConfig.getStringList("gui-slot-item-name.world_setting.lava_physics-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key))
        }
        if (lava_physics) {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics-true"))
                )
            )
        } else {
            loreList.add(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics-false"))
                )
            )
        }
        itemMeta?.lore = loreList
        item.itemMeta = itemMeta
        inv.setItem(16, item)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}