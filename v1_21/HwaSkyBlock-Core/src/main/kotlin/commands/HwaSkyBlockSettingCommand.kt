package org.hwabaeg.hwaskyblock.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.HwaSkyBlock.Companion.plugin
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.hwabaeg.hwaskyblock.database.sqlite.SQLiteManager
import org.hwabaeg.hwaskyblock.world.SkyblockWorldManager
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
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String?>? {
        if (!Bukkit.isPrimaryThread()) {
            return mutableListOf()
        }
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
                list.addAll(org.hwabaeg.hwaskyblock.HwaSkyBlock.onlineNameCache)
                return list
            }
        }
        return null
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
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
        if (args.isEmpty()) {
            for (key in MessageConfig.getStringList("setting-command-help-message")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
            }
            return true
        }
        if (args[0].equals(MessageConfig.getString("sub-command-message.changer"), ignoreCase = true)) {
            if (args.size == 1) {
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.nickname_blank"))
                    )
                )
                return true
            }
            val world = sender.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                val leader = DatabaseManager.getSkyBlockData(id.toString(), "getSkyBlockLeader") as? String
                if (leader == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (leader.equals(args[1], ignoreCase = true)) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.same_previous_owner"))
                        )
                    )
                    return true
                }
                val skyblock_master = leader

                val masterCount = DatabaseManager.getUserDataByName(
                    "$skyblock_master.skyblock.possession_count",
                    skyblock_master,
                    "getPlayerPossessionCount"
                ) as? Int ?: 1
                DatabaseManager.setUserDataByName(
                    "$skyblock_master.skyblock.possession_count",
                    skyblock_master,
                    masterCount - 1,
                    "setPlayerPossessionCount"
                )

                DatabaseManager.setUserDataByName(
                    "$skyblock_master.skyblock.possession.$id",
                    skyblock_master,
                    false,
                    "setPlayerPossession"
                )
                DatabaseManager.setSkyBlockData(id.toString(), args[1]!!, "setSkyBlockLeader")
                val newOwnerCount = DatabaseManager.getUserDataByName(
                    "${args[1]}.skyblock.possession_count",
                    args[1]!!,
                    "getPlayerPossessionCount"
                ) as? Int ?: 0
                DatabaseManager.setUserDataByName(
                    "${args[1]}.skyblock.possession_count",
                    args[1]!!,
                    newOwnerCount + 1,
                    "setPlayerPossessionCount"
                )
                DatabaseManager.setUserDataByName(
                    "${args[1]}.skyblock.possession.$id",
                    args[1]!!,
                    true,
                    "setPlayerPossession"
                )

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
                    DatabaseManager.getSkyBlockData(id.toString(), "getSkyBlockLeader") as? String
                if (skyblock_master == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }

                val currentCount = DatabaseManager.getUserDataByName(
                    "$skyblock_master.skyblock.possession_count",
                    skyblock_master,
                    "getPlayerPossessionCount"
                ) as? Int ?: 1

                DatabaseManager.setUserDataByName(
                    "$skyblock_master.skyblock.possession_count",
                    skyblock_master,
                    currentCount - 1,
                    "setPlayerPossessionCount"
                )

                DatabaseManager.setUserDataByName(
                    "$skyblock_master.skyblock.possession.$id",
                    skyblock_master,
                    false,
                    "setPlayerPossession"
                )

                DatabaseManager.setSkyBlockData(
                    id.toString(),
                    null,
                    "setSkyBlockName"
                )
                SkyblockWorldManager.setRemoveIsland(id)
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
