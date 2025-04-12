package org.hwabeag.hwaskyblock.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.hwabeag.hwaskyblock.HwaSkyBlock;

import java.util.HashMap;

public class ConfigManager {
    private static final HwaSkyBlock plugin = HwaSkyBlock.getPlugin();

    private static final HashMap<String, ConfigMaker> configSet = new HashMap<>();

    public ConfigManager() {
        String path = plugin.getDataFolder().getAbsolutePath();
        configSet.put("setting", new ConfigMaker(path, "config.yml"));
        configSet.put("message", new ConfigMaker(path, "message.yml"));
        configSet.put("skyblock", new ConfigMaker(path, "skyblock.yml"));
        configSet.put("player", new ConfigMaker(path, "player.yml"));
        loadSettings();
        saveConfigs();
    }

    public static void reloadConfigs() {
        for (String key : configSet.keySet()) {
            plugin.getLogger().info(key);
            configSet.get(key).reloadConfig();
        }
    }

    public static void saveConfigs() {
        for (String key : configSet.keySet())
            configSet.get(key).saveConfig();
    }

    public static FileConfiguration getConfig(String fileName) {
        return configSet.get(fileName).getConfig();
    }

    public static void loadSettings() {
        saveConfigs();
    }
}