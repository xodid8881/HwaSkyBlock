package org.hwabeag.hwaskyblock.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class PhysicsEvent implements Listener {

    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        World world = event.getBlock().getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String block_to_id = number[1];
            boolean water_physics = SkyBlockConfig.getBoolean(block_to_id + ".setting.water_physics");
            boolean lava_physics = SkyBlockConfig.getBoolean(block_to_id + ".setting.lava_physics");
            if (!water_physics) {
                if (event.getBlock().getType() == Material.WATER) {
                    event.setCancelled(true);
                }
            }
            if (!lava_physics) {
                if (event.getBlock().getType() == Material.LAVA) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
