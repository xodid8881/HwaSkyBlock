package org.hwabeag.hwaskyblock.schedules;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;

public class UnloadWorldTask implements Runnable {

    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            String worldName = world.getName();
            if (worldName.contains("HwaSkyBlock.")) {
                File worldDir = new File(Bukkit.getServer().getWorldContainer(), worldName);

                if (!worldDir.exists()) {
                    Bukkit.getLogger().warning(worldName + "에 대한 세계 디렉토리가 존재하지 않습니다.");
                    continue;
                }

                if (world.getPlayers().isEmpty()) {
                    try {
                        Bukkit.getServer().unloadWorld(world, true);
                        Bukkit.getLogger().info("세계를 성공적으로 언로드했습니다: " + worldName);
                    } catch (Exception e) {
                        Bukkit.getLogger().severe("세계 언로드 실패: " + worldName);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
