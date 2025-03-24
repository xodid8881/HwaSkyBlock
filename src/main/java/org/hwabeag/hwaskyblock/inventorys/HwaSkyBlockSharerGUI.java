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

        boolean player_join = SkyBlockConfig.getBoolean(id + ".공유." + name + ".join");
        boolean block_break = SkyBlockConfig.getBoolean(id + ".공유." + name + ".break");
        boolean block_place = SkyBlockConfig.getBoolean(id + ".공유." + name + ".place");

        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&l" + name + " &7님 (공유자)"));
        skull.setOwner(name);
        ArrayList<String> loreList = new ArrayList<>();
        Player player_exact = Bukkit.getServer().getPlayerExact(name);
        if (player_exact != null) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&7&l- &r&a온라인"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&7&l- &r&c오프라인"));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &fSHIFT 좌클릭 &7시 &a섬 접근 권한&7을 설정합니다."));
        if (player_join) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 접근 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 접근 &c거부"));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &fSHIFT 우클릭 &7시 &a블럭 파괴 권한&7을 설정합니다."));
        if (block_break) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 파괴 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 파괴 &c거부"));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &f좌클릭 &7시 &a블럭 설치 권한&7을 설정합니다."));
        if (block_place) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 설치 &a허용"));
        } else {
            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &7섬 블럭 설치 &c거부"));
        }

        loreList.add("");
        loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &f우클릭 &7시 &a블럭 이용 권한&7을 설정합니다."));

        skull.setLore(loreList);
        item.setItemMeta(skull);
        return item;
    }

    private void initItemSetting(Player player, String id) {
        String name = player.getName();
        int N = 0;
        int Page = 1;
        if (SkyBlockConfig.getConfigurationSection(id + ".공유") != null) {
            for (String key : Objects.requireNonNull(SkyBlockConfig.getConfigurationSection(id + ".공유")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".섬.페이지");
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
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a이전 페이지"));
        itemMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', "&a- &f클릭 시 이전 페이지로 이동합니다.")));
        item.setItemMeta(itemMeta);
        inv.setItem(45, item);

        item = new ItemStack(Material.PAPER, 1);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a다음 페이지"));
        itemMeta.setLore(List.of(ChatColor.translateAlternateColorCodes('&', "&a- &f클릭 시 다음 페이지로 이동합니다.")));
        item.setItemMeta(itemMeta);
        inv.setItem(53, item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

}