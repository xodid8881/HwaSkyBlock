package addon.dailymission.listener

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.gui.IslandMissionGUI
import addon.dailymission.gui.MissionDetailHolder
import addon.dailymission.gui.MissionMainHolder
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
    private val mainTitle = ChatColor.translateAlternateColorCodes('&', MessageConfig.guiTitle)

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val topHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view)
        val viewTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view)

        if (topHolder is MissionMainHolder || viewTitle == mainTitle) {
            e.isCancelled = true
            val diff = gui.resolveDifficultyBySlot(e.rawSlot) ?: return
            val islandId = IslandResolver.resolve(player) ?: return
            gui.openDetail(player, islandId, diff)
            return
        }

        if (topHolder is MissionDetailHolder || gui.isDetailTitle(viewTitle)) {
            e.isCancelled = true
            if (e.rawSlot == AddonConfig.guiDetailBackSlot) {
                val islandId = IslandResolver.resolve(player) ?: return
                gui.open(player, islandId)
                return
            }
            val islandId = IslandResolver.resolve(player) ?: return
            val difficulty = when (topHolder) {
                is MissionDetailHolder -> topHolder.difficulty
                else -> gui.resolveDifficultyBySlot(e.rawSlot) ?: return
            }
            val pair = gui.getDetailAssigned(islandId, difficulty) ?: return
            gui.handleDetailButtonClick(player, e.rawSlot, islandId, pair.first, pair.second)
        }
    }

    @EventHandler
    fun onDrag(e: InventoryDragEvent) {
        val topHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(e.view)
        val viewTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(e.view)
        val isMain = topHolder is MissionMainHolder || viewTitle == mainTitle
        val isDetail = topHolder is MissionDetailHolder || gui.isDetailTitle(viewTitle)
        if (!isMain && !isDetail) return
        e.isCancelled = true
    }
}

