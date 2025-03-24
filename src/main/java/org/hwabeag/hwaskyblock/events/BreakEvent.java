package org.hwabeag.hwaskyblock.events;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class BreakEvent implements Listener {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Block block = event.getBlock();
        World world = block.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String id = number[1];
            if (SkyBlockConfig.getString(id + ".주인장") != null) {
                if (!Objects.equals(SkyBlockConfig.getString(id + ".주인장"), name)) {
                    if (SkyBlockConfig.getString(id + ".공유." + name) == null) {
                        if (!SkyBlockConfig.getBoolean(id + ".break")) {
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(id + ".공유." + name + ".break")) {
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
