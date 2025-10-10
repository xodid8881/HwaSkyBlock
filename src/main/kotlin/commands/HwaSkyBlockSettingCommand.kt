package org.hwabaeg.hwaskyblock.commands

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import org.hwabaeg.hwaskyblock.HwaSkyBlock.Companion.plugin
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.hwabaeg.hwaskyblock.database.sqlite.SQLiteManager
import java.util.*

class HwaSkyBlockSettingCommand : TabCompleter, CommandExecutor {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
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
            list.add(MessageConfig.getString("sub-command-message.changer"))
            list.add(MessageConfig.getString("sub-command-message.forced-disassembly"))
            list.add(MessageConfig.getString("sub-command-message.reload"))
            return list
        }
        if (args.size == 2) {
            if (args[0].equals(MessageConfig.getString("sub-command-message.changer"), ignoreCase = true)) {
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
        if (args[0].equals(MessageConfig.getString("sub-command-message.changer"), ignoreCase = true)) {
            val world = sender.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.same_previous_owner"))
                        )
                    )
                    return true
                }
                val skyblock_master =
                    DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") as? String

                val masterCount = DatabaseManager.getUserData(
                    "$skyblock_master.skyblock.possession_count",
                    sender,
                    "getPlayerPossessionCount"
                ) as? Int ?: 1
                DatabaseManager.setUserData(
                    "$skyblock_master.skyblock.possession_count",
                    sender,
                    masterCount - 1,
                    "setPlayerPossessionCount"
                )

                DatabaseManager.setUserData(
                    "$skyblock_master.skyblock.possession.$id",
                    sender,
                    null,
                    "setPlayerSetting"
                )
                DatabaseManager.setSkyBlockData(id.toString(), "$id.leader", args[1]!!, "setSkyBlockLeader")
                val newOwnerCount = DatabaseManager.getUserData(
                    "${args[1]}.skyblock.possession_count",
                    sender,
                    "getPlayerPossessionCount"
                ) as? Int ?: 0
                DatabaseManager.setUserData(
                    "${args[1]}.skyblock.possession_count",
                    sender,
                    newOwnerCount + 1,
                    "setPlayerPossessionCount"
                )
                DatabaseManager.setUserData("${args[1]}.skyblock.possession.$id", sender, name, "setPlayerSetting")

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
        if (args[0].equals(MessageConfig.getString("sub-command-message.forced-disassembly"), ignoreCase = true)) {
            val world = sender.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                val skyblock_master =
                    DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") as? String
                val econ: Economy? = HwaSkyBlock.economy
                econ!!.depositPlayer(skyblock_master, Config.getInt("chunk-buy").toDouble())

                val currentCount = DatabaseManager.getUserData(
                    "$skyblock_master.skyblock.possession_count",
                    sender,
                    "getPlayerPossessionCount"
                ) as? Int ?: 1

                DatabaseManager.setUserData(
                    "$skyblock_master.skyblock.possession_count",
                    sender,
                    currentCount - 1,
                    "setPlayerPossessionCount"
                )

                DatabaseManager.setUserData(
                    "$skyblock_master.skyblock.possession.$id",
                    sender,
                    null,
                    "setPlayerSetting"
                )

                DatabaseManager.setSkyBlockData(
                    id.toString(),
                    id.toString(),
                    null,
                    "setSkyBlockName"
                )
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
        if (args[0].equals(MessageConfig.getString("sub-command-message.reload"), ignoreCase = true)) {
            ConfigManager.reloadConfigs()
            val dbType = ConfigManager.getConfig("setting")!!.getString("database.type")
            if (dbType == "mysql") {
                MySQLManager.init(plugin)
            } else if (dbType == "sqlite") {
                SQLiteManager.init(plugin)
            }
            sender.sendMessage(
                Prefix + ChatColor.translateAlternateColorCodes(
                    '&',
                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.reload"))
                )
            )
            return true
        }
        for (key in MessageConfig.getStringList("setting-command-help-message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
        }
        return true
    }
}