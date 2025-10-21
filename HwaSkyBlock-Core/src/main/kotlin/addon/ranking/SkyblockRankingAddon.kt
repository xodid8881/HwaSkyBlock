package org.hwabaeg.hwaskyblock.addon.ranking

import org.bukkit.Bukkit
import org.bukkit.Bukkit.createInventory
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
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager

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
            Bukkit.getLogger().info("[SkyblockRankingAddon] 설정 파일 로드 완료")
        } catch (ex: Exception) {
            Bukkit.getLogger().warning("[SkyblockRankingAddon] ⚠ 설정 파일 로드 중 오류 발생: ${ex.message}")
        }

        registerCommand()

        Bukkit.getScheduler().runTaskLater(main, Runnable {
            try {
                startAutoUpdate()
                Bukkit.getLogger().info("[SkyblockRankingAddon] ✅ DB 데이터 로드 후 랭킹 시스템 시작됨")
            } catch (ex: Exception) {
                Bukkit.getLogger().warning("[SkyblockRankingAddon] ❌ 랭킹 시스템 시작 중 오류: ${ex.message}")
                ex.printStackTrace()
            }
        }, 20L * 3)

        Bukkit.getLogger().info("[SkyblockRankingAddon] ⏳ 서버 초기화 대기 중... (3초 후 랭킹 활성화)")
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
        val skyblocks = DatabaseManager.Select_Skyblock_List
        val list = mutableListOf<Triple<String, Int, String>>()

        Bukkit.getLogger().info("[SkyblockRankingAddon] ===== 랭킹 데이터 로드 시작 =====")
        Bukkit.getLogger().info("[SkyblockRankingAddon] 현재 Skyblock 목록 크기: ${skyblocks.size}")

        for ((id, _) in skyblocks) {
            if (id == null) continue
            val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader") as? String ?: continue
            val pointRaw = DatabaseManager.getSkyBlockData(id, "getSkyBlockPoint") ?: "0"
            val point = when (pointRaw) {
                is String -> pointRaw.toIntOrNull() ?: 0
                is Number -> pointRaw.toInt()
                else -> 0
            }
            list.add(Triple(leader, point, id))
        }

        if (list.isEmpty()) {
            Bukkit.getLogger().warning("[SkyblockRankingAddon] ⚠ Skyblock 목록이 비어 있습니다. DatabaseManager 로드가 아직 안 됐을 수 있습니다.")
        }

        Bukkit.getLogger().info("[SkyblockRankingAddon] ===== 랭킹 로드 완료 (${list.size}개) =====")
        return list.sortedByDescending { it.second }.take(10)
    }

    private fun openRankingGUI(player: Player, refreshOnly: Boolean = false) {
        val inv = createInventory(null, 27, "§6🏆 SkyBlock Rankings")

        if (cachedRanking.isEmpty()) {
            val item = createItem(Material.BARRIER, "§c데이터 없음", listOf("§7아직 등록된 섬이 없습니다."))
            inv.setItem(13, item)
        } else {
            cachedRanking.forEachIndexed { index, (leader, point, id) ->
                val slot = rankingSlots.getOrNull(index) ?: return@forEachIndexed

                val islandName = DatabaseManager.getSkyBlockData(id, "getSkyBlockName") as? String ?: id
                val size = DatabaseManager.getSkyBlockData(id, "getSkyBlockSize")?.toString() ?: "0"
                val members = DatabaseManager.getShareDataList(id).size + 1 // 리더 포함
                val lore = listOf(
                    "§7이름: §f$islandName",
                    "§7리더: §a$leader",
                    "§7포인트: §b$point",
                    "§7크기: §d${size}x${size}",
                    "§7인원 수: §e$members 명",
                    "",
                    "§7클릭 시 해당 섬으로 이동 (§f$id§7)"
                )

                val item = createItem(
                    Material.PLAYER_HEAD,
                    "§e${index + 1}위 §f- §a$leader",
                    lore
                )
                inv.setItem(slot, item)
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
        SkyBlock_Teleport(player, islandId)
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as? Player ?: return
        if (e.view.title == "§6🏆 SkyBlock Rankings") {
            openInventories.remove(player)
        }
    }

    private fun SkyBlock_Teleport(player: Player, number: String) {
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
