package addon.dailymission.logic

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.mission.MissionType
import addon.dailymission.util.CooldownGuard
import addon.dailymission.util.IslandResolver
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor

class MissionProgressProcessor(
    private val config: AddonConfig,
    private val missionService: MissionService
) {
    private val cropCooldown = CooldownGuard(config.cropCooldownMs)

    fun onBlockBreak(player: Player, material: Material) {
        if (!isValidPlayer(player)) return
        val islandId = IslandResolver.resolve(player) ?: return
        val updates = missionService.recordProgress(islandId, MissionType.BLOCK_BREAK, material.name, 1, player)
        sendActionbar(player, updates)
    }

    fun onCropHarvest(player: Player, material: Material) {
        if (!isValidPlayer(player)) return
        if (!cropCooldown.tryPass(player.uniqueId)) return
        val islandId = IslandResolver.resolve(player) ?: return
        val updates = missionService.recordProgress(islandId, MissionType.CROP_HARVEST, material.name, 1, player)
        sendActionbar(player, updates)
    }

    fun onEntityKill(player: Player, entityType: String) {
        if (!isValidPlayer(player)) return
        val islandId = IslandResolver.resolve(player) ?: return
        val updates = missionService.recordProgress(islandId, MissionType.ENTITY_KILL, entityType, 1, player)
        sendActionbar(player, updates)
    }

    fun onCraft(player: Player, result: ItemStack, amount: Int) {
        if (!isValidPlayer(player)) return
        val islandId = IslandResolver.resolve(player) ?: return
        val updates = missionService.recordProgress(islandId, MissionType.CRAFT, result.type.name, amount, player)
        sendActionbar(player, updates)
    }

    fun onSmelt(player: Player, result: ItemStack, amount: Int) {
        if (!isValidPlayer(player)) return
        val islandId = IslandResolver.resolve(player) ?: return
        val updates = missionService.recordProgress(islandId, MissionType.SMELT, result.type.name, amount, player)
        sendActionbar(player, updates)
    }

    private fun isValidPlayer(player: Player): Boolean {
        if (!config.allowCreative && player.gameMode == GameMode.CREATIVE) return false
        return true
    }

    private fun sendActionbar(
        player: Player,
        updates: List<ProgressUpdate>
    ) {
        val chosen = chooseUpdate(updates) ?: return
        val raw = MessageConfig.actionbarProgress
            .replace("%name%", chosen.mission.name)
            .replace("%current%", chosen.current.toString())
            .replace("%goal%", chosen.goal.toString())
        val msg = ChatColor.translateAlternateColorCodes('&', raw)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(msg))
    }

    private fun chooseUpdate(
        updates: List<ProgressUpdate>
    ): ProgressUpdate? {
        if (updates.isEmpty()) return null
        val completed = updates.firstOrNull { it.completedNow }
        if (completed != null) return completed
        return updates.maxByOrNull { it.current.toDouble() / it.goal.coerceAtLeast(1) }
    }
}
