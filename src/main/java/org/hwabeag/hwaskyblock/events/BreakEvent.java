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
    FileConfiguration MessageConfig = ConfigManager.getConfig("message");
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
            if (SkyBlockConfig.getString(id + ".leader") != null) {
                if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                        if (!SkyBlockConfig.getBoolean(id + ".break")) {
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                            return;
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".break")) {
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if (!isInRegion(event.getBlock(), id)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private boolean isInRegion(Block block, String id) {
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return (x >= 0 && x < SkyBlockConfig.getInt(id + ".size")) &&
                (y >= 0 && y < 256) &&
                (z >= 0 && z < SkyBlockConfig.getInt(id + ".size"));
    }
}
