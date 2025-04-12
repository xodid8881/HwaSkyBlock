package org.hwabeag.hwaskyblock.schedules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.hwabeag.hwaskyblock.HwaSkyBlock;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class HwaSkyBlockTask implements Runnable {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            if (PlayerConfig.get(name + ".skyblock") != null) {
                World world = player.getWorld();
                String world_name = world.getWorldFolder().getName();
                String[] number = world_name.split("\\.");
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String block_to_id = number[1];
                    String weather = SkyBlockConfig.getString(block_to_id + ".setting.weather");
                    String time = SkyBlockConfig.getString(block_to_id + ".setting.time");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (Objects.equals(weather, "clear")) {
                                world.setClearWeatherDuration(Integer.MAX_VALUE);
                            } else {
                                world.setClearWeatherDuration(0);
                            }
                            if (Objects.equals(weather, "rainy")) {
                                world.setStorm(true);
                                world.setWeatherDuration(Integer.MAX_VALUE);
                            } else {
                                world.setStorm(false);
                                world.setWeatherDuration(0);
                            }
                            if (Objects.equals(weather, "thunder")) {
                                world.setThunderDuration(Integer.MAX_VALUE);
                                world.setStorm(true);
                            } else {
                                world.setThunderDuration(0);
                            }
                            world.setThunderDuration(0);
                            world.setWeatherDuration(0);

                            if (Objects.equals(time, "morn")) {
                                long time_l = 1000;
                                world.setTime(time_l);
                            }
                            if (Objects.equals(time, "noon")) {
                                long time_l = 6000;
                                world.setTime(time_l);
                            }
                            if (Objects.equals(time, "evening")) {
                                long time_l = 18000;
                                world.setTime(time_l);
                            }
                        }
                    }.runTask(HwaSkyBlock.getPlugin());
                    String player_chunk = PlayerConfig.getString(name + ".skyblock.pos");
                    if (!Objects.equals(player_chunk, block_to_id)) {
                        if (SkyBlockConfig.get(block_to_id + ".leader") != null) {
                            String welcome_message = SkyBlockConfig.getString(block_to_id + ".welcome_message");
                            player.sendMessage(Prefix + " " + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(welcome_message)));
                            String chunk_master = SkyBlockConfig.getString(block_to_id + ".leader");
                            player.sendTitle(Prefix, ChatColor.translateAlternateColorCodes('&', "&r주인장 &f: &e" + chunk_master));
                            PlayerConfig.set(name + ".skyblock.pos", block_to_id);
                            ConfigManager.saveConfigs();
                        }
                    }
                    PlayerConfig.set(name + ".skyblock.pos", block_to_id);
                    ConfigManager.saveConfigs();
                } else {
                    PlayerConfig.set(name + ".skyblock.pos", world_name);
                    ConfigManager.saveConfigs();
                }
            }
        }
    }
}
