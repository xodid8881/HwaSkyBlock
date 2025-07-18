package org.hwabeag.hwaskyblock.events.click

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalUseGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSettingGUI
import java.util.*

class InvSettingClickEvent : Listener {

    var Config: FileConfiguration = ConfigManager.Companion.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.Companion.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    fun isBedrockPlayer(player: Player): Boolean {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        if (e.currentItem != null) {
            val player = e.whoClicked as Player
            val name = player.name
            var world: World? = player.world
            val world_name = world!!.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (e.view.title == ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.world_setting"))
                )
            ) {
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
                            "$id.setting.monster_spawn",
                            "isSkyBlockMonsterSpawn"
                        ) as? Boolean ?: false

                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            "$id.setting.monster_spawn",
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
                            "$id.setting.animal_spawn",
                            "isSkyBlockAnimalSpawn"
                        ) as? Boolean ?: false

                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            "$id.setting.animal_spawn",
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
                            "$id.setting.weather",
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
                            "$id.setting.weather",
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
                            "$id.setting.time",
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
                            "$id.setting.time",
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
                            "$id.setting.water_physics",
                            "isSkyBlockWaterPhysics"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            "$id.setting.water_physics",
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
                            "$id.setting.lava_physics",
                            "isSkyBlockLavaPhysics"
                        ) as? Boolean ?: false
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            "$id.setting.lava_physics",
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