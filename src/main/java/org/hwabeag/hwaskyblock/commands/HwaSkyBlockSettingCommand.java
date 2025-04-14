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
    FileConfiguration MessageConfig = ConfigManager.getConfig("message");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<String>();
            list.add("주인변경");
            list.add("강제분해");
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
            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.no_permission"))));
            return true;
        }
        String name = player.getName();
        if (args.length == 0) {
            for (String key : MessageConfig.getStringList("setting-command-help-message")) {
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
                if (SkyBlockConfig.get(id + ".leader") == null) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.no_sky_block"))));
                    return true;
                }
                if (Objects.equals(SkyBlockConfig.getString(id + ".leader"), args[1])) {
                    player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.same_previous_owner"))));
                    return true;
                }
                String skyblock_master = SkyBlockConfig.getString(id + ".leader");
                PlayerConfig.set(skyblock_master + ".skyblock.possession_count", PlayerConfig.getInt(skyblock_master + ".skyblock.possession_count") - 1);
                PlayerConfig.set(skyblock_master + ".skyblock.possession." + id, null);
                SkyBlockConfig.set(id + ".leader", args[1]);
                PlayerConfig.set(args[1] + ".skyblock.possession_count", PlayerConfig.getInt(skyblock_master + ".skyblock.possession_count") + 1);
                PlayerConfig.set(args[1] + ".skyblock.possession." + id, name);
                ConfigManager.saveConfigs();
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.change_owner"))));
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("강제분해")) {
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                String skyblock_master = SkyBlockConfig.getString(id + ".leader");
                Economy econ = HwaSkyBlock.getEconomy();
                econ.depositPlayer(skyblock_master, Config.getInt("chunk-buy"));
                PlayerConfig.set(skyblock_master + ".skyblock.possession_count", PlayerConfig.getInt(skyblock_master + ".skyblock.possession_count") - 1);
                PlayerConfig.set(skyblock_master + ".skyblock.possession." + id, null);
                SkyBlockConfig.set(String.valueOf(id), null);
                getPlugin().setRemoveIsland(id);
                Player PlayerExact = Bukkit.getServer().getPlayerExact(Objects.requireNonNull(skyblock_master));
                if (PlayerExact != null) {
                    PlayerExact.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.forced_refund_of_skyblock_zones"))));
                }
                player.sendMessage(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.forced_disassembly_completed"))));
                return true;
            }
        }
        for (String key : MessageConfig.getStringList("setting-command-help-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', key));
        }
        return true;
    }
}