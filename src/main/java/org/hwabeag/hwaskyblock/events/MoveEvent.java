package org.hwabeag.hwaskyblock.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class MoveEvent implements Listener {
    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        Location from = event.getFrom();
        Location to = event.getTo();

        Block blockFrom = from.getWorld().getBlockAt(from);
        Block blockTo = to.getWorld().getBlockAt(to);

        World world = blockTo.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String block_to_id = number[1];

            if (SkyBlockConfig.getString(block_to_id + ".주인장") != null) {
                if (!Objects.equals(SkyBlockConfig.getString(block_to_id + ".주인장"), name)) {
                    if (SkyBlockConfig.getString(block_to_id + ".공유." + name) == null) {
                        if (!SkyBlockConfig.getBoolean(block_to_id + ".join")) {
                            player.teleport(blockFrom.getLocation().add(0, 1, 0));
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(block_to_id + ".공유." + name + ".join")) {
                            player.teleport(blockFrom.getLocation().add(0, 1, 0));
                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
