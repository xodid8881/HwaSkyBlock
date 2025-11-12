package org.hwabaeg.hwaskyblock.addon.ranking

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.skyblock.MySQLSkyblockDAO
import org.hwabaeg.hwaskyblock.database.sqlite.skyblock.SkyblockDAO

class SkyblockRankingAddon : HwaSkyBlockAddon, Listener {

    private lateinit var plugin: JavaPlugin
    private var cachedRanking: List<Triple<String, Int, String>> = emptyList()
    private val rankingSlots = listOf(10, 12, 14, 16, 19, 20, 21, 22, 23, 24)
    private val openInventories = mutableSetOf<Player>()

    private var Config: FileConfiguration? = null
    private var MessageConfig: FileConfiguration? = null
    private var Prefix: String = "§7[§aHwaSkyBlock§7] "

    override fun onEnable(main: JavaPlugin) {
        plugin = main
        Bukkit.getPluginManager().registerEvents(this, main)

        try {
            Config = ConfigManager.getConfig("setting")
            MessageConfig = ConfigManager.getConfig("message")
            Prefix = ChatColor.translateAlternateColorCodes(
                '&',
                Config?.getString("hwaskyblock-system.prefix") ?: "&7[&aHwaSkyBlock&7] "
            )
        } catch (ex: Exception) {
            Bukkit.getLogger().warning("[SkyblockRankingAddon] 설정 파일 로드 중 오류 발생: ${ex.message}")
        }

        registerCommand()
        startAutoUpdate()
        Bukkit.getLogger().info("✅ SkyblockRankingAddon 로드 완료")
    }

    override fun onDisable() {
        cachedRanking = emptyList()
        openInventories.clear()
    }

    private fun registerCommand() {
        val cmd = plugin.getCommand("섬랭킹")
        if (cmd == null) {
            Bukkit.getLogger().warning("[SkyblockRankingAddon] ⚠ plugin.yml에 '섬랭킹' 명령어가 정의되지 않았습니다.")
            return
        }

        cmd.setExecutor { sender, _, _, _ ->
            if (sender is Player) openRankingGUI(sender)
            true
        }
    }

