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
import java.util.List;
import java.util.Objects;

public class HwaSkyBlockMenuGUI implements Listener {
    private final Inventory inv;

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");

    public HwaSkyBlockMenuGUI(Player player) {
        inv = Bukkit.createInventory(null, 54, Objects.requireNonNull(Config.getString("gui-name.sky_block_menu_list")));
        initItemSetting(player);
    }

    private void initItemSetting(Player player) {
        String name = player.getName();
        int N = 0;
        int Page = 1;
        if (PlayerConfig.getConfigurationSection(name + ".섬.보유") != null) {
            for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".섬.보유")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".섬.페이지");
                if (Page == PlayerPage) {
                    ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7" + key + " &a- &7나의 섬"));
                    ArrayList<String> loreList = new ArrayList<>();
                    String world_name = player.getWorld().getWorldFolder().getName();
                    String[] number = world_name.split("\\.");
                    if (Objects.equals(number[0], "HwaSkyBlock")) {
                        String id = number[1];
                        if (Objects.equals(id, key)){
                            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &fSHIFT 좌클릭 &7시 해당 섬 글로벌 권한관리"));
                            loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &fSHIFT 우클릭 &7시 해당 섬 공유자 권한관리"));
                        }
                    }
                    loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &f좌클릭 &7시 해당 섬로 텔레포트"));
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
        if (PlayerConfig.getConfigurationSection(name + ".섬.공유") != null) {
            for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".섬.공유")).getKeys(false)) {
                int PlayerPage = PlayerConfig.getInt(name + ".섬.페이지");
                if (Page == PlayerPage) {
                    ItemStack item = new ItemStack(Material.GRASS_BLOCK, 1);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a- &7" + key + " &a- &7공유 섬"));
                    ArrayList<String> loreList = new ArrayList<>();
                    loreList.add(ChatColor.translateAlternateColorCodes('&', "&a- &f좌클릭 &7시 해당 섬로 텔레포트"));
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