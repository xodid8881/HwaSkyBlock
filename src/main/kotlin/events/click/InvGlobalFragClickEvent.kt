package org.hwabeag.hwaskyblock.events.click

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalUseGUI
import java.util.*

class InvGlobalFragClickEvent : Listener {
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
                        val currentJoin =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.join", "isSkyBlockJoin") as? Boolean
                                ?: true
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.join", !currentJoin, "setSkyBlockJoin")
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
                        val currentBreak =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.break", "isSkyBlockBreak") as? Boolean
                                ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.break", !currentBreak, "setSkyBlockBreak")
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
                        val currentPlace =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.place", "isSkyBlockPlace") as? Boolean
                                ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.place", !currentPlace, "setSkyBlockPlace")
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
                        val currentPvp =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.pvp", "isSkyBlockPvp") as? Boolean
                                ?: false
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.pvp", !currentPvp, "setSkyBlockPvp")
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