package org.hwabeag.hwaskyblock.commands

import net.milkbowl.vault.economy.Economy
import org.apache.commons.lang.StringUtils
import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.inventorys.*
import java.util.*

class HwaSkyBlockCommand : TabCompleter, CommandExecutor {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!
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
                    if (SkyBlockConfig.getConfigurationSection("$id.공유") != null) {
                        list.addAll(
                            Objects.requireNonNull<ConfigurationSection?>(
                                SkyBlockConfig.getConfigurationSection(
                                    "$id.공유"
                                )
                            ).getKeys(false)
                        )
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
            PlayerConfig.set("$name.skyblock.page", 1)
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
                for (key in Objects.requireNonNull<ConfigurationSection?>(PlayerConfig.getConfigurationSection(""))
                    .getKeys(false)) {
                    val skyblock_name = SkyBlockConfig.getString("$key.name")
                    if (messageArgs == skyblock_name) {
                        SkyBlock_Teleport(sender, key)
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
                if (SkyBlockConfig.get("$id.leader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (SkyBlockConfig.getString("$id.leader") == name) {
                    if (SkyBlockConfig.getString(id + ".sharer." + args[1]) != null) {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.existing_sharer"))
                            )
                        )
                        return true
                    }
                    SkyBlockConfig.set(id + ".sharer." + args[1], args[1])
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".join", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".break", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".place", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.door", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.chest", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.barrel", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.hopper", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.furnace", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.blast_furnace", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.shulker_box", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.trapdoor", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.button", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.anvil", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.farm", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.beacon", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.minecart", true)
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.boat", true)
                    PlayerConfig.set(args[1] + ".skyblock.sharer." + id, args[1])
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
                if (SkyBlockConfig.getString("$id.leader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (SkyBlockConfig.getString("$id.leader") == name) {
                    if (SkyBlockConfig.getString(id + ".sharer." + args[1]) == null) {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.sharer_without"))
                            )
                        )
                        return true
                    }
                    SkyBlockConfig.set(id + ".sharer." + args[1], null)
                    PlayerConfig.set(args[1] + ".skyblock.sharer." + id, null)
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
                    if (SkyBlockConfig.get("$id.leader") == null) {
                        sender.sendMessage(
                            Prefix + ChatColor.translateAlternateColorCodes(
                                '&',
                                Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                            )
                        )
                        return true
                    }
                    if (SkyBlockConfig.getString("$id.leader") == name) {
                        if (args[1] == "글로벌") {
                            val inv = HwaSkyBlockGlobalFragGUI(id)
                            inv.open(sender)
                            return true
                        }
                        if (args[1] == "공유자") {
                            PlayerConfig.set("$name.skyblock.page", 1)
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
                if (SkyBlockConfig.get("$id.leader") == null) {
                    sender.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_sky_block"))
                        )
                    )
                    return true
                }
                if (SkyBlockConfig.getString("$id.leader") == name) {
                    if (args[1].equals("섬이름", ignoreCase = true)) {
                        val msgArgs: MutableList<String?> =
                            ArrayList<String?>(Arrays.asList<String>(*args).subList(2, args.size))
                        val messageArgs = StringUtils.join(msgArgs, " ")
                        SkyBlockConfig.set("$id.name", messageArgs)
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
                        SkyBlockConfig.set("$id.welcome_message", messageArgs)
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
                        SkyBlockConfig.set("$id.home", sender.location)
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
                        if (PlayerConfig.getInt(args[2] + ".skyblock.possession_count") >= Config.getInt("chunk-max")) {
                            sender.sendMessage(
                                Prefix + ChatColor.translateAlternateColorCodes(
                                    '&',
                                    Objects.requireNonNull<String?>(MessageConfig.getString("message-event.transferred_user_maximum"))
                                )
                            )
                            return true
                        }
                        val skyblock_master = SkyBlockConfig.getString("$id.leader")
                        val econ: Economy? = HwaSkyBlock.economy
                        econ!!.depositPlayer(skyblock_master, Config.getInt("chunk-buy").toDouble())
                        PlayerConfig.set(
                            "$skyblock_master.skyblock.possession_count",
                            PlayerConfig.getInt("$skyblock_master.skyblock.possession_count") - 1
                        )
                        PlayerConfig.set("$skyblock_master.skyblock.possession.$id", null)

                        PlayerConfig.set(
                            args[2] + ".skyblock.possession_count",
                            PlayerConfig.getInt(args[2] + ".skyblock.possession_count") + 1
                        )
                        PlayerConfig.set(args[2] + ".skyblock.possession." + id, args[2])
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
        if (SkyBlockConfig.getString("$number.leader") != name) {
            if (SkyBlockConfig.getString("$number.sharer.$name") != null) {
                if (!SkyBlockConfig.getBoolean("$number.sharer.$name.join")) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.join_refusal"))
                        )
                    )
                    return
                }
            } else {
                if (!SkyBlockConfig.getBoolean("$number.join")) {
                    player.sendMessage(
                        Prefix + ChatColor.translateAlternateColorCodes(
                            '&',
                            Objects.requireNonNull<String?>(MessageConfig.getString("message-event.join_refusal"))
                        )
                    )
                    return
                }
            }
        }
        if (SkyBlockConfig.getInt("$number.home") == 0) {
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
            val section = SkyBlockConfig.getConfigurationSection("$number.home")
            val location = section?.let {
                val world = Bukkit.getWorld(it.getString("world") ?: return@let null)
                val x = it.getDouble("x")
                val y = it.getDouble("y")
                val z = it.getDouble("z")
                val yaw = it.getDouble("yaw").toFloat()
                val pitch = it.getDouble("pitch").toFloat()
                Location(world, x, y, z, yaw, pitch)
            }
            player.teleport(Objects.requireNonNull<Location?>(location))
        }
    }
}