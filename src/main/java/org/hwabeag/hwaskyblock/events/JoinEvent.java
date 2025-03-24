package org.hwabeag.hwaskyblock.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;

public class JoinEvent implements Listener {
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (PlayerConfig.get(name + ".섬") == null) {
            PlayerConfig.addDefault(name + ".섬.보유갯수", 0);
            PlayerConfig.set(name + ".섬.보유갯수", 0);
            PlayerConfig.set(name + ".섬.위치", 0);
            PlayerConfig.set(name + ".섬.페이지", 1);
            PlayerConfig.set(name + ".섬.설정", "");
            ConfigManager.saveConfigs();
        }
    }
}
