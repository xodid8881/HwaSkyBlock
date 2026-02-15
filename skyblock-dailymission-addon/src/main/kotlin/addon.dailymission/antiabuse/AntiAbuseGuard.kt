package addon.dailymission.antiabuse

import addon.dailymission.config.AddonConfig
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.entity.CreatureSpawnEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class AntiAbuseGuard(
    private val config: AddonConfig
) {
    private val placedBlocks = ConcurrentHashMap<String, Long>()
    private val blockedEntities = ConcurrentHashMap<UUID, Boolean>()
    private val recentDrops = ConcurrentHashMap<UUID, Pair<UUID, Long>>()
    private val placedTtlMs = 1000L * 60L * 60L * 24L * 7L

    fun recordPlaced(location: Location) {
        if (config.allowPlacedBlocks) return
        placedBlocks[keyOf(location)] = System.currentTimeMillis()
    }

    fun isPlaced(location: Location): Boolean {
        if (config.allowPlacedBlocks) return false
        val key = keyOf(location)
        val time = placedBlocks[key] ?: return false
        if (System.currentTimeMillis() - time > placedTtlMs) {
            placedBlocks.remove(key)
            return false
        }
        return true
    }

    fun removePlaced(location: Location) {
        placedBlocks.remove(keyOf(location))
    }

    fun recordSpawn(entity: Entity, reason: CreatureSpawnEvent.SpawnReason) {
        if (isAllowedSpawn(reason)) {
            blockedEntities.remove(entity.uniqueId)
        } else {
            blockedEntities[entity.uniqueId] = true
        }
    }

    fun isBlocked(entity: Entity): Boolean {
        return blockedEntities[entity.uniqueId] == true
    }

    fun recordDrop(itemId: UUID, playerId: UUID) {
        if (!config.blockDropReacquire) return
        recentDrops[itemId] = playerId to System.currentTimeMillis()
    }

    fun isReacquire(itemId: UUID, playerId: UUID): Boolean {
        if (!config.blockDropReacquire) return false
        val data = recentDrops[itemId] ?: return false
        if (data.first != playerId) return false
        val now = System.currentTimeMillis()
        if (now - data.second > config.blockDropReacquireMs) {
            recentDrops.remove(itemId)
            return false
        }
        return true
    }

    private fun isAllowedSpawn(reason: CreatureSpawnEvent.SpawnReason): Boolean {
        return when (reason) {
            CreatureSpawnEvent.SpawnReason.SPAWNER_EGG -> config.allowSpawnEgg
            CreatureSpawnEvent.SpawnReason.SPAWNER -> config.allowSpawner
            CreatureSpawnEvent.SpawnReason.COMMAND -> config.allowCommandSpawn
            else -> true
        }
    }

    private fun keyOf(location: Location): String {
        return location.world?.name + ":" + location.blockX + ":" + location.blockY + ":" + location.blockZ
    }
}
