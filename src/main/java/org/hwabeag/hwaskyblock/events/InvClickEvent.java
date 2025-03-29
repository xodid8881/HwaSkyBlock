package org.hwabeag.hwaskyblock.events;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.hwabeag.hwaskyblock.inventorys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InvClickEvent implements Listener {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    FileConfiguration PlayerConfig = ConfigManager.getConfig("player");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;
        if (e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            String name = player.getName();
            World world = player.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sky_block_menu_list"))))) {
                e.setCancelled(true);
                String clickitem = e.getCurrentItem().getItemMeta().getDisplayName();
                if (PlayerConfig.getConfigurationSection(name + ".skyblock.possession") != null) {
                    for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".skyblock.possession")).getKeys(false)) {

                        @Nullable String display_name = Config.getString("gui-slot-item-name.sky_block_menu_list.my");
                        display_name = Objects.requireNonNull(display_name).replace("{name}", key);
                        if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', display_name))) {
                            if (e.getClick() == ClickType.SHIFT_LEFT) {
                                if (Objects.equals(SkyBlockConfig.getString(key + ".leader"), name)) {
                                    HwaSkyBlockGlobalFragGUI inv = null;
                                    inv = new HwaSkyBlockGlobalFragGUI(player, key);
                                    inv.open(player);
                                } else {
                                    e.getInventory().clear();
                                    player.closeInventory();
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                                }
                                return;
                            }
                            if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                if (Objects.equals(SkyBlockConfig.getString(key + ".leader"), name)) {
                                    PlayerConfig.set(name + ".skyblock.setting", key);
                                    ConfigManager.saveConfigs();
                                    HwaSkyBlockSharerGUI inv = null;
                                    inv = new HwaSkyBlockSharerGUI(player, key);
                                    inv.open(player);
                                } else {
                                    e.getInventory().clear();
                                    player.closeInventory();
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.not_the_owner"))));
                                }
                                return;
                            }
                            if (e.getClick() == ClickType.LEFT) {
                                e.getInventory().clear();
                                player.closeInventory();
                                if (SkyBlockConfig.getInt(key + ".home") == 0) {
                                    String worldPath = "worlds/HwaSkyBlock." + key;
                                    world = Bukkit.getServer().getWorld(worldPath);
                                    if (world == null) {
                                        world = new WorldCreator(worldPath).createWorld();
                                        Location location = Objects.requireNonNull(world).getSpawnLocation();
                                        player.teleport(location);
                                        return;
                                    }
                                    Location location = Objects.requireNonNull(world).getSpawnLocation();
                                    player.teleport(location);
                                    return;
                                } else {
                                    String worldPath = "worlds/HwaSkyBlock." + key;
                                    world = Bukkit.getServer().getWorld(worldPath);
                                    if (world == null) {
                                        World createWorld = new WorldCreator(worldPath).createWorld();
                                        Location location = Objects.requireNonNull(createWorld).getSpawnLocation();
                                        player.teleport(location);
                                        return;
                                    } else {
                                        Location location = Objects.requireNonNull(world).getSpawnLocation();
                                        player.teleport(location);
                                    }
                                    Location location = SkyBlockConfig.getLocation(key + ".home");
                                    player.teleport(Objects.requireNonNull(location));
                                    return;
                                }
                            }
                        }
                    }
                }
                if (PlayerConfig.getConfigurationSection(name + ".skyblock.sharer") != null) {
                    for (String key : Objects.requireNonNull(PlayerConfig.getConfigurationSection(name + ".skyblock.sharer")).getKeys(false)) {
                        @Nullable String display_name = Config.getString("gui-slot-item-name.sky_block_menu_list.sharer");
                        display_name = Objects.requireNonNull(display_name).replace("{name}", key);
                        if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', display_name))) {
                            if (e.getClick() == ClickType.LEFT) {
                                e.getInventory().clear();
                                player.closeInventory();
                                if (SkyBlockConfig.getInt(key + ".home") == 0) {
                                    String worldPath = "worlds/HwaSkyBlock." + key;
                                    world = WorldCreator.name(worldPath).createWorld();
                                    Location location = Objects.requireNonNull(world).getSpawnLocation();
                                    player.teleport(Objects.requireNonNull(location));
                                } else {
                                    Location location = SkyBlockConfig.getLocation(key + ".home");
                                    player.teleport(Objects.requireNonNull(location));
                                }
                                return;
                            }
                        }
                    }
                }
                if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.previous_page"))))) {
                    int page = PlayerConfig.getInt(name + ".skyblock.page");
                    int plus = page - 1;
                    PlayerConfig.set(name + ".skyblock.page", plus);
                    ConfigManager.saveConfigs();
                    HwaSkyBlockMenuGUI inv = null;
                    inv = new HwaSkyBlockMenuGUI(player);
                    inv.open(player);
                    return;
                }
                if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.next_page"))))) {
                    int page = PlayerConfig.getInt(name + ".skyblock.page");
                    int plus = page + 1;
                    PlayerConfig.set(name + ".skyblock.page", plus);
                    ConfigManager.saveConfigs();
                    HwaSkyBlockMenuGUI inv = null;
                    inv = new HwaSkyBlockMenuGUI(player);
                    inv.open(player);
                    return;
                }
            }
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_setting"))))) {
                e.setCancelled(true);
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String id = number[1];
                    String clickitem = e.getCurrentItem().getItemMeta().getDisplayName();
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.global_setting.join"))))) {
                        boolean player_join = SkyBlockConfig.getBoolean(id + ".join");
                        if (player_join) {
                            SkyBlockConfig.set(id + ".join", false);
                        } else {
                            SkyBlockConfig.set(id + ".join", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalFragGUI inv = null;
                        inv = new HwaSkyBlockGlobalFragGUI(player, id);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.global_setting.break"))))) {
                        boolean block_break = SkyBlockConfig.getBoolean(id + ".break");
                        if (block_break) {
                            SkyBlockConfig.set(id + ".break", false);
                        } else {
                            SkyBlockConfig.set(id + ".break", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalFragGUI inv = null;
                        inv = new HwaSkyBlockGlobalFragGUI(player, id);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.global_setting.place"))))) {
                        boolean block_place = SkyBlockConfig.getBoolean(id + ".place");
                        if (block_place) {
                            SkyBlockConfig.set(id + ".place", false);
                        } else {
                            SkyBlockConfig.set(id + ".place", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalFragGUI inv = null;
                        inv = new HwaSkyBlockGlobalFragGUI(player, id);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.global_setting.use"))))) {
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.global_setting.pvp"))))) {
                        boolean pvp = SkyBlockConfig.getBoolean(id + ".pvp");
                        if (pvp) {
                            SkyBlockConfig.set(id + ".pvp", false);
                        } else {
                            SkyBlockConfig.set(id + ".pvp", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalFragGUI inv = null;
                        inv = new HwaSkyBlockGlobalFragGUI(player, id);
                        inv.open(player);
                        return;
                    }
                }
            }
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_setting"))))) {
                e.setCancelled(true);
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String id = number[1];
                    String clickitem = e.getCurrentItem().getItemMeta().getDisplayName();
                    for (String key : Objects.requireNonNull(SkyBlockConfig.getConfigurationSection(id + ".공유")).getKeys(false)) {
                        @Nullable String display_name = Config.getString("gui-slot-item-name.sharer_setting.sharer");
                        display_name = Objects.requireNonNull(display_name).replace("{name}", key);
                        if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', display_name))) {
                            if (e.getClick() == ClickType.SHIFT_LEFT) {
                                boolean player_join = SkyBlockConfig.getBoolean(id + ".sharer." + key + ".join");
                                if (player_join) {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".join", false);
                                } else {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".join", true);
                                }
                                ConfigManager.saveConfigs();
                                HwaSkyBlockSharerGUI inv = null;
                                inv = new HwaSkyBlockSharerGUI(player, id);
                                inv.open(player);
                                return;
                            }
                            if (e.getClick() == ClickType.SHIFT_RIGHT) {
                                boolean block_break = SkyBlockConfig.getBoolean(id + ".sharer." + key + ".break");
                                if (block_break) {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".break", false);
                                } else {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".break", true);
                                }
                                ConfigManager.saveConfigs();
                                HwaSkyBlockSharerGUI inv = null;
                                inv = new HwaSkyBlockSharerGUI(player, id);
                                inv.open(player);
                                return;
                            }
                            if (e.getClick() == ClickType.LEFT) {
                                boolean block_place = SkyBlockConfig.getBoolean(id + ".sharer." + key + ".place");
                                if (block_place) {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".place", false);
                                } else {
                                    SkyBlockConfig.set(id + ".sharer." + key + ".place", true);
                                }
                                ConfigManager.saveConfigs();
                                HwaSkyBlockSharerGUI inv = null;
                                inv = new HwaSkyBlockSharerGUI(player, id);
                                inv.open(player);
                                return;
                            }
                            if (e.getClick() == ClickType.RIGHT) {
                                PlayerConfig.set(name + ".skyblock.setting", key);
                                ConfigManager.saveConfigs();
                                HwaSkyBlockSharerUseGUI inv = null;
                                inv = new HwaSkyBlockSharerUseGUI(player, key);
                                inv.open(player);
                                return;
                            }
                        }
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.previous_page"))))) {
                        int page = PlayerConfig.getInt(name + ".skyblock.page");
                        int plus = page - 1;
                        PlayerConfig.set(name + ".skyblock.page", plus);
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerGUI inv = null;
                        inv = new HwaSkyBlockSharerGUI(player, id);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-slot-item-name.next_page"))))) {
                        int page = PlayerConfig.getInt(name + ".skyblock.page");
                        int plus = page + 1;
                        PlayerConfig.set(name + ".skyblock.page", plus);
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerGUI inv = null;
                        inv = new HwaSkyBlockSharerGUI(player, id);
                        inv.open(player);
                        return;
                    }
                }
            }
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list"))))) {
                e.setCancelled(true);
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String id = number[1];
                    String clickitem = e.getCurrentItem().getItemMeta().getDisplayName();
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.OAK_DOOR"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.door")) {
                            SkyBlockConfig.set(id + ".use.door", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.door", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.CHEST"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.chest")) {
                            SkyBlockConfig.set(id + ".use.chest", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.chest", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.BARREL"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.barrel")) {
                            SkyBlockConfig.set(id + ".use.barrel", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.barrel", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.HOPPER"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.hopper")) {
                            SkyBlockConfig.set(id + ".use.hopper", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.hopper", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.FURNACE"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.furnace")) {
                            SkyBlockConfig.set(id + ".use.furnace", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.furnace", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.BLAST_FURNACE"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.blast_furnace")) {
                            SkyBlockConfig.set(id + ".use.blast_furnace", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.blast_furnace", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.SHULKER_BOX"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.shulker_box")) {
                            SkyBlockConfig.set(id + ".use.shulker_box", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.shulker_box", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.OAK_TRAPDOOR"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.trapdoor")) {
                            SkyBlockConfig.set(id + ".use.trapdoor", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.trapdoor", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.OAK_BUTTON"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.button")) {
                            SkyBlockConfig.set(id + ".use.button", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.button", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.ANVIL"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.anvil")) {
                            SkyBlockConfig.set(id + ".use.anvil", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.anvil", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.SWEET_BERRIES"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.farm")) {
                            SkyBlockConfig.set(id + ".use.farm", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.farm", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.BEACON"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.beacon")) {
                            SkyBlockConfig.set(id + ".use.beacon", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.beacon", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.MINECART"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.minecart")) {
                            SkyBlockConfig.set(id + ".use.minecart", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.minecart", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.global_use_list.OAK_BOAT"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".use.boat")) {
                            SkyBlockConfig.set(id + ".use.boat", false);
                        } else {
                            SkyBlockConfig.set(id + ".use.boat", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockGlobalUseGUI inv = null;
                        inv = new HwaSkyBlockGlobalUseGUI(player);
                        inv.open(player);
                        return;
                    }
                }
            }
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list"))))) {
                e.setCancelled(true);
                if (Objects.equals(number[0], "HwaSkyBlock")) {
                    String id = number[1];
                    String clickitem = e.getCurrentItem().getItemMeta().getDisplayName();
                    String user_name = PlayerConfig.getString(name + ".skyblock.setting");
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.OAK_DOOR"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.door")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.door", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.door", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.CHEST"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.chest")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.chest", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.chest", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.BARREL"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.barrel")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.barrel", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.barrel", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.HOPPER"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.hopper")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.hopper", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.hopper", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.FURNACE"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.furnace")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.furnace", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.furnace", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.BLAST_FURNACE"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.blast_furnace")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.blast_furnace", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.blast_furnace", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.SHULKER_BOX"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.shulker_box")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.shulker_box", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.shulker_box", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.OAK_TRAPDOOR"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.trapdoor")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.trapdoor", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.trapdoor", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.OAK_BUTTON"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.button")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.button", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.button", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.ANVIL"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.anvil")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.anvil", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.anvil", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.SWEET_BERRIES"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.farm")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.farm", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.farm", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.BEACON"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.beacon")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.beacon", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.beacon", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.MINECART"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.minecart")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.minecart", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.minecart", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                    if (clickitem.equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("gui-name.sharer_use_list.OAK_BOAT"))))) {
                        if (SkyBlockConfig.getBoolean(id + ".sharer." + user_name + ".use.boat")) {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.boat", false);
                        } else {
                            SkyBlockConfig.set(id + ".sharer." + user_name + ".use.boat", true);
                        }
                        ConfigManager.saveConfigs();
                        HwaSkyBlockSharerUseGUI inv = null;
                        inv = new HwaSkyBlockSharerUseGUI(player, user_name);
                        inv.open(player);
                        return;
                    }
                }
            }
        }
    }
}
