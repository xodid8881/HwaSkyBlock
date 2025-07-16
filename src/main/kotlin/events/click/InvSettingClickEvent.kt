package org.hwabeag.hwaskyblock.events.click

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSettingGUI
import java.util.*

class InvSettingClickEvent : Listener {

    var Config: FileConfiguration = ConfigManager.Companion.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.Companion.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.Companion.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.Companion.getConfig("player")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    var User_Select: SelectUser = SelectUser()
    var Update_User: UpdateUser = UpdateUser()

    fun isBedrockPlayer(player: Player): Boolean {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    }

    companion object {
        var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> = HashMap<String?, hwaskyblock_skyblock?>()
        var Select_User_List: HashMap<String?, hwaskyblock_user?> = HashMap<String?, hwaskyblock_user?>()
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
                        val monster_spawn = SkyBlockConfig.getBoolean("$id.setting.monster_spawn")
                        if (monster_spawn) {
                            SkyBlockConfig.set("$id.setting.monster_spawn", false)
                        } else {
                            SkyBlockConfig.set("$id.setting.monster_spawn", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn"))
                    )
                    if (clickitem == item_name) {
                        val animal_spawn = SkyBlockConfig.getBoolean("$id.setting.animal_spawn")
                        if (animal_spawn) {
                            SkyBlockConfig.set("$id.setting.animal_spawn", false)
                        } else {
                            SkyBlockConfig.set("$id.setting.animal_spawn", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.weather"))
                    )
                    if (clickitem == item_name) {
                        val weather = SkyBlockConfig.getString("$id.setting.weather")
                        if (weather == "clear") {
                            SkyBlockConfig.set("$id.setting.weather", "rainy")
                        } else if (weather == "rainy") {
                            SkyBlockConfig.set("$id.setting.weather", "thunder")
                        } else if (weather == "thunder") {
                            SkyBlockConfig.set("$id.setting.weather", "basic")
                        } else if (weather == "basic") {
                            SkyBlockConfig.set("$id.setting.weather", "clear")
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.time"))
                    )
                    if (clickitem == item_name) {
                        val time = SkyBlockConfig.getString("$id.setting.time")
                        if (time == "morn") {
                            SkyBlockConfig.set("$id.setting.time", "noon")
                        } else if (time == "noon") {
                            SkyBlockConfig.set("$id.setting.time", "evening")
                        } else if (time == "evening") {
                            SkyBlockConfig.set("$id.setting.time", "basic")
                        } else if (time == "basic") {
                            SkyBlockConfig.set("$id.setting.time", "morn")
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics"))
                    )
                    if (clickitem == item_name) {
                        val water_physics = SkyBlockConfig.getBoolean("$id.setting.water_physics")
                        if (water_physics) {
                            SkyBlockConfig.set("$id.setting.water_physics", false)
                        } else {
                            SkyBlockConfig.set("$id.setting.water_physics", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                    item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics"))
                    )
                    if (clickitem == item_name) {
                        val lava_physics = SkyBlockConfig.getBoolean("$id.setting.lava_physics")
                        if (lava_physics) {
                            SkyBlockConfig.set("$id.setting.lava_physics", false)
                        } else {
                            SkyBlockConfig.set("$id.setting.lava_physics", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}