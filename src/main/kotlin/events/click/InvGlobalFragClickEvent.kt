package org.hwabeag.hwaskyblock.events.click

import database.user.SelectUser
import database.user.UpdateUser
import database.utils.hwaskyblock_skyblock
import database.utils.hwaskyblock_user
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalUseGUI
import java.util.*

class InvGlobalFragClickEvent : Listener {
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
        var Select_Skyblock_List: java.util.HashMap<String?, hwaskyblock_skyblock?> = HashMap<String?, hwaskyblock_skyblock?>()
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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.global_setting"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.join"))
                        )
                    ) {
                        val player_join = SkyBlockConfig.getBoolean("$id.join")
                        if (player_join) {
                            SkyBlockConfig.set("$id.join", false)
                        } else {
                            SkyBlockConfig.set("$id.join", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockGlobalFragGUI? = null
                        inv = HwaSkyBlockGlobalFragGUI(id)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.break"))
                        )
                    ) {
                        val block_break = SkyBlockConfig.getBoolean("$id.break")
                        if (block_break) {
                            SkyBlockConfig.set("$id.break", false)
                        } else {
                            SkyBlockConfig.set("$id.break", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockGlobalFragGUI? = null
                        inv = HwaSkyBlockGlobalFragGUI(id)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.place"))
                        )
                    ) {
                        val block_place = SkyBlockConfig.getBoolean("$id.place")
                        if (block_place) {
                            SkyBlockConfig.set("$id.place", false)
                        } else {
                            SkyBlockConfig.set("$id.place", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockGlobalFragGUI? = null
                        inv = HwaSkyBlockGlobalFragGUI(id)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.use"))
                        )
                    ) {
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_setting.pvp"))
                        )
                    ) {
                        val pvp = SkyBlockConfig.getBoolean("$id.pvp")
                        if (pvp) {
                            SkyBlockConfig.set("$id.pvp", false)
                        } else {
                            SkyBlockConfig.set("$id.pvp", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockGlobalFragGUI? = null
                        inv = HwaSkyBlockGlobalFragGUI(id)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}