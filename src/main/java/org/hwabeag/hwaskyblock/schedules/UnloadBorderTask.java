package org.hwabeag.hwaskyblock.schedules;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class UnloadBorderTask implements Runnable {

    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                setWorldBorder(world.getName(), SkyBlockConfig.getInt(id + ".size"));
            }
        }
    }

    public void setWorldBorder(String worldName, int size) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(world.getSpawnLocation());
            border.setSize(size);
            border.setWarningDistance(10);
            border.setDamageAmount(0.1);
        }
    }
}
