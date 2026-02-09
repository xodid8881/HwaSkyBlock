package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

class SpawnMissionListener(
    private val antiAbuse: AntiAbuseGuard
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onSpawn(e: CreatureSpawnEvent) {
        antiAbuse.recordSpawn(e.entity, e.spawnReason)
    }
}
