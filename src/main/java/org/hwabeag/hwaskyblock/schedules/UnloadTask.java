package org.hwabeag.hwaskyblock.schedules;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class UnloadTask implements Runnable {

    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            String worldName = world.getName();
            if (worldName.contains("HwaSkyBlock.")) {
                File worldDir = new File(Bukkit.getServer().getWorldContainer(), worldName);

                if (!worldDir.exists()) {
                    Bukkit.getLogger().warning("World directory for " + worldName + " does not exist.");
                    continue;
                }

                if (world.getPlayers().isEmpty()) {
                    try {
                        Bukkit.getServer().unloadWorld(world, true);
                        Bukkit.getLogger().info("Successfully unloaded world: " + worldName);
                    } catch (Exception e) {
                        Bukkit.getLogger().severe("Failed to unload world: " + worldName);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
