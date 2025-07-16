package org.hwabeag.hwaskyblock.events.click

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import java.io.File
import java.util.*

class InvBuyClickEvent : Listener {
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
                        val econ: Economy? = HwaSkyBlock.Companion.economy
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
                            ConfigManager.Companion.saveConfigs()
                            filepath?.let { HwaSkyBlock.Companion.addIsland(player, id, it) }
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
        }
    }
}