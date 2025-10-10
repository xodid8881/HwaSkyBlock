package org.hwabaeg.hwaskyblock.events.entity

import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.hwabaeg.hwaskyblock.database.DatabaseManager

class SpawnEvent : Listener {

    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        val entityType: EntityType? = event.entityType
        val entity: Entity = event.getEntity()
        val world = entity.world
        val world_name = world.worldFolder.getName()
        val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (number[0] == "HwaSkyBlock") {
            val block_to_id = number[1]
            val monster_spawn = DatabaseManager.getSkyBlockData(
                block_to_id.toString(),
                "$block_to_id.setting.monster_spawn",
                "isSkyBlockMonsterSpawn"
            ) as? Boolean ?: true

            val animal_spawn = DatabaseManager.getSkyBlockData(
                block_to_id.toString(),
                "$block_to_id.setting.animal_spawn",
                "isSkyBlockAnimalSpawn"
            ) as? Boolean ?: true

            if (!monster_spawn) {
                if (isMonster(entityType)) {
                    event.isCancelled = true
                }
            }
            if (!animal_spawn) {
                if (isAnimal(entityType)) {
                    event.isCancelled = false
                }
            }
        }
    }

    private fun isMonster(entityType: EntityType?): Boolean {
        return entityType == EntityType.ZOMBIE || entityType == EntityType.SKELETON || entityType == EntityType.CREEPER || entityType == EntityType.SPIDER || entityType == EntityType.ENDERMAN || entityType == EntityType.WITCH || entityType == EntityType.SLIME || entityType == EntityType.GHAST || entityType == EntityType.PIGLIN || entityType == EntityType.VEX || entityType == EntityType.WITHER || entityType == EntityType.DROWNED || entityType == EntityType.HUSK || entityType == EntityType.PHANTOM
    }

    private fun isAnimal(entityType: EntityType?): Boolean {
        return entityType == EntityType.COW || entityType == EntityType.PIG || entityType == EntityType.SHEEP || entityType == EntityType.CHICKEN || entityType == EntityType.HORSE || entityType == EntityType.RABBIT || entityType == EntityType.LLAMA || entityType == EntityType.OCELOT || entityType == EntityType.PARROT || entityType == EntityType.TURTLE
    }
}