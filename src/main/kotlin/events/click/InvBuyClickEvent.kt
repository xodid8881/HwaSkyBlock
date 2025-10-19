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
            world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
                            DatabaseManager.setSkyBlockData(id.toString(), name, "setSkyBlockName")
                            DatabaseManager.setSkyBlockData(id.toString(), name, "setSkyBlockLeader")
                            DatabaseManager.setSkyBlockData(id.toString(), true, "setSkyBlockJoin")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBreak")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockPlace")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockDoor")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockChest")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBarrel")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockHopper")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                false,
                                "setSkyBlockFurnace"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                false,
                                "setSkyBlockBlastFurnace"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                false,
                                "setSkyBlockShulkerBox"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                false,
                                "setSkyBlockTrapdoor"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockButton")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockAnvil")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockFarm")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBeacon")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                false,
                                "setSkyBlockMinecart"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBoat")
                            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockPvp")
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "Welcome $name Farm",
                                "setSkyBlockWelcomeMessage"
                            )
                            DatabaseManager.setSkyBlockData(id.toString(), 0, "setSkyBlockHome")
                            DatabaseManager.setSkyBlockData(id.toString(), size, "setSkyBlockSize")

                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                true,
                                "setSkyBlockMonsterSpawn"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                true,
                                "setSkyBlockAnimalSpawn"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "basic",
                                "setSkyBlockWeather"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                "basic",
                                "setSkyBlockTime"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
                                true,
                                "setSkyBlockWaterPhysics"
                            )
                            DatabaseManager.setSkyBlockData(
                                id.toString(),
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
                            DatabaseManager.setUserData(
                                "$name.skyblock.possession.$id",
                                player,
                                true,
                                "setPlayerPossession"
                            )

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