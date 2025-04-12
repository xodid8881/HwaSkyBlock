package org.hwabeag.hwaskyblock.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.ArrayList;
import java.util.Objects;

public class HwaSkyBlockSettingGUI implements Listener {
    private final Inventory inv;

    FileConfiguration MessageConfig = ConfigManager.getConfig("message");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    public HwaSkyBlockSettingGUI(String key) {
        inv = Bukkit.createInventory(null, 27, Objects.requireNonNull(MessageConfig.getString("gui-name.world_setting")));
        initItemSetting(key);
    }

    private void initItemSetting(String id) {
        boolean monster_spawn = SkyBlockConfig.getBoolean(id + ".setting.monster_spawn");
        boolean animal_spawn = SkyBlockConfig.getBoolean(id + ".setting.animal_spawn");
        String weather = SkyBlockConfig.getString(id + ".setting.weather");
        String time = SkyBlockConfig.getString(id + ".setting.time");
        boolean water_physics = SkyBlockConfig.getBoolean(id + ".setting.water_physics");
        boolean lava_physics = SkyBlockConfig.getBoolean(id + ".setting.lava_physics");

        ItemStack item = new ItemStack(Material.ZOMBIE_HEAD, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn"))));
        ArrayList<String> loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.monster_spawn-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (monster_spawn) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.monster_spawn-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(10, item);

        item = new ItemStack(Material.PIG_SPAWN_EGG, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn"))));
        loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.animal_spawn-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (animal_spawn) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.animal_spawn-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(11, item);

        item = new ItemStack(Material.SCAFFOLDING, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.weather"))));
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.weather-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (Objects.equals(weather, "clear")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.weather-clear"))));
        } else if (Objects.equals(weather, "rainy")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.weather-rainy"))));
        } else if (Objects.equals(weather, "thunder")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.weather-thunder"))));
        } else if (Objects.equals(weather, "basic")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.weather-basic"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(12, item);

        item = new ItemStack(Material.CLOCK, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.time"))));
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.time-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (Objects.equals(time, "morn")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.time-morn"))));
        } else if (Objects.equals(time, "noon")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.time-noon"))));
        } else if (Objects.equals(time, "evening")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.time-evening"))));
        } else if (Objects.equals(time, "basic")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.time-basic"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(14, item);

        item = new ItemStack(Material.WATER_BUCKET, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics"))));
        loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.water_physics-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (water_physics) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.water_physics-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(15, item);

        item = new ItemStack(Material.LAVA_BUCKET, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics"))));
        loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.world_setting.lava_physics-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (lava_physics) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.world_setting.lava_physics-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(16, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}