package org.hwabaeg.hwaskyblock.addon.ranking

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
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
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.concurrent.CopyOnWriteArraySet
import java.util.jar.JarFile
import java.util.logging.Logger

class SkyblockRankingAddon : HwaSkyBlockAddon, Listener {

    /* ===============================
       Core
       =============================== */

    private lateinit var plugin: JavaPlugin
    private lateinit var log: Logger

    /* ===============================
       Config / Lang
       =============================== */

    private lateinit var config: FileConfiguration
    private lateinit var lang: FileConfiguration

    private var language = "kor"

    private var prefix = "&7[&aHwaSkyBlock&7] "
    private var guiTitle = "&6🏆 SkyBlock Rankings"
    private var reloadMessage = "&a랭킹이 리로드되었습니다."
    private var noIslandMessage = "&c해당 섬이 존재하지 않습니다."
    private var emptyTitle = "&c데이터 없음"
    private var emptyLore = "&7아직 등록된 섬이 없습니다."

    /* ===============================
       Runtime
       =============================== */

    private var cachedRanking: List<Triple<String, Int, String>> = emptyList()
    private val openInventories = CopyOnWriteArraySet<Player>()
    private val rankingSlots = listOf(10, 12, 14, 16, 19, 20, 21, 22, 23, 24)

    /* ===============================
       Enable / Disable
       =============================== */

    override fun onEnable(main: JavaPlugin) {
        plugin = main
        log = Logger.getLogger("HwaSkyBlock:SkyblockRankingAddon")

        loadConfig()
        loadLang()

        Bukkit.getPluginManager().registerEvents(this, plugin)

        registerCommand()
        startAutoUpdate()

        log.info("SkyblockRankingAddon enabled")
    }

    override fun onDisable() {
        cachedRanking = emptyList()
        openInventories.clear()
        log.info("SkyblockRankingAddon disabled")
    }

    /* ===============================
       Config / Lang Loader
       =============================== */

    private fun loadConfig() {
        val dir = File(plugin.dataFolder, "addons/SkyblockRankingAddon")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "config.yml")
        if (!file.exists()) extractFromJar("config.yml", file)

        config = YamlConfiguration.loadConfiguration(file)
        language = config.getString("language", "kor")!!
    }

    private fun loadLang() {
        val dir = File(plugin.dataFolder, "addons/SkyblockRankingAddon/message")
        if (!dir.exists()) dir.mkdirs()

        val fileName = "message_$language.yml"
        val file = File(dir, fileName)
        if (!file.exists()) extractFromJar("message/$fileName", file)

        lang = YamlConfiguration.loadConfiguration(file)

        prefix = lang.getString("prefix", prefix)!!
        guiTitle = lang.getString("gui.title", guiTitle)!!
        reloadMessage = lang.getString("message.reload", reloadMessage)!!
        noIslandMessage = lang.getString("message.no-island", noIslandMessage)!!
        emptyTitle = lang.getString("gui.empty.title", emptyTitle)!!
        emptyLore = lang.getString("gui.empty.lore", emptyLore)!!
    }

    private fun extractFromJar(path: String, outFile: File) {
        val jarFile = File(
            this::class.java.protectionDomain
                .codeSource.location.toURI()
        )

        JarFile(jarFile).use { jar ->
            jar.getJarEntry(path)?.let { entry ->
                jar.getInputStream(entry).use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } ?: log.severe("[SkyblockRankingAddon] Resource not found in jar: $path")
        }
    }

    /* ===============================
       Command
       =============================== */

    private fun registerCommand() {
        val cmd = plugin.getCommand("섬랭킹") ?: return

        cmd.setExecutor { sender, _, args, _ ->

            if (args.isNotEmpty() && args[0].lowercase() == "reload") {
                loadConfig()
                loadLang()
                cachedRanking = fetchRanking()
                sender.sendMessage(color("$prefix$reloadMessage"))
                return@setExecutor true
            }

            if (sender is Player) {
                cachedRanking = fetchRanking() // ✅ 여기 추가
                openRankingGUI(sender)
            }

            true
        }
    }

    /* ===============================
       Ranking Logic
       =============================== */

    private fun startAutoUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            Runnable { cachedRanking = fetchRanking() },
            20L * 10,
            20L * 60L * 5
        )
    }

    private fun fetchRanking(): List<Triple<String, Int, String>> {
        val result = mutableListOf<Triple<String, Int, String>>()

        for (id in DatabaseManager.Select_Skyblock_List.keys) {
            if (id.isNullOrBlank()) continue

            val leader = DatabaseManager
                .getSkyBlockData(id, "getSkyBlockLeader")
                ?.toString() ?: continue

            val point = when (val raw = DatabaseManager.getSkyBlockData(id, "getSkyBlockPoint")) {
                is Number -> raw.toInt()
                is String -> raw.toIntOrNull() ?: 0
                else -> 0
            }

            result.add(Triple(leader, point, id))
        }

        return result.sortedByDescending { it.second }.take(10)
    }

    /* ===============================
       GUI
       =============================== */

    private fun openRankingGUI(player: Player) {

        if (cachedRanking.isEmpty()) cachedRanking = fetchRanking()

        val inv = Bukkit.createInventory(null, 27, color(guiTitle))

        if (cachedRanking.isEmpty()) {
            inv.setItem(
                13,
                createItem(Material.BARRIER, color(emptyTitle), listOf(color(emptyLore)))
            )
        } else {
            cachedRanking.forEachIndexed { index, (leader, point, id) ->
                val slot = rankingSlots.getOrNull(index) ?: return@forEachIndexed

                val skull = ItemStack(Material.PLAYER_HEAD)
                val meta = skull.itemMeta as SkullMeta
                meta.owningPlayer = Bukkit.getOfflinePlayer(leader)
                meta.setDisplayName("§e${index + 1}위 §f- §a$leader")
                meta.lore = listOf(
                    "§7포인트: §b$point",
                    "§7섬 ID: §f$id",
                    "§8클릭하여 이동"
                )
                skull.itemMeta = meta
                inv.setItem(slot, skull)
            }
        }

        player.openInventory(inv)
        openInventories.add(player)
    }

    private fun createItem(material: Material, name: String, lore: List<String>): ItemStack =
        ItemStack(material).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName(name)
                this.lore = lore
            }
        }

    /* ===============================
       Events
       =============================== */

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != color(guiTitle)) return

        e.isCancelled = true

        val name = e.currentItem?.itemMeta?.displayName ?: return
        val rank = ChatColor.stripColor(name)
            ?.substringBefore("위")
            ?.trim()
            ?.toIntOrNull() ?: return

        val island = cachedRanking.getOrNull(rank - 1) ?: return
        player.closeInventory()
        teleportToIsland(player, island.third)
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        openInventories.remove(e.player)
    }

    private fun teleportToIsland(player: Player, id: String) {
        val world = Bukkit.getWorld("HwaSkyBlock.$id")
        if (world == null) {
            player.sendMessage(color("$prefix$noIslandMessage"))
            return
        }

        player.teleport(
            DatabaseManager.getSkyBlockData(id, "getSkyBlockHome") as? Location
                ?: world.spawnLocation
        )
    }

    /* ===============================
       Util
       =============================== */

    private fun color(text: String): String =
        ChatColor.translateAlternateColorCodes('&', text)
}
