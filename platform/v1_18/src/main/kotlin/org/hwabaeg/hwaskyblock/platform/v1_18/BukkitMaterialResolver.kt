package org.hwabaeg.hwaskyblock.platform.v1_18

import org.bukkit.Material
import org.hwabaeg.hwaskyblock.platform.MaterialResolver

class BukkitMaterialResolver : MaterialResolver {
    private val aliases = mapOf(
        "TORCHFLOWER_SEEDS" to "WHEAT_SEEDS",
        "PITCHER_POD" to "WHEAT_SEEDS",
        "MANGROVE_LOG" to "OAK_LOG",
        "MANGROVE_PLANKS" to "OAK_PLANKS",
        "MANGROVE_FENCE" to "OAK_FENCE",
        "MANGROVE_FENCE_GATE" to "OAK_FENCE_GATE",
        "MANGROVE_DOOR" to "OAK_DOOR",
        "MANGROVE_TRAPDOOR" to "OAK_TRAPDOOR",
        "MANGROVE_BUTTON" to "OAK_BUTTON",
        "CHERRY_LOG" to "OAK_LOG",
        "CHERRY_PLANKS" to "OAK_PLANKS",
        "CHERRY_FENCE" to "OAK_FENCE",
        "CHERRY_FENCE_GATE" to "OAK_FENCE_GATE",
        "CHERRY_DOOR" to "OAK_DOOR",
        "CHERRY_TRAPDOOR" to "OAK_TRAPDOOR",
        "CHERRY_BUTTON" to "OAK_BUTTON",
        "BAMBOO_MOSAIC" to "OAK_PLANKS",
        "BAMBOO_MOSAIC_STAIRS" to "OAK_STAIRS",
        "BAMBOO_MOSAIC_SLAB" to "OAK_SLAB",
        "BREEZE_SPAWN_EGG" to "COW_SPAWN_EGG",
        "ARMADILLO_SPAWN_EGG" to "COW_SPAWN_EGG"
    )

    override fun fromKeyOrNull(key: String): Material? {
        return resolve(key.trim().uppercase())
    }

    private fun resolve(key: String): Material? {
        val direct = Material.matchMaterial(key) ?: runCatching { Material.valueOf(key) }.getOrNull()
        if (direct != null) return direct
        val alias = aliases[key] ?: return null
        return resolve(alias)
    }
}
