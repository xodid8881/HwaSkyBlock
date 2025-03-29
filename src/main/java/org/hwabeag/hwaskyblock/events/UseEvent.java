package org.hwabeag.hwaskyblock.events;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UseEvent implements Listener {

    FileConfiguration Config = ConfigManager.getConfig("setting");
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");
    String Prefix = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("hwaskyblock-system.prefix")));

    @EventHandler
    public void onPlayerDamageMinecart(VehicleDamageEvent event) {
        if (!(event.getVehicle() instanceof Minecart entity)) return;
        if (!(event.getAttacker() instanceof Player damager)) return;
        String name = damager.getName();
        World world = entity.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String id = number[1];
            if (SkyBlockConfig.getString(id + ".leader") != null) {
                if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                        if (!SkyBlockConfig.getBoolean(id + ".break")) {
                            damager.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.break")) {
                            damager.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageBoat(VehicleDamageEvent event) {
        if (!(event.getVehicle() instanceof Boat entity)) return;
        if (!(event.getAttacker() instanceof Player damager)) return;
        String name = damager.getName();
        World world = entity.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String id = number[1];
            if (SkyBlockConfig.getString(id + ".leader") != null) {
                if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                    if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                        if (!SkyBlockConfig.getBoolean(id + ".use.break")) {
                            damager.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    } else {
                        if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.break")) {
                            damager.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityCollision(VehicleEntityCollisionEvent event) {
        Entity vehicle = event.getVehicle();
        Entity entity = event.getEntity();
        if (entity instanceof Player player && vehicle instanceof Minecart) {
            String name = player.getName();
            World world = entity.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") != null) {
                    if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                            if (!SkyBlockConfig.getBoolean(id + ".use.minecart")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                                return;
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.minecart")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (entity instanceof Player player && vehicle instanceof Boat) {
            String name = player.getName();
            World world = entity.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") != null) {
                    if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                            if (!SkyBlockConfig.getBoolean(id + ".use.boat")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.boat")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        Entity entity = event.getEntity();
        Entity vehicle = event.getMount();
        if (entity instanceof Player player && vehicle instanceof Minecart) {
            String name = player.getName();
            World world = entity.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") != null) {
                    if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                            if (!SkyBlockConfig.getBoolean(id + ".use.minecart")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                                return;
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.minecart")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (entity instanceof Player player && vehicle instanceof Boat) {
            String name = player.getName();
            World world = entity.getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") != null) {
                    if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                            if (!SkyBlockConfig.getBoolean(id + ".use.boat")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.boat")) {
                                player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            World world = Objects.requireNonNull(block).getWorld();
            String world_name = world.getWorldFolder().getName();
            String[] number = world_name.split("\\.");
            if (Objects.equals(number[0], "HwaSkyBlock")) {
                String id = number[1];
                if (SkyBlockConfig.getString(id + ".leader") != null) {
                    if (!Objects.equals(SkyBlockConfig.getString(id + ".leader"), name)) {
                        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
                        if (customBlock != null) {
                            ItemStack CustomCrop = ItemsAdder.getCustomBlock(block);
                            @NotNull String CustomCrop_DisplayName = CustomCrop.getItemMeta().getDisplayName();
                            for (String type : Objects.requireNonNull(Config.getConfigurationSection("Custom-Crop-Interact")).getKeys(false)) {
                                if (CustomCrop_DisplayName.contains(type)) {
                                    if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                        if (!SkyBlockConfig.getBoolean(id + ".use.farm")) {
                                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                            event.setCancelled(true);
                                            return;
                                        }
                                    } else {
                                        if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.farm")) {
                                            player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                            event.setCancelled(true);
                                            return;
                                        }
                                    }
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("CHEST")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.chest")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.chest")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("TRAPDOOR")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.trapdoor")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.trapdoor")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("DOOR")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.door")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.door")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("BARREL")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.barrel")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.barrel")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("HOPPER")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.hopper")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.hopper")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("FURNACE")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.furnace")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.furnace")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("BLAST")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.furnace")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.furnace")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("SHULKER")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.shulker")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.shulker")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            return;
                        }
                        if (block.getType().name().contains("BUTTON")) {
                            if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                if (!SkyBlockConfig.getBoolean(id + ".use.button")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                                if (!SkyBlockConfig.getBoolean(id + ".use.door")) {
                                    for (int x = -1; x <= 1; x++) {
                                        for (int y = -1; y <= 1; y++) {
                                            for (int z = -1; z <= 1; z++) {
                                                if (x == 0 && y == 0 && z == 0) continue;
                                                Block adjacentBlock = block.getRelative(x, y, z);
                                                if (adjacentBlock.getType().name().contains("DOOR")) {
                                                    event.setCancelled(true);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!SkyBlockConfig.getBoolean(id + ".use.trapdoor")) {
                                    for (int x = -1; x <= 1; x++) {
                                        for (int y = -1; y <= 1; y++) {
                                            for (int z = -1; z <= 1; z++) {
                                                if (x == 0 && y == 0 && z == 0) continue;
                                                Block adjacentBlock = block.getRelative(x, y, z);
                                                if (adjacentBlock.getType().name().contains("TRAPDOOR")) {
                                                    event.setCancelled(true);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.button")) {
                                    player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                    event.setCancelled(true);
                                    return;
                                }
                                if (!SkyBlockConfig.getBoolean(id + ".use.door")) {
                                    for (int x = -1; x <= 1; x++) {
                                        for (int y = -1; y <= 1; y++) {
                                            for (int z = -1; z <= 1; z++) {
                                                if (x == 0 && y == 0 && z == 0) continue;
                                                Block adjacentBlock = block.getRelative(x, y, z);
                                                if (adjacentBlock.getType().name().contains("DOOR")) {
                                                    event.setCancelled(true);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!SkyBlockConfig.getBoolean(id + ".use.trapdoor")) {
                                    for (int x = -1; x <= 1; x++) {
                                        for (int y = -1; y <= 1; y++) {
                                            for (int z = -1; z <= 1; z++) {
                                                if (x == 0 && y == 0 && z == 0) continue;
                                                Block adjacentBlock = block.getRelative(x, y, z);
                                                if (adjacentBlock.getType().name().contains("TRAPDOOR")) {
                                                    event.setCancelled(true);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                                return;
                            }
                            if (block.getType().name().contains("ANVIL")) {
                                if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                    if (!SkyBlockConfig.getBoolean(id + ".use.anvil")) {
                                        player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                        event.setCancelled(true);
                                        return;
                                    }
                                } else {
                                    if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.anvil")) {
                                        player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                                return;
                            }
                            if (block.getType().name().contains("BEACON")) {
                                if (SkyBlockConfig.getString(id + ".sharer." + name) == null) {
                                    if (!SkyBlockConfig.getBoolean(id + ".use.beacon")) {
                                        player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                        event.setCancelled(true);
                                    }
                                } else {
                                    if (!SkyBlockConfig.getBoolean(id + ".sharer." + name + ".use.beacon")) {
                                        player.sendActionBar(Prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Config.getString("message-event.no_permission"))));
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player entity)) return;
        World world = entity.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String id = number[1];
            if (SkyBlockConfig.getString(id + ".leader") != null) {
                if (!SkyBlockConfig.getBoolean(id + ".pvp")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
