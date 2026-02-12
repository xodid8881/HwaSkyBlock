package org.hwabaeg.hwaskyblock.events.click

import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.world.SkyblockWorldManager
import java.io.File
import java.util.*

class InvBuyClickEvent : Listener {

    private val Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    private val MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    private val Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")).toString()
    )

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.clickedInventory == null || e.currentItem == null) return

        if (e.view.getTitle() != ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull(MessageConfig.getString("gui-name.buy")).toString()
            )
        ) return

        e.isCancelled = true

        val clickItemName = e.currentItem?.itemMeta?.displayName ?: return
        val name = player.name

        val section: ConfigurationSection =
            Config.getConfigurationSection("sky-block-world") ?: return

        for (skyblockName in section.getKeys(false)) {

            val itemName = ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull(Config.getString("sky-block-world.$skyblockName.item-name")).toString()
            )

            if (itemName != clickItemName) continue

            val buy = Config.getInt("sky-block-world.$skyblockName.buy")
            val size = Config.getInt("sky-block-world.$skyblockName.max-size")
            val filepath = Config.getString("sky-block-world.$skyblockName.world-filepath")

            val templateDir = filepath?.let { File(it) }
            if (templateDir == null || !templateDir.exists()) {
                player.sendMessage("§c섬 템플릿을 찾을 수 없습니다.")
                return
            }

            val econ: Economy = HwaSkyBlock.economy ?: run {
                player.sendMessage("§c경제 시스템을 찾을 수 없습니다.")
                return
            }

            if (!econ.has(player, buy.toDouble())) {
                player.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull(
                            MessageConfig.getString("message-event.insufficient_funds")
                        ).toString()
                    )
                )
                return
            }

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
                        Objects.requireNonNull(
                            MessageConfig.getString("message-event.hold_the_maximum")
                        ).toString()
                    )
                )
                return
            }

            econ.withdrawPlayer(player, buy.toDouble())

            val id = Config.getInt("sky-block-number") + 1

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
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockFurnace")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBlastFurnace")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockShulkerBox")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockTrapdoor")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockButton")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockAnvil")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockFarm")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBeacon")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockMinecart")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockBoat")
            DatabaseManager.setSkyBlockData(id.toString(), false, "setSkyBlockPvp")
            DatabaseManager.setSkyBlockData(
                id.toString(),
                "Welcome $name Farm",
                "setSkyBlockWelcomeMessage"
            )
            DatabaseManager.setSkyBlockData(id.toString(), 0, "setSkyBlockHome")
            DatabaseManager.setSkyBlockData(id.toString(), size, "setSkyBlockSize")
            DatabaseManager.setSkyBlockData(id.toString(), true, "setSkyBlockMonsterSpawn")
            DatabaseManager.setSkyBlockData(id.toString(), true, "setSkyBlockAnimalSpawn")
            DatabaseManager.setSkyBlockData(id.toString(), "basic", "setSkyBlockWeather")
            DatabaseManager.setSkyBlockData(id.toString(), "basic", "setSkyBlockTime")
            DatabaseManager.setSkyBlockData(id.toString(), true, "setSkyBlockWaterPhysics")
            DatabaseManager.setSkyBlockData(id.toString(), true, "setSkyBlockLavaPhysics")

            DatabaseManager.setUserData(
                "$name.skyblock.possession.$id",
                player,
                true,
                "setPlayerPossession"
            )
            DatabaseManager.setUserData(
                "$name.skyblock.possession_count",
                player,
                possessionCount + 1,
                "setPlayerPossessionCount"
            )

            try {
                SkyblockWorldManager.addIsland(player, id, filepath)
            } catch (ex: Exception) {

                DatabaseManager.DeleteSkyBlock(id.toString())
                DatabaseManager.setUserData(
                    "$name.skyblock.possession.$id",
                    player,
                    null,
                    "setPlayerPossession"
                )

                player.sendMessage("§c섬 생성에 실패했습니다. 관리자에게 문의하세요.")
                return
            }

            Config.set("sky-block-number", id)
            ConfigManager.saveConfigs()

            player.sendMessage(
                Prefix + ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull(
                        MessageConfig.getString("message-event.purchase_completed")
                    ).toString()
                )
            )

            e.inventory.clear()
            player.closeInventory()
            return
        }
    }
}
