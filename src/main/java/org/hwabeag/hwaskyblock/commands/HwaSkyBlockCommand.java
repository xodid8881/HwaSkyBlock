package org.hwabeag.hwaskyblock.commands;

import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.hwaskyblock.HwaSkyBlock;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockBuyGUI;
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockGlobalFragGUI;
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockMenuGUI;
import org.hwabeag.hwaskyblock.inventorys.HwaSkyBlockSharerGUI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hwabeag.hwaskyblock.HwaSkyBlock.getPlugin;

public class HwaSkyBlockCommand implements TabCompleter, CommandExecutor {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<String>();
            list.add("구매");
            list.add("메뉴");
            list.add("이동");
            list.add("공유추가");
            list.add("공유제거");
            list.add("권한관리");
            list.add("세부관리");
            return list;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("공유추가")) {
                List<String> list = new ArrayList<String>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return list;
            }
            if (args[0].equalsIgnoreCase("공유제거")) {
                if (sender instanceof Player player) {
                    @NotNull Chunk chunk = player.getLocation().getChunk();
                    long chunkZ = chunk.getZ();
                    long chunkX = chunk.getX();
                    long id = chunkZ ^ (chunkX << 32);
                    List<String> list = new ArrayList<String>();
                    if (SkyBlockConfig.getConfigurationSection(id + ".공유") != null) {
                        list.addAll(Objects.requireNonNull(SkyBlockConfig.getConfigurationSection(id + ".공유")).getKeys(false));
                    }
                    return list;
                }
            }
            if (args[0].equalsIgnoreCase("권한관리")) {
                List<String> list = new ArrayList<String>();
                list.add("글로벌");
                list.add("공유자");
                return list;
            }
            if (args[0].equalsIgnoreCase("세부관리")) {
                List<String> list = new ArrayList<String>();
                list.add("환영말");
                list.add("스폰설정");
                list.add("공중분해");
                list.add("양도");
                return list;
            }
            if (args[1].equalsIgnoreCase("양도")) {
                List<String> list = new ArrayList<String>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return list;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        String name = player.getName();
        if (args.length == 0) {
            for (String key : Config.getStringList("command-help-message")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', key));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("구매")) {
            HwaSkyBlockBuyGUI inv = new HwaSkyBlockBuyGUI();
            inv.open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("메뉴")) {
            PlayerConfig.set(name + ".skyblock.page", 1);
            HwaSkyBlockMenuGUI inv = new HwaSkyBlockMenuGUI(player);
            inv.open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("이동")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.number_blank"))));
                return true;
            }
            if (args[1].matches("[+-]?\\d*(\\.\\d+)?")) {
                if (!Objects.equals(SkyBlockConfig.getString(args[1] + ".leader"), name)) {
                    if (SkyBlockConfig.getString(args[1] + ".sharer." + name) != null) {
                        if (!SkyBlockConfig.getBoolean(args[1] + ".sharer." + name + ".join")) {
                            player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.join_refusal"))));
                            return true;
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(args[1] + ".join")) {
                            player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.join_refusal"))));
                            return true;
                        }
                    }
                }
                if (SkyBlockConfig.getInt(args[1] + ".home") == 0) {
                    String worldPath = "worlds/HwaSkyBlock." + args[1];
                    World world = Bukkit.getServer().getWorld(worldPath);
                    if (world == null) {
                        world = new WorldCreator(worldPath).createWorld();
                        Location location = Objects.requireNonNull(world).getSpawnLocation();
                        player.teleport(location);
                        return true;
                    }
                    Location location = Objects.requireNonNull(world).getSpawnLocation();
                    player.teleport(location);
                    return true;
                } else {
                    String worldPath = "worlds/HwaSkyBlock." + args[1];
                    World world = Bukkit.getServer().getWorld(worldPath);
                    if (world == null) {
                        World createWorld = new WorldCreator(worldPath).createWorld();
                        Location location = Objects.requireNonNull(createWorld).getSpawnLocation();
                        player.teleport(location);
                        return true;
                    } else {
                        Location location = Objects.requireNonNull(world).getSpawnLocation();
                        player.teleport(location);
                    }
                    Location location = SkyBlockConfig.getLocation(args[1] + ".home");
                    player.teleport(Objects.requireNonNull(location));
                    return true;
                }
            } else {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.number_check"))));
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("공유추가")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.nickname_blank"))));
                return true;
            }
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.get(id + ".leader") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                    return true;
                }
                if (Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (SkyBlockConfig.getString(id + ".sharer." + args[1]) != null) {
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.existing_sharer"))));
                        return true;
                    }
                    SkyBlockConfig.set(id + ".sharer." + args[1], args[1]);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".join", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".break", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".place", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.door", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.chest", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.barrel", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.hopper", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.furnace", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.blast_furnace", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.shulker_box", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.trapdoor", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.button", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.anvil", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.farm", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.beacon", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.minecart", true);
                    SkyBlockConfig.set(id + ".sharer." + args[1] + ".use.boat", true);
                    PlayerConfig.set(args[1] + ".skyblock.sharer." + id, args[1]);
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.sharer_added_completed"))));
                    ConfigManager.saveConfigs();
                } else {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                }
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("공유제거")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.nickname_blank"))));
                return true;
            }
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                    return true;
                }
                if (Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (SkyBlockConfig.getString(id + ".sharer." + args[1]) == null) {
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.sharer_without"))));
                        return true;
                    }
                    SkyBlockConfig.set(id + ".sharer." + args[1], null);
                    PlayerConfig.set(args[1] + ".skyblock.sharer." + id, null);
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.remove_sharer"))));
                    ConfigManager.saveConfigs();
                } else {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("권한관리")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.write_down_global_or_sharers"))));
                return true;
            }
            if (args[1].equalsIgnoreCase("글로벌") || args[1].equalsIgnoreCase("공유자")) {
                World world = player.getWorld();
                String world_name = world.getWorldFolder().getName();
                String[] number = world_name.split("\\.");
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String id = number[1];
                    if (SkyBlockConfig.get(id + ".leader") == null) {
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                        return true;
                    }
                    if (Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        if (args[1].equals("글로벌")) {
                            HwaSkyBlockGlobalFragGUI inv = new HwaSkyBlockGlobalFragGUI(id);
                            inv.open(player);
                            return true;
                        }
                        if (args[1].equals("공유자")) {
                            PlayerConfig.set(name + ".skyblock.page", 1);
                            HwaSkyBlockSharerGUI inv = new HwaSkyBlockSharerGUI(player, id);
                            inv.open(player);
                            return true;
                        }
                    } else {
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                    }
                }
            } else {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.write_down_global_or_sharers"))));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("세부관리")) {
            if (args.length == 1) {
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.welcome_message_transfer_of_spawn_settings_public_disassembly"))));
                return true;
            }
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.get(id + ".leader") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                    return true;
                }
                if (Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (args[1].equalsIgnoreCase("환영말")) {
                        List<String> msgArgs = new ArrayList<String>(Arrays.asList(args).subList(2, args.length));
                        String messageArgs = StringUtils.join(msgArgs, " ");
                        SkyBlockConfig.set(id + ".welcome_message", messageArgs);
                        ConfigManager.saveConfigs();
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.welcome_message_setting"))));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("스폰설정")) {
                        SkyBlockConfig.set(id + ".home", player.getLocation());
                        ConfigManager.saveConfigs();
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.spawn_settings"))));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("공중분해")) {
                        String skyblock_master = SkyBlockConfig.getString(id + ".leader");
                        Economy econ = HwaSkyBlock.getEconomy();
                        econ.depositPlayer(skyblock_master, Config.getInt("chunk-buy"));
                        PlayerConfig.set(skyblock_master + ".skyblock.possession_count", PlayerConfig.getInt(skyblock_master + ".skyblock.possession_count") - 1);
                        PlayerConfig.set(skyblock_master + ".skyblock.possession." + id, null);
                        SkyBlockConfig.set(String.valueOf(id), null);
                        getPlugin().setRemoveIsland(id);
                        Player PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull(skyblock_master));
                        if (PlayerExact != null) {
                            PlayerExact.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.forced_refund_of_skyblock_zones"))));
                        }
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.forced_disassembly_completed"))));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("양도")) {
                        if (args.length == 2) {
                            player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.nickname_blank"))));
                            return true;
                        }
                        if (PlayerConfig.getInt(args[2] + ".skyblock.possession_count") >= Config.getInt("chunk-max")) {
                            player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.transferred_user_maximum"))));
                            return true;
                        }
                        String skyblock_master = SkyBlockConfig.getString(id + ".leader");
                        Economy econ = HwaSkyBlock.getEconomy();
                        econ.depositPlayer(skyblock_master, Config.getInt("chunk-buy"));
                        PlayerConfig.set(skyblock_master + ".skyblock.possession_count", PlayerConfig.getInt(skyblock_master + ".skyblock.possession_count") - 1);
                        PlayerConfig.set(skyblock_master + ".skyblock.possession." + id, null);

                        PlayerConfig.set(args[2] + ".skyblock.possession_count", PlayerConfig.getInt(args[2] + ".skyblock.possession_count") + 1);
                        PlayerConfig.set(args[2] + ".skyblock.possession." + id, args[2]);
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.refund_after_transfer"))));
                        player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.transfer_completed"))));
                        Player PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull(args[2]));
                        if (PlayerExact != null) {
                            PlayerExact.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.received_transfer"))));
                        }
                        return true;
                    }
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.welcome_message_transfer_of_spawn_settings_public_disassembly"))));
                } else {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                }
                return true;
            }
        }
        for (String key : Config.getStringList("command-help-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', key));
        }
        return true;
    }
}