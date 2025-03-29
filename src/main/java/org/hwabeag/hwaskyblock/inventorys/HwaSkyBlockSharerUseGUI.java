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
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.ArrayList;
import java.util.Objects;

public class HwaSkyBlockSharerUseGUI implements Listener {
    private final Inventory inv;

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    public HwaSkyBlockSharerUseGUI(Player player, String key) {
        inv = Bukkit.createInventory(null, 36, Objects.requireNonNull(Config.getString("gui-name.sharer_use_list")));
        initItemSetting(player, key);
    }

    private void initItemSetting(Player player, String name) {
        World world = player.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        String id = number[1];

        boolean player_door = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.door");
        boolean player_chest = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.chest");
        boolean player_barrel = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.barrel");
        boolean player_hopper = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.hopper");
        boolean player_furnace = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.furnace");
        boolean player_blast_furnace = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.blast_furnace");
        boolean player_shulker_box = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.shulker_box");

        boolean player_trapdoor = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.trapdoor");
        boolean player_button = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.button");
        boolean player_anvil = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.anvil");
        boolean player_farm = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.farm");
        boolean player_beacon = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.beacon");
        boolean player_minecart = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.minecart");
        boolean player_boat = SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.boat");

        ItemStack item = new ItemStack(Material.OAK_DOOR, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR"))));
        ArrayList<String> loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.OAK_DOOR-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_door) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_DOOR-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(10, item);

        item = new ItemStack(Material.CHEST, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.CHEST"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.CHEST-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_chest) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.CHEST-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.CHEST-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(11, item);

        item = new ItemStack(Material.BARREL, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BARREL"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.BARREL-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_barrel) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BARREL-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BARREL-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(12, item);

        item = new ItemStack(Material.HOPPER, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.HOPPER"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.HOPPER-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_hopper) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.HOPPER-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.HOPPER-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(13, item);

        item = new ItemStack(Material.FURNACE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.FURNACE"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.FURNACE-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_furnace) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.FURNACE-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.FURNACE-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(14, item);

        item = new ItemStack(Material.BLAST_FURNACE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.BLAST_FURNACE-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_blast_furnace) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BLAST_FURNACE-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(15, item);

        item = new ItemStack(Material.SHULKER_BOX, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.SHULKER_BOX-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_shulker_box) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SHULKER_BOX-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(16, item);

        item = new ItemStack(Material.OAK_TRAPDOOR, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_trapdoor) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_TRAPDOOR-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(19, item);

        item = new ItemStack(Material.OAK_BUTTON, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.OAK_BUTTON-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_button) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BUTTON-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(20, item);

        item = new ItemStack(Material.ANVIL, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.ANVIL"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.ANVIL-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_anvil) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.ANVIL-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.ANVIL-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(21, item);

        item = new ItemStack(Material.SWEET_BERRIES, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.SWEET_BERRIES-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_farm) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.SWEET_BERRIES-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(22, item);

        item = new ItemStack(Material.BEACON, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BEACON"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.BEACON-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_beacon) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BEACON-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.BEACON-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(23, item);

        item = new ItemStack(Material.MINECART, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.MINECART"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.MINECART-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_minecart) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.MINECART-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.MINECART-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(24, item);

        item = new ItemStack(Material.OAK_BOAT, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT"))));
        loreList = new ArrayList<>();
        for (String key : Config.getStringList("gui-slot-item-name.sharer_use_list.OAK_BOAT-lore")) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', key));
        }
        if (player_boat) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT-true"))));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.sharer_use_list.OAK_BOAT-false"))));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(25, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

}