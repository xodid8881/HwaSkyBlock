package addon.dailymission.command

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.logic.MissionService
import addon.dailymission.mission.MissionRegistry
import addon.dailymission.storage.MissionDataStore
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class IslandMissionReloadCommand(
    private val config: AddonConfig,
    private val missionService: MissionService,
    private val store: MissionDataStore,
    private var registry: MissionRegistry,
    private val plugin: JavaPlugin
) : Command("isdmreload", "Daily mission reload", "/isdmreload", listOf("dailymissionreload", "섬미션리로드")) {

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        AddonConfig.load(plugin)
        MessageConfig.load(plugin)
        registry = MissionRegistry.Companion.load(plugin)
        missionService.reloadRegistry(registry)
        sender.sendMessage(color("${MessageConfig.prefix}${MessageConfig.reload}"))
        return true
    }

    private fun color(text: String): String =
        ChatColor.translateAlternateColorCodes('&', text)
}
