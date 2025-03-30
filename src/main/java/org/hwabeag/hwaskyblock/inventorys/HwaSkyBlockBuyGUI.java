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

public class HwaSkyBlockBuyGUI implements Listener {
    private final Inventory inv;

    FileConfiguration Config = ConfigManager.getConfig("setting");

    public HwaSkyBlockBuyGUI() {
        inv = Bukkit.createInventory(null, 54, Objects.requireNonNull(Config.getString("gui-name.buy")));
        initItemSetting();
    }

    private void initItemSetting() {
        for (String world_name : Objects.requireNonNull(Config.getConfigurationSection("sky-block-world")).getKeys(false)) {
            String item_type = Config.getString("sky-block-world." + world_name + ".item-type");
            String item_name = Config.getString("sky-block-world." + world_name + ".item-name");
            int item_slot = Config.getInt("sky-block-world." + world_name + ".item-slot");
            ArrayList<String> loreList = new ArrayList<>();
            for (String lore : Config.getStringList("sky-block-world." + world_name + ".item-lore")) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(item_type))), 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(item_name)));
            itemMeta.setLore(loreList);
            item.setItemMeta(itemMeta);
            inv.setItem(item_slot, item);
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}