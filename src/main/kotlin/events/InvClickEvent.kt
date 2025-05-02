package org.hwabeag.hwaskyblock.events

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.milkbowl.vault.economy.Economy
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.*
import java.io.File
import java.util.*

class InvClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockSettingGUI? = null
                        inv = HwaSkyBlockSettingGUI(id)
                        inv.open(player)
                        return
                    }
                }
            }
            if (e.view.title == ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.buy"))
                )
            ) {
                e.isCancelled = true
                val clickitem = e.currentItem?.itemMeta?.displayName
                for (skyblock_name in Objects.requireNonNull<ConfigurationSection?>(Config.getConfigurationSection("sky-block-world"))
                    .getKeys(false)) {
                    val item_name = ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(Config.getString("sky-block-world.$skyblock_name.item-name"))
                    )
                    if (item_name == clickitem) {
                        val buy = Config.getInt("sky-block-world.$skyblock_name.item-buy")
                        val size = Config.getInt("sky-block-world.$skyblock_name.max-size")
                        val filepath = Config.getString("sky-block-world.$skyblock_name.world-filepath")
                        val worldDir =
                            File(Bukkit.getServer().worldContainer, Objects.requireNonNull<String?>(filepath))
                        if (!worldDir.exists()) {
                            player.sendMessage("해당 월드를 찾을 수 없습니다.")
                            return
                        }
                        val count = Config.getInt("sky-block-number")
                        val id = count + 1
                        val econ: Economy? = HwaSkyBlock.economy
                        if (econ!!.has(player, buy.toDouble())) {
                            if (PlayerConfig.getInt("$name.skyblock.possession_count") >= Config.getInt("sky-block-max")) {
                                player.sendMessage(
                                    Prefix + ChatColor.translateAlternateColorCodes(
                                        '&',
                                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.hold_the_maximum"))
                                    )
                                )
                                return
                            }
                            econ.withdrawPlayer(player, buy.toDouble())
                            SkyBlockConfig.set("$id.name", name)
                            SkyBlockConfig.set("$id.leader", name)
                            SkyBlockConfig.set("$id.join", true)
                            SkyBlockConfig.set("$id.break", false)
                            SkyBlockConfig.set("$id.place", false)
                            SkyBlockConfig.set("$id.use.door", false)
                            SkyBlockConfig.set("$id.use.chest", false)
                            SkyBlockConfig.set("$id.use.barrel", false)
                            SkyBlockConfig.set("$id.use.hopper", false)
                            SkyBlockConfig.set("$id.use.furnace", false)
                            SkyBlockConfig.set("$id.use.blast_furnace", false)
                            SkyBlockConfig.set("$id.use.shulker_box", false)
                            SkyBlockConfig.set("$id.use.trapdoor", false)
                            SkyBlockConfig.set("$id.use.button", false)
                            SkyBlockConfig.set("$id.use.anvil", false)
                            SkyBlockConfig.set("$id.use.farm", false)
                            SkyBlockConfig.set("$id.use.beacon", false)
                            SkyBlockConfig.set("$id.use.minecart", false)
                            SkyBlockConfig.set("$id.use.boat", false)
                            SkyBlockConfig.set("$id.pvp", false)
                            SkyBlockConfig.set("$id.welcome_message", "Welcome $name Farm")
                            SkyBlockConfig.set("$id.home", 0)
                            SkyBlockConfig.set("$id.size", size)

                            SkyBlockConfig.set("$id.setting.monster_spawn", true)
                            SkyBlockConfig.set("$id.setting.animal_spawn", true)
                            SkyBlockConfig.set("$id.setting.weather", "basic")
                            SkyBlockConfig.set("$id.setting.time", "basic")
                            SkyBlockConfig.set("$id.setting.water_physics", true)
                            SkyBlockConfig.set("$id.setting.lava_physics", true)

                            PlayerConfig.set(
                                "$name.skyblock.possession_count",
                                PlayerConfig.getInt("$name.skyblock.possession_count") + 1
                            )
                            PlayerConfig.set("$name.skyblock.possession.$id", name)
                            Config.set("sky-block-number", id)
                            ConfigManager.saveConfigs()
                            filepath?.let { HwaSkyBlock.addIsland(player, id, it) }
                            player.sendMessage(
                                Prefix + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.purchase_completed"))
                                )
                            )
                        } else {
                            player.sendMessage(
                                Prefix + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.insufficient_funds"))
                                )
                            )
                        }
                        e.inventory.clear()
                        player.closeInventory()
                        return
                    }
                }
            }
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
                                    ConfigManager.saveConfigs()
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
                    ConfigManager.saveConfigs()
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
                    ConfigManager.saveConfigs()
                    var inv: HwaSkyBlockMenuGUI? = null
                    inv = HwaSkyBlockMenuGUI(player)
                    inv.open(player)
                    return
                }
            }
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalFragGUI? = null
                        inv = HwaSkyBlockGlobalFragGUI(id)
                        inv.open(player)
                        return
                    }
                }
            }
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
                                ConfigManager.saveConfigs()
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
                                ConfigManager.saveConfigs()
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
                                ConfigManager.saveConfigs()
                                var inv: HwaSkyBlockSharerGUI? = null
                                inv = HwaSkyBlockSharerGUI(player, id)
                                inv.open(player)
                                return
                            }
                            if (e.click == ClickType.RIGHT) {
                                PlayerConfig.set("$name.skyblock.setting", key)
                                ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockSharerGUI? = null
                        inv = HwaSkyBlockSharerGUI(player, id)
                        inv.open(player)
                        return
                    }
                }
            }
            if (e.view.title == ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.global_use_list"))
                )
            ) {
                e.isCancelled = true
                if (number[0] == "HwaSkyBlock") {
                    val id = number[1]
                    val clickitem = e.currentItem?.itemMeta?.displayName
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_DOOR"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.door")) {
                            SkyBlockConfig.set("$id.use.door", false)
                        } else {
                            SkyBlockConfig.set("$id.use.door", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.CHEST"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.chest")) {
                            SkyBlockConfig.set("$id.use.chest", false)
                        } else {
                            SkyBlockConfig.set("$id.use.chest", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BARREL"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.barrel")) {
                            SkyBlockConfig.set("$id.use.barrel", false)
                        } else {
                            SkyBlockConfig.set("$id.use.barrel", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.HOPPER"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.hopper")) {
                            SkyBlockConfig.set("$id.use.hopper", false)
                        } else {
                            SkyBlockConfig.set("$id.use.hopper", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.FURNACE"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.furnace")) {
                            SkyBlockConfig.set("$id.use.furnace", false)
                        } else {
                            SkyBlockConfig.set("$id.use.furnace", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BLAST_FURNACE"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.blast_furnace")) {
                            SkyBlockConfig.set("$id.use.blast_furnace", false)
                        } else {
                            SkyBlockConfig.set("$id.use.blast_furnace", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SHULKER_BOX"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.shulker_box")) {
                            SkyBlockConfig.set("$id.use.shulker_box", false)
                        } else {
                            SkyBlockConfig.set("$id.use.shulker_box", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_TRAPDOOR"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.trapdoor")) {
                            SkyBlockConfig.set("$id.use.trapdoor", false)
                        } else {
                            SkyBlockConfig.set("$id.use.trapdoor", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BUTTON"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.button")) {
                            SkyBlockConfig.set("$id.use.button", false)
                        } else {
                            SkyBlockConfig.set("$id.use.button", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.ANVIL"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.anvil")) {
                            SkyBlockConfig.set("$id.use.anvil", false)
                        } else {
                            SkyBlockConfig.set("$id.use.anvil", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.SWEET_BERRIES"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.farm")) {
                            SkyBlockConfig.set("$id.use.farm", false)
                        } else {
                            SkyBlockConfig.set("$id.use.farm", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.BEACON"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.beacon")) {
                            SkyBlockConfig.set("$id.use.beacon", false)
                        } else {
                            SkyBlockConfig.set("$id.use.beacon", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.MINECART"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.minecart")) {
                            SkyBlockConfig.set("$id.use.minecart", false)
                        } else {
                            SkyBlockConfig.set("$id.use.minecart", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                    if (clickitem == ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.global_use_list.OAK_BOAT"))
                        )
                    ) {
                        if (SkyBlockConfig.getBoolean("$id.use.boat")) {
                            SkyBlockConfig.set("$id.use.boat", false)
                        } else {
                            SkyBlockConfig.set("$id.use.boat", true)
                        }
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockGlobalUseGUI? = null
                        inv = HwaSkyBlockGlobalUseGUI(player)
                        inv.open(player)
                        return
                    }
                }
            }
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
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
                        ConfigManager.saveConfigs()
                        var inv: HwaSkyBlockSharerUseGUI? = null
                        inv = HwaSkyBlockSharerUseGUI(player, user_name)
                        inv.open(player)
                    }
                }
            }
        }
    }
}