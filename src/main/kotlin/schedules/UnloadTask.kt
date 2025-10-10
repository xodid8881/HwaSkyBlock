package org.hwabaeg.hwaskyblock.schedules

import org.bukkit.Bukkit
import java.io.File

class UnloadTask : Runnable {
    override fun run() {
        for (world in Bukkit.getServer().worlds) {
            val worldName = world.name
            if (worldName.contains("HwaSkyBlock.")) {
                val worldDir = File(Bukkit.getServer().worldContainer, worldName)

                if (!worldDir.exists()) {
                    Bukkit.getLogger().warning(worldName + "에 대한 세계 디렉토리가 존재하지 않습니다.")
                    continue
                }

                if (world.players.isEmpty()) {
                    try {
                        Bukkit.getServer().unloadWorld(world, true)
                        Bukkit.getLogger().info("세계를 성공적으로 언로드했습니다: $worldName")
                    } catch (e: Exception) {
                        Bukkit.getLogger().severe("세계 언로드 실패: $worldName")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}