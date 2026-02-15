package org.hwabaeg.hwaskyblock.addon.cattle.util

import org.bukkit.World
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import java.util.concurrent.ConcurrentHashMap

object IslandResolver {
    private const val PREFIX = "HwaSkyBlock."
    private const val CACHE_TTL_MS = 30_000L
    private val cache = ConcurrentHashMap<String, CacheEntry>()

    private data class CacheEntry(
        val islandId: String?,
        val expiresAt: Long
    )

    fun maybeIslandId(world: World): String? {
        val worldName = world.name
        if (!worldName.startsWith(PREFIX)) return null
        val islandId = worldName.removePrefix(PREFIX)
        return islandId.takeIf { it.isNotBlank() }
    }

    fun resolve(world: World): String? {
        val islandId = maybeIslandId(world) ?: return null
        val now = System.currentTimeMillis()
        val cached = cache[islandId]
        if (cached != null && cached.expiresAt > now) {
            return cached.islandId
        }

        val resolved = runCatching {
            val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") ?: return@runCatching null
            if (leader.toString().isBlank()) null else islandId
        }.getOrNull()
        if (resolved != null) {
            cache[islandId] = CacheEntry(resolved, now + CACHE_TTL_MS)
        } else {
            cache.remove(islandId)
        }
        return resolved
    }
}
