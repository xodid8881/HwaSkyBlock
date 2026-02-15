package org.hwabaeg.hwaskyblock.addon.cattle.model

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.java.JavaPlugin

data class CattleDefinition(
    val id: String,
    val type: EntityType,
    val displayName: String,
    val shopPrice: Double,
    val feed: FeedRule,
    val breeding: BreedingRule,
    val drops: Map<String, DropRule>,
    val hologram: HologramRule
)

data class FeedRule(
    val item: String,
    val dailyAmount: Int
)

data class BreedingRule(
    val item: String,
    val cooldownHours: Long,
    val durationSeconds: Long
)

data class DropRule(
    val item: String,
    val min: Int,
    val max: Int
)

data class HologramRule(
    val feedFormat: String,
    val breedFormat: String
)

data class FeedDefinition(
    val id: String,
    val material: String,
    val displayName: String,
    val lore: List<String>
)

data class BreedingItemDefinition(
    val id: String,
    val material: String,
    val displayName: String,
    val lore: List<String>,
    val maxUses: Int,
    val durationSeconds: Long
)

data class CustomItemDefinition(
    val id: String,
    val material: String,
    val displayName: String,
    val lore: List<String>
)

enum class CustomItemType {
    FEED,
    BREED,
    SPAWN
}

data class KeyRing(
    val managed: NamespacedKey,
    val cattleId: NamespacedKey,
    val feedDay: NamespacedKey,
    val feedCount: NamespacedKey,
    val breedReadyAt: NamespacedKey,
    val breedMarkedUntil: NamespacedKey,
    val itemType: NamespacedKey,
    val itemId: NamespacedKey,
    val itemUses: NamespacedKey,
    val itemMaxUses: NamespacedKey
) {
    constructor(plugin: JavaPlugin) : this(
        NamespacedKey(plugin, "cattle_managed"),
        NamespacedKey(plugin, "cattle_id"),
        NamespacedKey(plugin, "feed_day"),
        NamespacedKey(plugin, "feed_count"),
        NamespacedKey(plugin, "breed_ready_at"),
        NamespacedKey(plugin, "breed_marked_until"),
        NamespacedKey(plugin, "item_type"),
        NamespacedKey(plugin, "item_id"),
        NamespacedKey(plugin, "item_uses"),
        NamespacedKey(plugin, "item_max_uses")
    )
}

class ShopHolder : InventoryHolder {
    override fun getInventory(): Inventory = Bukkit.createInventory(null, 9)
}
