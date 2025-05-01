package org.hwabeag.hwaskyblock.commands

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.util.*

class HwaSkyBlockSettingCommand : TabCompleter, CommandExecutor {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String?>
    ): MutableList<String?>? {
        if (args.size == 1) {
            val list: MutableList<String?> = ArrayList<String?>()
            list.add("주인변경")
            list.add("강제분해")
            return list
        }
        if (args.size == 2) {
            if (args[0].equals("주인변경", ignoreCase = true)) {
                val list: MutableList<String?> = ArrayList<String?>()
                for (p in Bukkit.getOnlinePlayers()) {
                    list.add(p.name)
                }
                return list
            }
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (sender !is Player) {
            return true
        }
        if (!sender.isOp) {
            sender.sendMessage(
                Prefix + ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
                )
            )
            return true
        }
        val name = sender.name
        if (args.isEmpty()) {
            for (key in MessageConfig.getStringList("setting-command-help-message")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
            }
            return true
        }
        if (args[0].equals("주인변경", ignoreCase = true)) {
            val world = sender.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (SkyBlockConfig.get("$id.leader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (SkyBlockConfig.getString("$id.leader") == args[1]) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.same_previous_owner"))
                        )
                    )
                    return true
                }
                val skyblock_master = SkyBlockConfig.getString("$id.leader")
                PlayerConfig.set(
                    "$skyblock_master.skyblock.possession_count",
                    PlayerConfig.getInt("$skyblock_master.skyblock.possession_count") - 1
                )
                PlayerConfig.set("$skyblock_master.skyblock.possession.$id", null)
                SkyBlockConfig.set("$id.leader", args[1])
                PlayerConfig.set(
                    args[1] + ".skyblock.possession_count",
                    PlayerConfig.getInt("$skyblock_master.skyblock.possession_count") + 1
                )
                PlayerConfig.set(args[1] + ".skyblock.possession." + id, name)
                ConfigManager.saveConfigs()
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.change_owner"))
                    )
                )
                return true
            }
        }
        if (args[0].equals("강제분해", ignoreCase = true)) {
            val world = sender.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                val skyblock_master = SkyBlockConfig.getString("$id.leader")
                val econ: Economy? = HwaSkyBlock.economy
                econ!!.depositPlayer(skyblock_master, Config.getInt("chunk-buy").toDouble())
                PlayerConfig.set(
                    "$skyblock_master.skyblock.possession_count",
                    PlayerConfig.getInt("$skyblock_master.skyblock.possession_count") - 1
                )
                PlayerConfig.set("$skyblock_master.skyblock.possession.$id", null)
                SkyBlockConfig.set(id.toString(), null)
                HwaSkyBlock.setRemoveIsland(id)
                val PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull<String?>(skyblock_master))
                PlayerExact?.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.forced_refund_of_skyblock_zones"))
                    )
                )
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.forced_disassembly_completed"))
                    )
                )
                return true
            }
        }
        for (key in MessageConfig.getStringList("setting-command-help-message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
        }
        return true
    }
}