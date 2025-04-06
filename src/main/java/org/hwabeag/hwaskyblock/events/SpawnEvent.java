package org.hwabeag.hwaskyblock.events;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.hwabeag.hwaskyblock.config.ConfigManager;

import java.util.Objects;

public class SpawnEvent implements Listener {
    FileConfiguration SkyBlockConfig = ConfigManager.getConfig("skyblock");

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        EntityType entityType = event.getEntityType();
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        String world_name = world.getWorldFolder().getName();
        String[] number = world_name.split("\\.");
        if (Objects.equals(number[0], "HwaSkyBlock")) {
            String block_to_id = number[1];
            boolean monster_spawn = SkyBlockConfig.getBoolean(block_to_id + ".setting.monster_spawn");
            boolean animal_spawn = SkyBlockConfig.getBoolean(block_to_id + ".setting.animal_spawn");
            if (!monster_spawn) {
                if (isMonster(entityType)) {
                    event.setCancelled(true);
                }
            }
            if (!animal_spawn) {
                if (isAnimal(entityType)) {
                    event.setCancelled(false);
                }
            }
        }
    }

    private boolean isMonster(EntityType entityType) {
        return entityType == EntityType.ZOMBIE ||
                entityType == EntityType.SKELETON ||
                entityType == EntityType.CREEPER ||
                entityType == EntityType.SPIDER ||
                entityType == EntityType.ENDERMAN ||
                entityType == EntityType.WITCH ||
                entityType == EntityType.SLIME ||
                entityType == EntityType.GHAST ||
                entityType == EntityType.PIGLIN ||
                entityType == EntityType.VEX ||
                entityType == EntityType.WITHER ||
                entityType == EntityType.DROWNED ||
                entityType == EntityType.HUSK ||
                entityType == EntityType.PHANTOM;
    }

    private boolean isAnimal(EntityType entityType) {
        return entityType == EntityType.COW ||
                entityType == EntityType.PIG ||
                entityType == EntityType.SHEEP ||
                entityType == EntityType.CHICKEN ||
                entityType == EntityType.HORSE ||
                entityType == EntityType.RABBIT ||
                entityType == EntityType.LLAMA ||
                entityType == EntityType.OCELOT ||
                entityType == EntityType.PARROT ||
                entityType == EntityType.TURTLE;
    }
}