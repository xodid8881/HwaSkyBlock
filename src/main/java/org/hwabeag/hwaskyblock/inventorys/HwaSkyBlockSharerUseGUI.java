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

    private void initItemSetting(Player player, String key) {
        World world = player.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        String id = number[1];

        boolean player_door = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.door");
        boolean player_chest = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.chest");
        boolean player_barrel = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.barrel");
        boolean player_hopper = SkyBlockConfig.getBoolean(id + ".use.hopper");
        boolean player_furnace = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.furnace");
        boolean player_blast_furnace = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.blast_furnace");
        boolean player_shulker_box = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.shulker_box");

        boolean player_trapdoor = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.trapdoor");
        boolean player_button = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.button");
        boolean player_anvil = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.anvil");
        boolean player_farm = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.farm");
        boolean player_beacon = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.beacon");
        boolean player_minecart = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.minecart");
        boolean player_boat = SkyBlockConfig.getBoolean(id + ".공유." + key + ".use.boat");

        ItemStack item = new ItemStack(Material.OAK_DOOR, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7문 권한 관리"));
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a문 권한 &7을 설정합니다."));
        if (player_door) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7문 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7문 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(10, item);

        item = new ItemStack(Material.CHEST, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7창고 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a창고 권한 &7을 설정합니다."));
        if (player_chest) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7창고 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7창고 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(11, item);

        item = new ItemStack(Material.BARREL, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7통 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a통 권한 &7을 설정합니다."));
        if (player_barrel) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7통 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7통 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(12, item);

        item = new ItemStack(Material.HOPPER, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7호퍼 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a호퍼 권한 &7을 설정합니다."));
        if (player_hopper) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7호퍼 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7호퍼 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(13, item);

        item = new ItemStack(Material.FURNACE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7화로 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a화로 권한 &7을 설정합니다."));
        if (player_furnace) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7화로 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7화로 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(14, item);

        item = new ItemStack(Material.BLAST_FURNACE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7용광로 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a용광로 권한 &7을 설정합니다."));
        if (player_blast_furnace) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7용광로 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7용광로 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(15, item);

        item = new ItemStack(Material.SHULKER_BOX, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7셜커상자 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a셜커상자 권한 &7을 설정합니다."));
        if (player_shulker_box) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7셜커상자 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7셜커상자 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(16, item);

        item = new ItemStack(Material.OAK_TRAPDOOR, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7다락문 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a다락문 권한 &7을 설정합니다."));
        if (player_trapdoor) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7다락문 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7다락문 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(19, item);

        item = new ItemStack(Material.OAK_BUTTON, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7버튼 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a버튼 권한 &7을 설정합니다."));
        if (player_button) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7버튼 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7버튼 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(20, item);

        item = new ItemStack(Material.ANVIL, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7모루 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a모루 권한 &7을 설정합니다."));
        if (player_anvil) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7모루 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7모루 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(21, item);

        item = new ItemStack(Material.SWEET_BERRIES, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭형 농작물 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a클릭형 농작물 권한 &7을 설정합니다."));
        if (player_farm) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭형 농작물 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭형 농작물 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(22, item);

        item = new ItemStack(Material.BEACON, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7신호기 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a신호기 권한 &7을 설정합니다."));
        if (player_beacon) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7신호기 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7신호기 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(23, item);

        item = new ItemStack(Material.MINECART, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7카트 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a카트 권한 &7을 설정합니다."));
        if (player_minecart) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7카트 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7카트 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(24, item);

        item = new ItemStack(Material.OAK_BOAT, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7보트 권한 관리"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a보트 권한 &7을 설정합니다."));
        if (player_boat) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7보트 사용 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7보트 사용 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(25, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

}