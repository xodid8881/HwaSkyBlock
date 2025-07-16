package org.hwabeag.hwaskyblock.events.click

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
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
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockMenuGUI
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI
import java.util.*

class InvMenuClickEvent : Listener {
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
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
                )
            ) {
                e.isCancelled = true
                val clickitem = e.currentItem?.getItemMeta()?.getDisplayName()
                if (PlayerConfig.getConfigurationSection("$name.skyblock.possession") != null) {
                    for (key in Objects.requireNonNull<ConfigurationSection?>(PlayerConfig.getConfigurationSection("$name.skyblock.possession"))
                        .getKeys(false)) {
                        var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                        display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key)
                        if (clickitem == ChatColor.translateAlternateColorCodes('&', display_name)) {
                            if (e.click == ClickType.SHIFT_LEFT) {
                                if (SkyBlockConfig.getString("$key.leader") == name) {
                                    var inv: HwaSkyBlockGlobalFragGUI? = null
                                    inv = HwaSkyBlockGlobalFragGUI(key)
                                    inv.open(player)
                                } else {
                                    e.inventory.clear()
                                    player.closeInventory()
                                    val message = ChatColor.translateAlternateColorCodes(
                                        '&',
                                        Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                                    )
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                                }
                                return
                            }
                            if (e.click == ClickType.SHIFT_RIGHT) {
                                if (SkyBlockConfig.getString("$key.leader") == name) {
                                    PlayerConfig.set("$name.skyblock.setting", key)
                                    ConfigManager.Companion.saveConfigs()
                                    var inv: HwaSkyBlockSharerGUI? = null
                                    inv = HwaSkyBlockSharerGUI(player, key)
                                    inv.open(player)
                                } else {
                                    e.inventory.clear()
                                    player.closeInventory()
                                    val message = ChatColor.translateAlternateColorCodes(
                                        '&',
                                        Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                                    )
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
                                }
                                return
                            }
                            if (e.click == ClickType.LEFT) {
                                e.inventory.clear()
                                player.closeInventory()
                                if (SkyBlockConfig.getInt("$key.home") == 0) {
                                    val worldPath = "worlds/HwaSkyBlock.$key"
                                    world = Bukkit.getServer().getWorld(worldPath)
                                    if (world == null) {
                                        world = WorldCreator(worldPath).createWorld()
                                        val location = Objects.requireNonNull<World?>(world).spawnLocation
                                        player.teleport(location)
                                        return
                                    }
                                    val location = Objects.requireNonNull<World?>(world).spawnLocation
                                    player.teleport(location)
                                    return
                                } else {
                                    val worldPath = "worlds/HwaSkyBlock.$key"
                                    world = Bukkit.getServer().getWorld(worldPath)
                                    if (world == null) {
                                        val createWorld = WorldCreator(worldPath).createWorld()
                                        val location = Objects.requireNonNull<World?>(createWorld).spawnLocation
                                        player.teleport(location)
                                        return
                                    } else {
                                        val location = Objects.requireNonNull<World?>(world).spawnLocation
                                        player.teleport(location)
                                    }
                                    val location: Location? = SkyBlockConfig.getLocation("$key.home")
                                    player.teleport(Objects.requireNonNull<Location?>(location))
                                    return
                                }
                            }
                        }
                    }
                }
                if (PlayerConfig.getConfigurationSection("$name.skyblock.sharer") != null) {
                    for (key in Objects.requireNonNull<ConfigurationSection?>(PlayerConfig.getConfigurationSection("$name.skyblock.sharer"))
                        .getKeys(false)) {
                        var display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                        display_name = Objects.requireNonNull<String?>(display_name).replace("{number}", key)
                        if (clickitem == ChatColor.translateAlternateColorCodes('&', display_name)) {
                            if (e.click == ClickType.LEFT) {
                                e.inventory.clear()
                                player.closeInventory()
                                if (SkyBlockConfig.getInt("$key.home") == 0) {
                                    val worldPath = "worlds/HwaSkyBlock.$key"
                                    world = WorldCreator.name(worldPath).createWorld()
                                    val location = Objects.requireNonNull<World?>(world).spawnLocation
                                    player.teleport(Objects.requireNonNull<Location?>(location))
                                } else {
                                    val location: Location? = SkyBlockConfig.getLocation("$key.home")
                                    player.teleport(Objects.requireNonNull<Location?>(location))
                                }
                                return
                            }
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
                    var inv: HwaSkyBlockMenuGUI? = null
                    inv = HwaSkyBlockMenuGUI(player)
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
                    var inv: HwaSkyBlockMenuGUI? = null
                    inv = HwaSkyBlockMenuGUI(player)
                    inv.open(player)
                    return
                }
            }
        }
    }
}