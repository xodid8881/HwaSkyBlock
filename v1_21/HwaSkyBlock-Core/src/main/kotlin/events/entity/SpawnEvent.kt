package org.hwabaeg.hwaskyblock.events.entity

import org.bukkit.entity.Animals
import org.bukkit.entity.Monster
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager

class SpawnEvent : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onEntitySpawn(event: CreatureSpawnEvent) {
        val worldName = event.entity.world.name
        if (!worldName.startsWith("HwaSkyBlock.")) return

        val rawBlockId = worldName.substringAfter("HwaSkyBlock.")
        if (rawBlockId.isBlank()) return

        val blockIdCandidates = buildList {
            add(rawBlockId)
            val normalized = rawBlockId.substringBefore("_")
            if (normalized != rawBlockId) add(normalized)
        }

        val monsterSpawn = getBooleanSetting(
            blockIdCandidates,
            "isSkyBlockMonsterSpawn",
            true
        )

        val animalSpawn = getBooleanSetting(
            blockIdCandidates,
            "isSkyBlockAnimalSpawn",
            true
        )

        if (!monsterSpawn && event.entity is Monster) {
            event.isCancelled = true
            return
        }

        if (!animalSpawn && event.entity is Animals) {
            event.isCancelled = true
        }
    }

    private fun getBooleanSetting(
        blockIdCandidates: List<String>,
        key: String,
        defaultValue: Boolean
    ): Boolean {
        for (blockId in blockIdCandidates) {
            val parsed = parseBoolean(DatabaseManager.getSkyBlockData(blockId, key))
            if (parsed != null) return parsed
        }
        return defaultValue
    }

    private fun parseBoolean(value: Any?): Boolean? {
        return when (value) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> {
                when (value.trim().lowercase()) {
                    "true", "1", "yes", "on" -> true
                    "false", "0", "no", "off" -> false
                    else -> null
                }
            }
            else -> null
        }
    }
}
