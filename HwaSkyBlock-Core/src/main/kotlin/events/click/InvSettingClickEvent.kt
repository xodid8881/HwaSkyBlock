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
import org.hwabaeg.hwaskyblock.inventorys.holder.SettingGuiHolder
import org.hwabaeg.hwaskyblock.inventorys.HwaSkyBlockSettingGUI
import java.util.*

class InvSettingClickEvent : Listener {

    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        val topIsGuiHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is SettingGuiHolder
        val topIsGuiTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
            '&',
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.world_setting"))
        )
        if (topIsGuiHolder || topIsGuiTitle) {
            e.isCancelled = true
        }
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            var world: World? = player.world
            val world_name = world!!.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val isGuiByHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view) is SettingGuiHolder
            val isGuiByTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view) == ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.world_setting"))
            )
            if (isGuiByHolder || isGuiByTitle) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    var item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn"))
                    )
                    if (clickitem == item_name) {
                        val monsterSpawn = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockMonsterSpawn"
                        ) as? Boolean ?: false

                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !monsterSpawn,
                            "setSkyBlockMonsterSpawn"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn"))
                    )
                    if (clickitem == item_name) {
                        val animalSpawn = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockAnimalSpawn"
                        ) as? Boolean ?: false

                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !animalSpawn,
                            "setSkyBlockAnimalSpawn"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather"))
                    )
                    if (clickitem == item_name) {
                        val weather = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockWeather"
                        ) as? String ?: "clear"

                        val nextWeather = when (weather) {
                            "clear" -> "rainy"
                            "rainy" -> "thunder"
                            "thunder" -> "basic"
                            "basic" -> "clear"
                            else -> "clear"
                        }
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            nextWeather,
                            "setSkyBlockWeather"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time"))
                    )
                    if (clickitem == item_name) {
                        val time = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockTime"
                        ) as? String ?: "morn"
                        val nextTime = when (time) {
                            "morn" -> "noon"
                            "noon" -> "evening"
                            "evening" -> "basic"
                            "basic" -> "morn"
                            else -> "morn"
                        }
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            nextTime,
                            "setSkyBlockTime"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics"))
                    )
                    if (clickitem == item_name) {
                        val waterPhysics = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockWaterPhysics"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !waterPhysics,
                            "setSkyBlockWaterPhysics"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics"))
                    )
                    if (clickitem == item_name) {
                        val lavaPhysics = DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "isSkyBlockLavaPhysics"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            !lavaPhysics,
                            "setSkyBlockLavaPhysics"
                        )
                        player.closeInventory()
                        Bukkit.getScheduler().runTaskLater(HwaSkyBlock.plugin, Runnable {
                            val inv = HwaSkyBlockSettingGUI(id)
                            inv.open(player)
                        }, 2L)
                        return
                    }
                }
            }
        }
    }
}
