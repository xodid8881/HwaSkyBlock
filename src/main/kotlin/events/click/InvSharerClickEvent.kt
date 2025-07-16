package org.hwabeag.hwaskyblock.events.click

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerClickEvent : Listener {

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
        var Select_Skyblock_List: java.util.HashMap<String?, hwaskyblock_skyblock?> =
            HashMap<String?, hwaskyblock_skyblock?>()
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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_setting"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    for (key in Objects.requireNonNull<ConfigurationSection?>(SkyBlockConfig.getConfigurationSection("$id.sharer"))
                        .getKeys(false)) {
                        var display_name = MessageConfig.getString("gui-slot-item-name.sharer_setting.sharer")
                        display_name = Objects.requireNonNull<String?>(display_name).replace("{name}", key)
                        if (clickitem == ChatColor.translateAlternateColorCodes('&', display_name)) {
                            if (e.click == ClickType.SHIFT_LEFT) {
                                val player_join = SkyBlockConfig.getBoolean("$id.sharer.$key.join")
                                if (player_join) {
                                    SkyBlockConfig.set("$id.sharer.$key.join", false)
                                } else {
                                    SkyBlockConfig.set("$id.sharer.$key.join", true)
                                }
                                ConfigManager.Companion.saveConfigs()
                                var inv: HwaSkyBlockSharerGUI? = null
                                inv = HwaSkyBlockSharerGUI(player, id)
                                inv.open(player)
                                return
                            }
                            if (e.click == ClickType.SHIFT_RIGHT) {
                                val block_break = SkyBlockConfig.getBoolean("$id.sharer.$key.break")
                                if (block_break) {
                                    SkyBlockConfig.set("$id.sharer.$key.break", false)
                                } else {
                                    SkyBlockConfig.set("$id.sharer.$key.break", true)
                                }
                                ConfigManager.Companion.saveConfigs()
                                var inv: HwaSkyBlockSharerGUI? = null
                                inv = HwaSkyBlockSharerGUI(player, id)
                                inv.open(player)
                                return
                            }
                            if (e.click == ClickType.LEFT) {
                                val block_place = SkyBlockConfig.getBoolean("$id.sharer.$key.place")
                                if (block_place) {
                                    SkyBlockConfig.set("$id.sharer.$key.place", false)
                                } else {
                                    SkyBlockConfig.set("$id.sharer.$key.place", true)
                                }
                                ConfigManager.Companion.saveConfigs()
                                var inv: HwaSkyBlockSharerGUI? = null
                                inv = HwaSkyBlockSharerGUI(player, id)
                                inv.open(player)
                                return
                            }
                            if (e.click == ClickType.RIGHT) {
                                PlayerConfig.set("$name.skyblock.setting", key)
                                ConfigManager.Companion.saveConfigs()
                                var inv: HwaSkyBlockSharerUseGUI? = null
                                inv = HwaSkyBlockSharerUseGUI(player, key)
                                inv.open(player)
                                return
                            }
                        }
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.previous_page"))
                        )
                    ) {
                        val page = PlayerConfig.getInt("$name.skyblock.page")
                        val plus = page - 1
                        PlayerConfig.set("$name.skyblock.page", plus)
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerGUI? = null
                        inv = HwaSkyBlockSharerGUI(player, id)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.next_page"))
                        )
                    ) {
                        val page = PlayerConfig.getInt("$name.skyblock.page")
                        val plus = page + 1
                        PlayerConfig.set("$name.skyblock.page", plus)
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerGUI? = null
                        inv = HwaSkyBlockSharerGUI(player, id)
                        inv.open(player)
                        return
                    }
                }
            }
        }
    }
}