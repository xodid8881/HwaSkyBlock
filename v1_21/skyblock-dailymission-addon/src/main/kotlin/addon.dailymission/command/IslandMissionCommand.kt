package addon.dailymission.command

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.gui.IslandMissionGUI
import addon.dailymission.logic.MissionService
import addon.dailymission.storage.MissionDataStore
import addon.dailymission.util.IslandResolver
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class IslandMissionCommand(
    private val config: AddonConfig,
    private val missionService: MissionService,
    private val store: MissionDataStore,
    private val gui: IslandMissionGUI
) : Command("isdm", "Daily mission command", "/isdm", listOf("dailymission", "섬미션")) {

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (!config.enabled) {
            sender.sendMessage(color("${MessageConfig.prefix}${MessageConfig.disabled}"))
            return true
        }

        val player = sender as? Player ?: run {
            sender.sendMessage(color("${MessageConfig.prefix}Player only."))
            return true
        }

        val islandId = IslandResolver.resolve(player) ?: run {
            player.sendMessage(color("${MessageConfig.prefix}${MessageConfig.noIsland}"))
            return true
        }

        gui.open(player, islandId)
        return true
    }

    private fun color(text: String): String =
        ChatColor.translateAlternateColorCodes('&', text)
}
