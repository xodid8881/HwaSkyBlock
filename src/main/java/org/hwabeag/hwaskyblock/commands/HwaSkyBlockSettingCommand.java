package org.hwabeag.hwaskyblock.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.hwaskyblock.HwaSkyBlock;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hwabeag.hwaskyblock.HwaSkyBlock.getPlugin;

public class HwaSkyBlockSettingCommand implements TabCompleter, CommandExecutor {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<String>();
            list.add("주인변경");
            list.add("공중분해");
            return list;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("주인변경")) {
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
        if (!player.isOp()) {
            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
            return true;
        }
        String name = player.getName();
        if (args.length == 0) {
            for (String key : Config.getStringList("setting-command-help-message")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', key));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("주인변경")) {
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.get(id + ".주인장") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                    return true;
                }
                if (Objects.equals(SkyBlockConfig.getString(id + ".주인장"), args[1])) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.same_previous_owner"))));
                    return true;
                }
                String chunk_master = SkyBlockConfig.getString(id + ".주인장");
                PlayerConfig.set(chunk_master + ".섬.보유갯수", PlayerConfig.getInt(chunk_master + ".섬.보유갯수") - 1);
                PlayerConfig.set(chunk_master + ".섬.보유." + id, null);
                SkyBlockConfig.set(id + ".주인장", args[1]);
                PlayerConfig.set(args[1] + ".섬.보유갯수", PlayerConfig.getInt(chunk_master + ".섬.보유갯수") + 1);
                PlayerConfig.set(args[1] + ".섬.보유." + id, name);
                ConfigManager.saveConfigs();
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.change_owner"))));
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("공중분해")) {
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.get(id + ".주인장") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_sky_block"))));
                    return true;
                }
                String chunk_master = SkyBlockConfig.getString(id + ".주인장");
                Economy econ = HwaSkyBlock.getEconomy();
                econ.depositPlayer(chunk_master, Config.getInt("chunk-buy"));
                PlayerConfig.set(chunk_master + ".섬.보유갯수", PlayerConfig.getInt(chunk_master + ".섬.보유갯수") - 1);
                PlayerConfig.set(chunk_master + ".섬.보유." + id, null);
                SkyBlockConfig.set(String.valueOf(id), null);
                getPlugin().setRemoveIsland(id);
                Player PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull(chunk_master));
                if (PlayerExact != null) {
                    PlayerExact.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.forced_refund_of_skyblock_zones"))));
                }
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.forced_disassembly_completed"))));
                return true;
            }
        }
        for (String key : Config.getStringList("setting-command-help-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', key));
        }
        return true;
    }
}