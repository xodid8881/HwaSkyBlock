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

public class HwaSkyBlockGlobalFragGUI implements Listener {
    private final Inventory inv;

    FileConfiguration MessageConfig = ConfigManager.getConfig("message");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    public HwaSkyBlockGlobalFragGUI(String key) {
        inv = Bukkit.createInventory(null, 27, Objects.requireNonNull(MessageConfig.getString("gui-name.global_setting")));
        initItemSetting(key);
    }

    private void initItemSetting(String id) {
        boolean player_join = SkyBlockConfig.getBoolean(id + ".join");
        boolean block_break = SkyBlockConfig.getBoolean(id + ".break");
        boolean block_place = SkyBlockConfig.getBoolean(id + ".place");
        boolean pvp_place = SkyBlockConfig.getBoolean(id + ".pvp");

        ItemStack item = new ItemStack(Material.SPYGLASS, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.join"))));
        ArrayList<String> loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.global_setting.join-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_join) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.join-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.join-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(11, item);

        item = new ItemStack(Material.WOODEN_AXE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.break"))));
        loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.global_setting.break-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (block_break) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.break-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.break-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(12, item);

        item = new ItemStack(Material.SCAFFOLDING, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.place"))));
        for (String key : MessageConfig.getStringList("gui-slot-item-name.global_setting.place-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (block_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.place-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.place-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(13, item);

        item = new ItemStack(Material.CHEST, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.use"))));
        loreList = new ArrayList<>();
        for (String key : MessageConfig.getStringList("gui-slot-item-name.global_setting.use-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(14, item);

        item = new ItemStack(Material.DIAMOND_SWORD, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.pvp"))));
        for (String key : MessageConfig.getStringList("gui-slot-item-name.global_setting.pvp-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (pvp_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.pvp-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.global_setting.pvp-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(15, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}