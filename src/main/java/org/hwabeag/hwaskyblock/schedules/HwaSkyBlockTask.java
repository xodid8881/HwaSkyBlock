package org.hwabeag.hwaskyblock.schedules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

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
            if (PlayerConfig.get(name + ".섬") != null) {
                @NotNull Chunk chunk = player.getLocation().getChunk();
                long chunkZ = chunk.getZ();
                long chunkX = chunk.getX();
                long id = chunkZ ^ (chunkX << 32);
                long player_chunk = PlayerConfig.getLong(name + ".섬.위치");
                if (player_chunk != id) {
                    if (SkyBlockConfig.get(id + ".주인장") != null) {
                        String welcome_message = SkyBlockConfig.getString(id + ".welcome_message");
                        player.sendMessage(Prefix + " " + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(welcome_message)));
                        String chunk_master = SkyBlockConfig.getString(id + ".주인장");
                        player.sendTitle(Prefix, ChatColor.translateAlternateColorCodes('&', "&r주인장 &f: &e" + chunk_master));
                        PlayerConfig.set(name + ".섬.위치", id);
                        ConfigManager.saveConfigs();
                    }
                }
                PlayerConfig.set(name + ".섬.위치", id);
                ConfigManager.saveConfigs();
            }
        }
    }
}
