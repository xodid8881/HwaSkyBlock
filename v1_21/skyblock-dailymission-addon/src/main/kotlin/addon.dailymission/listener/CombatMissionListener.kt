package addon.dailymission.listener

import addon.dailymission.antiabuse.AntiAbuseGuard
import addon.dailymission.logic.MissionProgressProcessor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class CombatMissionListener(
    private val processor: MissionProgressProcessor,
    private val antiAbuse: AntiAbuseGuard? = null
) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        if (antiAbuse?.isBlocked(e.entity) == true) return
        processor.onEntityKill(killer, e.entity.type.name)
    }
}
