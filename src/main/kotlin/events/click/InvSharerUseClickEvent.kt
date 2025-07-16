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
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerUseGUI
import java.util.*

class InvSharerUseClickEvent : Listener {

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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sharer_use_list"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    val user_name = PlayerConfig.getString("$name.skyblock.setting")
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.door")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.door", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.door", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.CHEST"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.chest")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.chest", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.chest", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BARREL"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.barrel")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.barrel", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.barrel", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.HOPPER"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.hopper")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.hopper", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.hopper", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.FURNACE"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.furnace")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.furnace", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.furnace", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.blast_furnace")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.blast_furnace", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.blast_furnace", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.shulker_box")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.shulker_box", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.shulker_box", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.trapdoor")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.trapdoor", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.trapdoor", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.button")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.button", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.button", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.ANVIL"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.anvil")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.anvil", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.anvil", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.farm")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.farm", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.farm", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.BEACON"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.beacon")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.beacon", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.beacon", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.MINECART"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.minecart")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.minecart", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.minecart", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.sharer.$user_name.use.boat")) {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.boat", false)
                        } else {
                            SkyBlockConfig.set("$id.sharer.$user_name.use.boat", true)
                        }
                        ConfigManager.Companion.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                    }
                }
            }
        }
    }
}