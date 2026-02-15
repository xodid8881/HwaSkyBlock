package addon.dailymission.listener

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.gui.IslandMissionGUI
import addon.dailymission.mission.MissionDifficulty
import addon.dailymission.util.IslandResolver
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent

class MissionGuiListener(
    private val gui: IslandMissionGUI
) : Listener {
    private val title = ChatColor.translateAlternateColorCodes(
        '&',
        MessageConfig.guiTitle
    )

    @EventHandler(ignoreCancelled = true)
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val viewTitle = e.view.title

        if (viewTitle == title) {
            e.isCancelled = true
            val diff = gui.resolveDifficultyBySlot(e.rawSlot) ?: return
            val islandId = IslandResolver.resolve(player) ?: return
            gui.openDetail(player, islandId, diff)
            return
        }

        if (gui.isDetailTitle(viewTitle)) {
            e.isCancelled = true
            if (e.rawSlot == AddonConfig.guiDetailBackSlot) {
                val islandId = IslandResolver.resolve(player) ?: return
                gui.open(player, islandId)
                return
            }
            val islandId = IslandResolver.resolve(player) ?: return
            val titleDiff = extractDifficulty(viewTitle) ?: return
            val pair = gui.getDetailAssigned(islandId, titleDiff) ?: return
            gui.handleDetailButtonClick(player, e.rawSlot, islandId, pair.first, pair.second)
        }
    }

    private fun extractDifficulty(title: String): MissionDifficulty? {
        val clean = ChatColor.stripColor(title) ?: return null
        val veryEasy = stripColors(MessageConfig.diffVeryEasy)
        val easy = stripColors(MessageConfig.diffEasy)
        val normal = stripColors(MessageConfig.diffNormal)
        val hard = stripColors(MessageConfig.diffHard)
        val veryHard = stripColors(MessageConfig.diffVeryHard)

        return when {
            clean.contains(veryEasy, ignoreCase = true) -> MissionDifficulty.VERY_EASY
            clean.contains(easy, ignoreCase = true) -> MissionDifficulty.EASY
            clean.contains(normal, ignoreCase = true) -> MissionDifficulty.NORMAL
            clean.contains(hard, ignoreCase = true) -> MissionDifficulty.HARD
            clean.contains(veryHard, ignoreCase = true) -> MissionDifficulty.VERY_HARD
            else -> null
        }
    }

    private fun stripColors(text: String): String {
        val colored = ChatColor.translateAlternateColorCodes('&', text)
        return ChatColor.stripColor(colored) ?: text
    }

    @EventHandler(ignoreCancelled = true)
    fun onDrag(e: InventoryDragEvent) {
        if (e.view.title != title) return
        e.isCancelled = true
    }
}
