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

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    public HwaSkyBlockGlobalFragGUI(Player player, String key) {
        inv = Bukkit.createInventory(null, 27, Objects.requireNonNull(Config.getString("gui-name.global_setting")));
        initItemSetting(player, key);
    }

    private void initItemSetting(Player player, String id) {
        boolean player_join = SkyBlockConfig.getBoolean(id + ".join");
        boolean block_break = SkyBlockConfig.getBoolean(id + ".break");
        boolean block_place = SkyBlockConfig.getBoolean(id + ".place");
        boolean pvp_place = SkyBlockConfig.getBoolean(id + ".pvp");

        ItemStack item = new ItemStack(Material.SPYGLASS, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 접근 권한 관리"));
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭 시 &a섬 접근 권한 &7을 설정합니다."));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7공유자가 아닌 유저들의 제한을 설정합니다."));
        if (player_join) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 접근 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 접근 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(11, item);

        item = new ItemStack(Material.WOODEN_AXE, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7블럭 파괴 권한 설정"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭을 진행하면 &a블럭 파괴 권한 &7을 설정합니다."));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7공유자가 아닌 유저들의 제한을 설정합니다."));
        if (block_break) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 파괴 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 파괴 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(12, item);

        item = new ItemStack(Material.SCAFFOLDING, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7블럭 설치 권한 설정"));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭을 진행하면 &a블럭 설치 권한 &7을 설정합니다."));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7공유자가 아닌 유저들의 제한을 설정합니다."));
        if (block_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 설치 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 설치 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(13, item);

        item = new ItemStack(Material.CHEST, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7블럭 이용 권한 설정"));
        loreList = new ArrayList<>();
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭을 진행하면 &a블럭 이용 권한 &7을 설정합니다."));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7공유자가 아닌 유저들의 제한을 설정합니다."));
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(14, item);

        item = new ItemStack(Material.DIAMOND_SWORD, 1);
        itemMeta = item.getItemMeta();
        loreList = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7전투 권한 설정"));
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7클릭을 진행하면 &a전투 권한 &7을 설정합니다."));
        if (pvp_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7전투 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7전투 &c거부"));
        }
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        inv.setItem(15, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

}