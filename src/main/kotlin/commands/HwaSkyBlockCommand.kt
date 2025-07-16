package org.hwabeag.hwaskyblock.commands

import net.milkbowl.vault.economy.Economy
import org.apache.commons.lang.StringUtils
import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.*
import java.util.*

class HwaSkyBlockCommand : TabCompleter, CommandExecutor {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String?>
    ): MutableList<String?>? {
        if (args.size == 1) {
            val list: MutableList<String?> = ArrayList<String?>()
            list.add("구매")
            list.add("메뉴")
            list.add("이동")
            list.add("공유추가")
            list.add("공유제거")
            list.add("권한관리")
            list.add("세부관리")
            return list
        }
        if (args.size == 2) {
            if (args[0].equals("공유추가", ignoreCase = true)) {
                val list: MutableList<String?> = ArrayList<String?>()
                for (p in Bukkit.getOnlinePlayers()) {
                    list.add(p.name)
                }
                return list
            }
            if (args[0].equals("공유제거", ignoreCase = true)) {
                if (sender is Player) {
                    val chunk = sender.location.chunk
                    val chunkZ = chunk.z.toLong()
                    val chunkX = chunk.x.toLong()
                    val id = chunkZ xor (chunkX shl 32)
                    val list: MutableList<String?> = ArrayList<String?>()
                    val shareList = DatabaseManager.getSkyBlockShareList(id.toString())
                    if (shareList.isNotEmpty()) {
                        list.addAll(shareList)
                    }
                    return list

                }
            }
            if (args[0].equals("권한관리", ignoreCase = true)) {
                val list: MutableList<String?> = ArrayList<String?>()
                list.add("글로벌")
                list.add("공유자")
                return list
            }
            if (args[0].equals("세부관리", ignoreCase = true)) {
                val list: MutableList<String?> = ArrayList<String?>()
                list.add("섬이름")
                list.add("환영말")
                list.add("스폰설정")
                list.add("환경설정")
                list.add("공중분해")
                list.add("양도")
                return list
            }
            if (args[1].equals("양도", ignoreCase = true)) {
                val list: MutableList<String?> = ArrayList<String?>()
                for (p in Bukkit.getOnlinePlayers()) {
                    list.add(p.name)
                }
                return list
            }
        }
        return null
    }

    @Suppress("DEPRECATION")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        if (sender !is Player) {
            return true
        }
        val name = sender.name
        if (args.isEmpty()) {
            for (key in MessageConfig.getStringList("command-help-message")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
            }
            return true
        }
        if (args[0].equals("구매", ignoreCase = true)) {
            val inv = HwaSkyBlockBuyGUI()
            inv.open(sender)
            return true
        }
        if (args[0].equals("메뉴", ignoreCase = true)) {
            DatabaseManager.setUserData("$name.skyblock.page", sender, 1, "setPlayerPage")
            val inv = HwaSkyBlockMenuGUI(sender)
            inv.open(sender)
            return true
        }
        if (args[0].equals("이동", ignoreCase = true)) {
            if (args.size == 1) {
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.number_blank"))
                    )
                )
                return true
            }
            val msgArgs: MutableList<String?> = ArrayList<String?>(Arrays.asList<String>(*args).subList(1, args.size))
            val messageArgs = StringUtils.join(msgArgs, " ")
            if (messageArgs.matches("[+-]?((\\d+\\.\\d+)|(\\d+)|(\\.\\d+))".toRegex())) {
                SkyBlock_Teleport(sender, messageArgs)
            } else {
                for ((key, skyblock) in DatabaseManager.Select_Skyblock_List) {
                    if (skyblock != null) {
                        val skyblockName = skyblock.getSkyBlockName()
                        if (messageArgs == skyblockName) {
                            SkyBlock_Teleport(sender, key ?: continue)
                        }
                    }
                }
            }
            return true
        }
        if (args[0].equals("공유추가", ignoreCase = true)) {
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
                val leader = DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader")
                if (leader == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }

                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == name) {
                    if (DatabaseManager.getSkyBlockData(
                            id.toString(),
                            "$id.sharer.${args[1]}",
                            "getSkyBlockName"
                        ) != null
                    ) {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.existing_sharer"))
                            )
                        )
                        return true
                    }
                    DatabaseManager.addSkyBlockShare(id.toString(), args[1]!!)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "can_break", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "can_place", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_door", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_chest", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_barrel", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_hopper", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_furnace", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_blast_furnace", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_shulker_box", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_trapdoor", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_button", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_anvil", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_farm", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_beacon", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_minecart", true)
                    DatabaseManager.setSkyBlockSharePermission(id.toString(), args[1]!!, "use_boat", true)


                    DatabaseManager.setUserData("${args[1]}.skyblock.sharer.$id", sender, args[1]!!, "setPlayerSetting")
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.sharer_added_completed"))
                        )
                    )
                    ConfigManager.saveConfigs()
                } else {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                        )
                    )
                }
                return true
            }
        }
        if (args[0].equals("공유제거", ignoreCase = true)) {
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
                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == name) {
                    if (!DatabaseManager.getSkyBlockShareList(id.toString()).contains(args[1])) {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.sharer_without"))
                            )
                        )
                        return true
                    }
                    DatabaseManager.removeSkyBlockShare(id.toString(), args[1].toString())
                    DatabaseManager.removeSkyBlockSharePermission(id.toString(), args[1].toString())
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.remove_sharer"))
                        )
                    )
                    ConfigManager.saveConfigs()
                } else {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                        )
                    )
                }
            }
            return true
        }
        if (args[0].equals("권한관리", ignoreCase = true)) {
            if (args.size == 1) {
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.write_down_global_or_sharers"))
                    )
                )
                return true
            }
            if (args[1].equals("글로벌", ignoreCase = true) || args[1].equals("공유자", ignoreCase = true)) {
                val world = sender.world
                val world_name = world.worldFolder.getName()
                val number: Array<String?> =
                    world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
                    if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == name) {
                        if (args[1] == "글로벌") {
                            val inv = HwaSkyBlockGlobalFragGUI(id)
                            inv.open(sender)
                            return true
                        }
                        if (args[1] == "공유자") {
                            DatabaseManager.setUserData("$name.skyblock.page", sender, 1, "setPlayerPage")
                            val inv = HwaSkyBlockSharerGUI(sender, id)
                            inv.open(sender)
                            return true
                        }
                    } else {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                            )
                        )
                    }
                }
            } else {
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.write_down_global_or_sharers"))
                    )
                )
            }
            return true
        }
        if (args[0].equals("세부관리", ignoreCase = true)) {
            if (args.size == 1) {
                sender.sendMessage(
                    Prefix + ChatColor.translateAlternateColorCodes(
                        '&',
                        Objects.requireNonNull<String?>(MessageConfig.getString("message-event.welcome_message_transfer_of_spawn_settings_public_disassembly"))
                    )
                )
                return true
            }
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
                if (DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") == name) {
                    if (args[1].equals("섬이름", ignoreCase = true)) {
                        val msgArgs: MutableList<String?> =
                            ArrayList<String?>(Arrays.asList<String>(*args).subList(2, args.size))
                        val messageArgs = StringUtils.join(msgArgs, " ")
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.name", messageArgs, "setSkyBlockName")
                        ConfigManager.saveConfigs()
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.skyblock_name_setting"))
                            )
                        )
                        return true
                    }
                    if (args[1].equals("환영말", ignoreCase = true)) {
                        val msgArgs: MutableList<String?> =
                            ArrayList<String?>(Arrays.asList<String>(*args).subList(2, args.size))
                        val messageArgs = StringUtils.join(msgArgs, " ")
                        DatabaseManager.setSkyBlockData(
                            id.toString(),
                            "$id.welcome_message",
                            messageArgs,
                            "setSkyBlockWelcomeMessage"
                        )
                        ConfigManager.saveConfigs()
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.welcome_message_setting"))
                            )
                        )
                        return true
                    }
                    if (args[1].equals("스폰설정", ignoreCase = true)) {
                        val loc = sender.location
                        val homeString = "${loc.world?.name},${loc.x},${loc.y},${loc.z},${loc.yaw},${loc.pitch}"
                        DatabaseManager.setSkyBlockData(id.toString(), "$id.home", homeString, "setSkyBlockHome")
                        ConfigManager.saveConfigs()
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.spawn_settings"))
                            )
                        )
                        return true
                    }
                    if (args[1].equals("환경설정", ignoreCase = true)) {
                        val inv = HwaSkyBlockSettingGUI(id)
                        inv.open(sender)
                        return true
                    }
                    if (args[1].equals("공중분해", ignoreCase = true)) {
                        val skyblock_master =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") as? String
                        val econ: Economy? = HwaSkyBlock.economy
                        econ!!.depositPlayer(skyblock_master, Config.getInt("chunk-buy").toDouble())

                        DatabaseManager.setUserData(
                            "$skyblock_master.skyblock.possession_count",
                            sender,
                            (DatabaseManager.getUserData(
                                "$skyblock_master.skyblock.possession_count",
                                sender,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 1) - 1,
                            "setPlayerPossessionCount"
                        )

                        DatabaseManager.setUserData(
                            "$skyblock_master.skyblock.possession.$id",
                            sender,
                            null,
                            "setPlayerSetting"
                        )

                        DatabaseManager.DeleteSkyBlock(id.toString())
                        HwaSkyBlock.setRemoveIsland(id)
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.disassembly_refund"))
                            )
                        )
                        return true
                    }

                    if (args[1].equals("양도", ignoreCase = true)) {
                        if (args.size == 2) {
                            sender.sendMessage(
                                Prefix + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.nickname_blank"))
                                )
                            )
                            return true
                        }
                        if ((DatabaseManager.getUserData(
                                "${args[2]}.skyblock.possession_count",
                                sender,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 0) >= Config.getInt("chunk-max")
                        ) {
                            sender.sendMessage(
                                Prefix + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.transferred_user_maximum"))
                                )
                            )
                            return true
                        }
                        val skyblock_master =
                            DatabaseManager.getSkyBlockData(id.toString(), "$id.leader", "getSkyBlockLeader") as? String
                        val econ: Economy? = HwaSkyBlock.economy
                        econ!!.depositPlayer(skyblock_master, Config.getInt("chunk-buy").toDouble())

                        DatabaseManager.setUserData(
                            "$skyblock_master.skyblock.possession_count",
                            sender,
                            (DatabaseManager.getUserData(
                                "$skyblock_master.skyblock.possession_count",
                                sender,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 1) - 1,
                            "setPlayerPossessionCount"
                        )
                        DatabaseManager.setUserData(
                            "$skyblock_master.skyblock.possession.$id",
                            sender,
                            null,
                            "setPlayerSetting"
                        )

                        DatabaseManager.setUserData(
                            "${args[2]}.skyblock.possession_count",
                            sender,
                            (DatabaseManager.getUserData(
                                "${args[2]}.skyblock.possession_count",
                                sender,
                                "getPlayerPossessionCount"
                            ) as? Int ?: 0) + 1,
                            "setPlayerPossessionCount"
                        )
                        DatabaseManager.setUserData(
                            "${args[2]}.skyblock.possession.$id",
                            sender,
                            args[2],
                            "setPlayerSetting"
                        )
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.refund_after_transfer"))
                            )
                        )
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.transfer_completed"))
                            )
                        )
                        val PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull<String?>(args[2]))
                        PlayerExact?.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.received_transfer"))
                            )
                        )
                        return true
                    }
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.welcome_message_transfer_of_spawn_settings_public_disassembly"))
                        )
                    )
                } else {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.not_the_owner"))
                        )
                    )
                }
                return true
            }
        }
        for (key in MessageConfig.getStringList("command-help-message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', key))
        }
        return true
    }

    fun SkyBlock_Teleport(player: Player, number: String) {
        val name = player.name
        if (DatabaseManager.getSkyBlockData(number.toString(), "$number.leader", "getSkyBlockLeader") != name) {
            if (DatabaseManager.getSkyBlockShareList(number.toString()).contains(name)) {
                if (DatabaseManager.getSkyBlockSharePermission(number.toString(), name, "can_join") == false) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            MessageConfig.getString("message-event.join_refusal") ?: "&c가입이 거부되었습니다."
                        )
                    )
                    return
                }
            } else {
                if (DatabaseManager.getSkyBlockData(number.toString(), "$number.join", "isSkyBlockJoin") == false) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            MessageConfig.getString("message-event.join_refusal") ?: "&c가입이 거부되었습니다."
                        )
                    )
                    return
                }
            }
        }
        if (DatabaseManager.getSkyBlockData(number.toString(), "$number.home", "getSkyBlockHome") == 0) {
            val worldPath = "worlds/HwaSkyBlock.$number"
            var world = Bukkit.getServer().getWorld(worldPath)
            if (world == null) {
                world = WorldCreator(worldPath).createWorld()
                val location = Objects.requireNonNull<World?>(world).spawnLocation
                player.teleport(location)
                return
            }
            val location = Objects.requireNonNull<World?>(world).spawnLocation
            player.teleport(location)
        } else {
            val worldPath = "worlds/HwaSkyBlock.$number"
            val world = Bukkit.getServer().getWorld(worldPath)
            if (world == null) {
                val createWorld = WorldCreator(worldPath).createWorld()
                val location = Objects.requireNonNull<World?>(createWorld).spawnLocation
                player.teleport(location)
                return
            } else {
                val location = Objects.requireNonNull<World?>(world).spawnLocation
                player.teleport(location)
            }
            val location = DatabaseManager.getSkyBlockData(number, "$number.home", "getSkyBlockHome") as? Location
            player.teleport(Objects.requireNonNull(location)!!)
        }
    }
}