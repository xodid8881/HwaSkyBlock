package org.hwabaeg.hwaskyblock.events.click

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
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.io.File
import java.util.*

class InvBuyClickEvent : Listener {
    var Config: FileConfiguration = ConfigManager.Companion.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.Companion.getConfig("message")!!
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
                            val possessionCount = DatabaseManager.getUserData(
                                "$name.skyblock.possession_count",
                                player,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 0
                            val maxCount = Config.getInt("sky-block-max")
                            if (possessionCount >= maxCount) {
                                player.sendMessage(
                                    Prefix + ChatColor.translateAlternateColorCodes(
                                        '&',
                                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.hold_the_maximum"))
                                    )
                                )
                                return
                            }
                            econ.withdrawPlayer(player, buy.toDouble())
                            DatabaseManager.insertSkyBlock(id.toString(), name)
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.name", name, "setSkyBlockName")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.leader", name, "setSkyBlockLeader")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.join", true, "setSkyBlockJoin")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.break", false, "setSkyBlockBreak")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.place", false, "setSkyBlockPlace")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.door", false, "setSkyBlockDoor")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.chest", false, "setSkyBlockChest")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.barrel", false, "setSkyBlockBarrel")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.hopper", false, "setSkyBlockHopper")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.use.furnace",
                                false,
                                "setSkyBlockFurnace"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.use.blast_furnace",
                                false,
                                "setSkyBlockBlastFurnace"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.use.shulker_box",
                                false,
                                "setSkyBlockShulkerBox"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.use.trapdoor",
                                false,
                                "setSkyBlockTrapdoor"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.button", false, "setSkyBlockButton")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.anvil", false, "setSkyBlockAnvil")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.farm", false, "setSkyBlockFarm")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.beacon", false, "setSkyBlockBeacon")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.use.minecart",
                                false,
                                "setSkyBlockMinecart"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.use.boat", false, "setSkyBlockBoat")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.pvp", false, "setSkyBlockPvp")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.welcome_message",
                                "Welcome $name Farm",
                                "setSkyBlockWelcomeMessage"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.home", 0, "setSkyBlockHome")
                            DatabaseManager.setSkyBlockData(id.toString(), "$id.size", size, "setSkyBlockSize")

                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.monster_spawn",
                                true,
                                "setSkyBlockMonsterSpawn"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.animal_spawn",
                                true,
                                "setSkyBlockAnimalSpawn"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.weather",
                                "basic",
                                "setSkyBlockWeather"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.time",
                                "basic",
                                "setSkyBlockTime"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.water_physics",
                                true,
                                "setSkyBlockWaterPhysics"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "$id.setting.lava_physics",
                                true,
                                "setSkyBlockLavaPhysics"
                            )

                            val player = Bukkit.getPlayerExact(name) ?: return
                            val currentCount = DatabaseManager.getUserData(
                                "$name.skyblock.possession_count",
                                player,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 0
                            DatabaseManager.setUserData(
                                "$name.skyblock.possession_count",
                                player,
                                currentCount + 1,
                                "setPlayerPossessionCount"
                            )
                            DatabaseManager.setUserData("$name.skyblock.possession.$id", player, name, null)

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