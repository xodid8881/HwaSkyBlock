package addon.dailymission.util

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CooldownGuard(private val cooldownMs: Long) {
    private val lastMap = ConcurrentHashMap<UUID, Long>()

    fun tryPass(id: UUID): Boolean {
        val now = System.currentTimeMillis()
        val last = lastMap[id]
        if (last != null && now - last < cooldownMs) {
            return false
        }
        lastMap[id] = now
        return true
    }
}
