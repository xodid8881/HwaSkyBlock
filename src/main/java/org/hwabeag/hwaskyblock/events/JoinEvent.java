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
        if (PlayerConfig.get(name + ".skyblock") == null) {
            PlayerConfig.addDefault(name + ".skyblock.possession_count", 0);
            PlayerConfig.set(name + ".skyblock.possession_count", 0);
            PlayerConfig.set(name + ".skyblock.pos", 0);
            PlayerConfig.set(name + ".skyblock.page", 1);
            PlayerConfig.set(name + ".skyblock.setting", "");
            ConfigManager.saveConfigs();
        }
    }
}
