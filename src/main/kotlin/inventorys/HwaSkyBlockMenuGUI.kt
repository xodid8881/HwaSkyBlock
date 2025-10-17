package org.hwabaeg.hwaskyblock.inventorys

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.util.*

class HwaSkyBlockMenuGUI(player: Player) : Listener {
    private val inv: Inventory
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!

    init {
        inv = Bukkit.createInventory(
            null,
            54,
            Objects.requireNonNull<String?>(MessageConfig.getString("gui-name.sky_block_menu_list"))
        )
        initItemSetting(player)
    }

    private fun initItemSetting(player: Player) {
        val name = player.name
        var slotIndex = 0
        val jsonParser = JSONParser()

        val rawPossession = DatabaseManager.getUserData("$name.skyblock.possession", player, "getPlayerPossession")
        val possessionData: Map<String, Boolean> = when (rawPossession) {
            is Map<*, *> -> rawPossession.mapNotNull {
                val key = it.key?.toString()
                val value = it.value as? Boolean
                if (key != null && value == true) key to true else null
            }.toMap()

            is String -> {
                try {
                    val json = jsonParser.parse(rawPossession) as JSONObject
                    val possessionObj = json["possession"] as? JSONObject
                    possessionObj?.mapNotNull { (k, v) ->
                        val key = k?.toString()
                        val value = (v as? Boolean) == true
                        if (key != null && value) key to true else null
                    }?.toMap() ?: emptyMap()
                } catch (e: Exception) {
                    Bukkit.getLogger().warning("[HwaSkyBlock] possession JSON parse error: ${e.message}")
                    emptyMap()
                }
            }

            else -> emptyMap()
        }

        val rawSharer = DatabaseManager.getUserData("$name.skyblock.sharer", player, "getPlayerSharer")
        val sharerData: Map<String, Boolean> = when (rawSharer) {
            is Map<*, *> -> rawSharer.mapNotNull {
                val key = it.key?.toString()
                val value = it.value as? Boolean
                if (key != null && value == true) key to true else null
            }.toMap()

            is String -> {
                try {
                    val json = jsonParser.parse(rawSharer) as JSONObject
                    val sharerObj = json["sharer"] as? JSONObject
                    sharerObj?.mapNotNull { (k, v) ->
                        val key = k?.toString()
                        val value = (v as? Boolean) == true
                        if (key != null && value) key to true else null
                    }?.toMap() ?: emptyMap()
                } catch (e: Exception) {
                    Bukkit.getLogger().warning("[HwaSkyBlock] sharer JSON parse error: ${e.message}")
                    emptyMap()
                }
            }

            else -> emptyMap()
        }

        if (possessionData.isNotEmpty()) {
            for ((key, _) in possessionData) {
                if (slotIndex >= 44) break

                val item = ItemStack(Material.GRASS_BLOCK, 1)
                val itemMeta = item.itemMeta

                var displayName = if (HwaSkyBlock.isBedrockPlayer(player)) {
                    MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.geyser_my")
                } else {
                    MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my")
                }
                displayName = displayName?.replace("{number}", key) ?: key
                itemMeta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName))

                val loreList = mutableListOf<String>()
                val worldName = player.world.worldFolder.name
                val parts = worldName.split(".")
                if (parts.size >= 2 && parts[0] == "HwaSkyBlock") {
                    val id = parts[1]
                    if (id == key) {
                        val lorePath = if (HwaSkyBlock.isBedrockPlayer(player)) {
                            "gui-slot-item-name.sky_block_menu_list.geyser_my-lore"
                        } else {
                            "gui-slot-item-name.sky_block_menu_list.my-lore"
                        }
                        for (lore in MessageConfig.getStringList(lorePath)) {
                            loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                        }
                    }
                }

                itemMeta?.lore = loreList
                item.itemMeta = itemMeta
                inv.setItem(slotIndex, item)
                slotIndex++
            }
        }

        if (sharerData.isNotEmpty()) {
            for ((key, _) in sharerData) {
                if (slotIndex >= 44) break

                val item = ItemStack(Material.GRASS_BLOCK, 1)
                val itemMeta = item.itemMeta

                var displayName = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer")
                displayName = displayName?.replace("{number}", key) ?: key
                itemMeta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName))

                val loreList = mutableListOf<String>()
                for (lore in MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.sharer-lore")) {
                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore))
                }
                itemMeta?.lore = loreList
                item.itemMeta = itemMeta
                inv.setItem(slotIndex, item)
                slotIndex++
            }
        }

        val prevItem = ItemStack(Material.PAPER, 1)
        val prevMeta = prevItem.itemMeta
        prevMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.previous_page"))
            )
        )
        val prevLore = MessageConfig.getStringList("gui-slot-item-name.previous_page-lore").map {
            ChatColor.translateAlternateColorCodes('&', it)
        }
        prevMeta?.lore = prevLore
        prevItem.itemMeta = prevMeta
        inv.setItem(45, prevItem)

        val nextItem = ItemStack(Material.PAPER, 1)
        val nextMeta = nextItem.itemMeta
        nextMeta?.setDisplayName(
            ChatColor.translateAlternateColorCodes(
                '&',
                Objects.requireNonNull<String?>(MessageConfig.getString("gui-slot-item-name.next_page"))
            )
        )
        val nextLore = MessageConfig.getStringList("gui-slot-item-name.next_page-lore").map {
            ChatColor.translateAlternateColorCodes('&', it)
        }
        nextMeta?.lore = nextLore
        nextItem.itemMeta = nextMeta
        inv.setItem(53, nextItem)
    }

    fun open(player: Player) {
        player.openInventory(inv)
    }
}
