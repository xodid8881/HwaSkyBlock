package org.hwabeag.hwaskyblock.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HwaSkyBlockSharerGUI implements Listener {
    private final Inventory inv;

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");

    public HwaSkyBlockSharerGUI(Player player, String key) {
        inv = Bukkit.createInventory(null, 54, Objects.requireNonNull(Config.getString("gui-name.sharer_setting")));
        initItemSetting(player, key);
    }

    private ItemStack getHead(Player player, String name) {
        World world = player.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        String id = number[1];

        boolean player_join = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".join");
        boolean block_break = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".break");
        boolean block_place = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".place");

        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        @Nullable String display_name = Config.getString("gui-slot-item-name.sharer_setting.sharer");
        display_name = Objects.requireNonNull(display_name).replace("{name}", name);
        skull.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
        skull.setOwner(name);
        ArrayList<String> loreList = new ArrayList<>();
        Player player_exact = Bukkit.getServer().getPlayerExact(name);
        if (player_exact != null) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.online-lore"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.offline-lore"))));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.join"))));
        if (player_join) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.join-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.join-false"))));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.break"))));
        if (block_break) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.break-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.break-false"))));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.place"))));
        if (block_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.place-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.place-false"))));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_setting.use"))));

        skull.setLore(loreList);
        item.setItemMeta(skull);
        return item;
    }

    private void initItemSetting(Player player, String id) {
        String name = player.getName();
        int N = 0;
        int Page = 1;
        if (SkyBlockConfig.getConfigurationSection(id + ".sharer") != null) {
            for (String key : Objects.requireNonNull(SkyBlockConfig.getConfigurationSection(id + ".sharer")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".skyblock.page");
                if (Page == PlayerPage) {
                    ItemStack item = getHead(player, key);
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
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.previous_page"))));
        ArrayList<String> loreList = new ArrayList<>();
        for (String lore : Config.getStringList("gui-slot-item-name.previous_page-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(45, item);


        item = new ItemStack(Material.PAPER, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.next_page"))));
        loreList = new ArrayList<>();
        for (String lore : Config.getStringList("gui-slot-item-name.next_page-lore")) {
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