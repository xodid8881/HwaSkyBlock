package org.hwabeag.hwaskyblock;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.hwabeag.hwaskyblock.commands.HwaSkyBlockCommand;
import org.hwabeag.hwaskyblock.commands.HwaSkyBlockSettingCommand;
import org.hwabeag.hwaskyblock.config.ConfigManager;
import org.hwabeag.hwaskyblock.events.*;
import org.hwabeag.hwaskyblock.schedules.HwaSkyBlockTask;
import org.hwabeag.hwaskyblock.schedules.UnloadTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class HwaSkyBlock extends JavaPlugin {

    private static ConfigManager configManager;
    private static Economy econ = null;

    public static HwaSkyBlock getPlugin() {
        return JavaPlugin.getPlugin(HwaSkyBlock.class);
    }

    public static void getConfigManager() {
        if (configManager == null)
            configManager = new ConfigManager();
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BreakEvent(), this);
        getServer().getPluginManager().registerEvents(new InvClickEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new UseEvent(), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getServer().getPluginCommand("섬")).setExecutor(new HwaSkyBlockCommand());
        Objects.requireNonNull(getServer().getPluginCommand("섬설정")).setExecutor(new HwaSkyBlockSettingCommand());
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[HwaSkyBlock] Enable");
        this.saveDefaultConfig();
        getConfigManager();
        registerCommands();
        registerEvents();
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Vault 종속성이 발견되지 않아 비활성화됨!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new HwaSkyBlockTask(), 20 * 2, 20 * 2);
        Bukkit.getScheduler().runTaskTimer(this, new UnloadTask(), 0L, 400L);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static void addIsland(Player player, int id) {
        World world = Bukkit.getServer().getWorld("worlds/HwaSkyBlock");
        if (world == null) {
            world = new WorldCreator("worlds/HwaSkyBlock").createWorld();
            String world_name = "worlds/HwaSkyBlock." + id;
            copyFileStructure(Objects.requireNonNull(world).getWorldFolder(), new File(Bukkit.getWorldContainer(), world_name));
            new WorldCreator(world_name).createWorld();
            Location location = Objects.requireNonNull(Bukkit.getServer().getWorld("worlds/HwaSkyBlock." + id)).getSpawnLocation();
            player.teleport(location);
        } else {
            String world_name = "worlds/HwaSkyBlock." + id;
            copyFileStructure(Objects.requireNonNull(world).getWorldFolder(), new File(Bukkit.getWorldContainer(), world_name));
            new WorldCreator(world_name).createWorld();
            Location location = Objects.requireNonNull(Bukkit.getServer().getWorld("worlds/HwaSkyBlock." + id)).getSpawnLocation();
            player.teleport(location);
        }
    }

    private static void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("세계 디렉토리를 만들 수 없습니다!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRemoveIsland(String id) {
        for (World world : Bukkit.getServer().getWorlds()) {
            String worldName = world.getWorldFolder().getName();
            if (("HwaSkyBlock." + id).equals(worldName)) {
                for (Player player : world.getPlayers()) {
                    String worldPath = ConfigManager.getConfig("setting").getString("main-spawn-world");
                    World main_world = Bukkit.getServer().getWorld(Objects.requireNonNull(worldPath));
                    Location spawnLocation = Objects.requireNonNull(main_world).getSpawnLocation();
                    player.teleport(spawnLocation);
                }
                Bukkit.getServer().unloadWorld(world, true);
            }
        }
        String serverDir = System.getProperty("user.dir");
        String worldPath = serverDir + "/worlds/HwaSkyBlock." + id;
        deleteFileStructure(new File(worldPath));
    }

    private static void deleteFileStructure(File target) {
        try {
            if (target.isDirectory()) {
                String[] files = target.list();
                if (files != null) {
                    for (String file : files) {
                        File subFile = new File(target, file);
                        deleteFileStructure(subFile);
                    }
                }
            }
            if (target.delete()) {
                System.out.println(target.getAbsolutePath() + " 삭제 성공");
            } else {
                System.out.println(target.getAbsolutePath() + " 삭제 실패");
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 오류 발생", e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[HwaSkyBlock] Disable");
        ConfigManager.saveConfigs();
    }
}