    private fun startAutoUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            cachedRanking = fetchRanking()
            Bukkit.getScheduler().runTask(plugin, Runnable {
                openInventories.forEach { player ->
                    if (player.isOnline) openRankingGUI(player, refreshOnly = true)
                }
            })
        }, 0L, 20L * 60L * 5L)
    }

    private fun fetchRanking(): List<Triple<String, Int, String>> {
        val dbType = ConfigManager.getConfig("setting")?.getString("database.type")?.lowercase()
        val skyblockMap = DatabaseManager.Select_Skyblock_List
        val resultList = mutableListOf<Triple<String, Int, String>>()

        Bukkit.getLogger().info("[SkyblockRankingAddon] ===== 랭킹 데이터 로드 시작 =====")
        Bukkit.getLogger().info("[SkyblockRankingAddon] 현재 Skyblock 캐시 크기: ${skyblockMap.size}")

        val skyblockIds: List<String> = if (skyblockMap.isNotEmpty()) {
            skyblockMap.keys.filterNotNull()
        } else {
            when (dbType) {
                "sqlite" -> SkyblockDAO().getAllSkyblocks().mapNotNull { it["skyblock_id"]?.toString() }
                "mysql" -> MySQLSkyblockDAO().getAllSkyblocks().mapNotNull { it["skyblock_id"]?.toString() }
                else -> emptyList()
            }
        }

        if (skyblockIds.isEmpty()) {
            Bukkit.getLogger().warning("[SkyblockRankingAddon] ⚠ Skyblock 데이터가 없습니다.")
            return emptyList()
        }

        for (id in skyblockIds) {
            val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader")?.toString()
            val pointRaw = DatabaseManager.getSkyBlockData(id, "getSkyBlockPoint")

            if (leader == null) continue

            val point = when (pointRaw) {
                is String -> pointRaw.toIntOrNull() ?: 0
                is Number -> pointRaw.toInt()
                else -> 0
            }

            resultList.add(Triple(leader, point, id))
        }

        val sorted = resultList.sortedByDescending { it.second }.take(10)
        Bukkit.getLogger().info("[SkyblockRankingAddon] ===== 랭킹 로드 완료 (${sorted.size}개) =====")
        return sorted
    }

    private fun openRankingGUI(player: Player, refreshOnly: Boolean = false) {
        val inv = Bukkit.createInventory(null, 27, "§6🏆 SkyBlock Rankings")

        if (cachedRanking.isEmpty()) {
            val item = createItem(Material.BARRIER, "§c데이터 없음", listOf("§7아직 등록된 섬이 없습니다."))
            inv.setItem(13, item)
        } else {
            cachedRanking.forEachIndexed { index, (leader, point, id) ->
                val slot = rankingSlots.getOrNull(index) ?: return@forEachIndexed
                val skull = ItemStack(Material.PLAYER_HEAD)
                val meta = skull.itemMeta as SkullMeta
                meta.owningPlayer = Bukkit.getOfflinePlayer(leader)
                meta.setDisplayName("§e${index + 1}위 §f- §a$leader")
                meta.lore = listOf("§7포인트: §b$point", "§7클릭 시 섬 이동 (§f$id§7)")
                skull.itemMeta = meta
                inv.setItem(slot, skull)
            }
        }

        if (!refreshOnly) {
            player.openInventory(inv)
            openInventories.add(player)
        } else {
            player.openInventory.topInventory.contents = inv.contents
        }
    }

    private fun createItem(material: Material, name: String, lore: List<String> = emptyList()): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        meta?.lore = lore
        item.itemMeta = meta
        return item
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != "§6🏆 SkyBlock Rankings") return
        e.isCancelled = true

        val clickedItem = e.currentItem ?: return
        val name = clickedItem.itemMeta?.displayName ?: return
        val rank = name.removePrefix("§e").substringBefore("위").trim().toIntOrNull() ?: return

        val island = cachedRanking.getOrNull(rank - 1) ?: return
        val islandId = island.third

        player.closeInventory()
        player.sendMessage("$Prefix §a${rank}위 섬 (§f${islandId}§a) 으로 이동 중입니다...")
        teleportToIsland(player, islandId)
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as? Player ?: return
        if (e.view.title == "§6🏆 SkyBlock Rankings") {
            openInventories.remove(player)
        }
    }

    private fun teleportToIsland(player: Player, number: String) {
        val name = player.name
        if (DatabaseManager.getSkyBlockData(number, "getSkyBlockLeader") != name) {
            if (DatabaseManager.getShareDataList(number).contains(name)) {
                if (DatabaseManager.getShareData(number, name, "isUseJoin") == false) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            MessageConfig?.getString("message-event.join_refusal") ?: "&c섬 입장이 거부되었습니다."
                        )
                    )
                    return
                }
            } else {
                if (DatabaseManager.getSkyBlockData(number, "isSkyBlockJoin") == false) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            MessageConfig?.getString("message-event.join_refusal") ?: "&c섬 입장이 거부되었습니다."
                        )
                    )
                    return
                }
            }
        }

        val homeData = DatabaseManager.getSkyBlockData(number, "getSkyBlockHome")
        val worldName = "HwaSkyBlock.$number"
        val world = Bukkit.getWorld(worldName)

        if (world == null) {
            player.sendMessage(
                Prefix + ChatColor.translateAlternateColorCodes(
                    '&',
                    MessageConfig?.getString("message-event.island_not_exist") ?: "&c해당 섬이 존재하지 않습니다."
                )
            )
            return
        }

        if (homeData == null || homeData == 0) {
            player.teleport(world.spawnLocation)
            return
        }

        if (homeData is Location) {
            player.teleport(homeData)
        } else {
            player.teleport(world.spawnLocation)
        }
    }
}