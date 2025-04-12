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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class HwaSkyBlockMenuGUI implements Listener {
    private final Inventory inv;

    FileConfiguration MessageConfig = ConfigManager.getConfig("message");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");

    public HwaSkyBlockMenuGUI(Player player) {
        inv = Bukkit.createInventory(null, 54, Objects.requireNonNull(MessageConfig.getString("gui-name.sky_block_menu_list")));
        initItemSetting(player);
    }

    private void initItemSetting(Player player) {
        String name = player.getName();
        int N = 0;
        int Page = 1;
        if (PlayerConfig.getConfigurationSection(name + ".skyblock.possession") != null) {
            for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".skyblock.possession")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".skyblock.page");
                if (Page == PlayerPage) {
                    ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta itemMeta = item.getItemMeta();
                    @Nullable String display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.my");
                    display_name = Objects.requireNonNull(display_name).replace("{number}", key);
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
                    ArrayList<String> loreList = new ArrayList<>();
                    String world_name = player.getWorld().getWorldFolder().getName();
                    String[] number = world_name.split("\\.");
                    if (Objects.equals(number[0], "HwaSkyBlock")) {
                        String id = number[1];
                        if (Objects.equals(id, key)) {
                            for (String lore : MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.my-lore")) {
                                loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                            }
                        }
                    }
                    for (String lore : MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.sharer-lore")) {
                        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                    }
                    itemMeta.setLore(loreList);
                    item.setItemMeta(itemMeta);
                    inv.setItem(N, item);
                }
                N = N + 1;
                if (N >= 44) {
                    Page = Page + 1;
                    N = 0;
                }
            }
        }
        if (PlayerConfig.getConfigurationSection(name + ".skyblock.sharer") != null) {
            for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".skyblock.sharer")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".skyblock.page");
                if (Page == PlayerPage) {
                    ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta itemMeta = item.getItemMeta();
                    @Nullable String display_name = MessageConfig.getString("gui-slot-item-name.sky_block_menu_list.sharer");
                    display_name = Objects.requireNonNull(display_name).replace("{number}", key);
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
                    ArrayList<String> loreList = new ArrayList<>();
                    for (String lore : MessageConfig.getStringList("gui-slot-item-name.sky_block_menu_list.sharer-lore")) {
                        loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                    }
                    itemMeta.setLore(loreList);
                    item.setItemMeta(itemMeta);
                    inv.setItem(N, item);
                }
                N = N + 1;
                if (N >= 44) {
                    Page = Page + 1;
                    N = 0;
                }
            }
        }

        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.previous_page"))));
        ArrayList<String> loreList = new ArrayList<>();
        for (String lore : MessageConfig.getStringList("gui-slot-item-name.previous_page-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(45, item);


        item = new ItemStack(Material.PAPER, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(MessageConfig.getString("gui-slot-item-name.next_page"))));
        loreList = new ArrayList<>();
        for (String lore : MessageConfig.getStringList("gui-slot-item-name.next_page-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(53, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}