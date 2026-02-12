package org.hwabaeg.hwaskyblock.addon.cattle.config

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.addon.cattle.model.BreedingItemDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.BreedingRule
import org.hwabaeg.hwaskyblock.addon.cattle.model.CattleDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.CustomItemDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.DropRule
import org.hwabaeg.hwaskyblock.addon.cattle.model.FeedDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.FeedRule
import org.hwabaeg.hwaskyblock.addon.cattle.model.HologramRule
import java.io.File
import java.util.jar.JarFile

data class AddonConfig(
    val cattles: Map<String, CattleDefinition>,
    val feeds: Map<String, FeedDefinition>,
    val breedingItems: Map<String, BreedingItemDefinition>,
    val customItems: Map<String, CustomItemDefinition>
) {
    companion object {
        fun load(plugin: JavaPlugin): AddonConfig {
            val dir = File(plugin.dataFolder, "addons/SkyblockCattleAddon")
            if (!dir.exists()) dir.mkdirs()

            ensure(dir, "cattle.yml")
            ensure(dir, "feed.yml")
            ensure(dir, "breeding.yml")
            ensure(dir, "items.yml")

            val cattleCfg = YamlConfiguration.loadConfiguration(File(dir, "cattle.yml"))
            val feedCfg = YamlConfiguration.loadConfiguration(File(dir, "feed.yml"))
            val breedCfg = YamlConfiguration.loadConfiguration(File(dir, "breeding.yml"))
            val itemCfg = YamlConfiguration.loadConfiguration(File(dir, "items.yml"))

            val items = parseItems(itemCfg)
            val feeds = parseFeeds(feedCfg)
            val breeds = parseBreedItems(breedCfg)
            val cattles = parseCattles(cattleCfg)
            return AddonConfig(cattles, feeds, breeds, items)
        }

        private fun ensure(dir: File, path: String) {
            val out = File(dir, path)
            if (out.exists()) return
            val jarFile = File(AddonConfig::class.java.protectionDomain.codeSource.location.toURI())
            JarFile(jarFile).use { jar ->
                val entry = jar.getJarEntry(path) ?: return
                jar.getInputStream(entry).use { input ->
                    out.outputStream().use { output -> input.copyTo(output) }
                }
            }
        }

        private fun parseItems(cfg: FileConfiguration): Map<String, CustomItemDefinition> {
            val section = cfg.getConfigurationSection("items") ?: return emptyMap()
            val map = LinkedHashMap<String, CustomItemDefinition>()
            for (id in section.getKeys(false)) {
                val s = section.getConfigurationSection(id) ?: continue
                map[id] = CustomItemDefinition(
                    id = id,
                    material = s.getString("material", "STONE")!!,
                    displayName = s.getString("display-name", "&f커스텀 아이템")!!,
                    lore = s.getStringList("lore")
                )
            }
            return map
        }

        private fun parseFeeds(cfg: FileConfiguration): Map<String, FeedDefinition> {
            val section = cfg.getConfigurationSection("feeds") ?: return emptyMap()
            val map = LinkedHashMap<String, FeedDefinition>()
            for (id in section.getKeys(false)) {
                val s = section.getConfigurationSection(id) ?: continue
                map[id] = FeedDefinition(
                    id = id,
                    material = s.getString("material", "WHEAT")!!,
                    displayName = s.getString("display-name", "&a특수 급식")!!,
                    lore = s.getStringList("lore")
                )
            }
            return map
        }

        private fun parseBreedItems(cfg: FileConfiguration): Map<String, BreedingItemDefinition> {
            val section = cfg.getConfigurationSection("breeding-items") ?: return emptyMap()
            val map = LinkedHashMap<String, BreedingItemDefinition>()
            for (id in section.getKeys(false)) {
                val s = section.getConfigurationSection(id) ?: continue
                map[id] = BreedingItemDefinition(
                    id = id,
                    material = s.getString("material", "NETHER_STAR")!!,
                    displayName = s.getString("display-name", "&d가축 번식 토큰")!!,
                    lore = s.getStringList("lore"),
                    maxUses = s.getInt("max-uses", 2).coerceAtLeast(1),
                    durationSeconds = s.getLong("duration-seconds", 600L).coerceAtLeast(1L)
                )
            }
            return map
        }

        private fun parseCattles(cfg: FileConfiguration): Map<String, CattleDefinition> {
            val section = cfg.getConfigurationSection("cattles") ?: return emptyMap()
            val map = LinkedHashMap<String, CattleDefinition>()
            for (id in section.getKeys(false)) {
                val s = section.getConfigurationSection(id) ?: continue
                map[id] = CattleDefinition(
                    id = id,
                    type = parseEntityType(s.getString("type", "COW")!!),
                    displayName = s.getString("display-name", "&f기본 가축")!!,
                    shopPrice = s.getDouble("shop-price", 0.0),
                    feed = parseFeedRule(s.getConfigurationSection("feed")),
                    breeding = parseBreedingRule(s.getConfigurationSection("breeding")),
                    drops = parseDrops(s.getConfigurationSection("drops")),
                    hologram = parseHologram(s.getConfigurationSection("hologram"))
                )
            }
            return map
        }

        private fun parseEntityType(name: String): EntityType {
            val entityType = runCatching { EntityType.valueOf(name.uppercase()) }.getOrNull()
            return entityType ?: EntityType.COW
        }

        private fun parseFeedRule(section: ConfigurationSection?): FeedRule {
            if (section == null) return FeedRule("special_feed", 5)
            return FeedRule(
                item = section.getString("item", "special_feed")!!,
                dailyAmount = section.getInt("daily-amount", 5).coerceAtLeast(1)
            )
        }

        private fun parseBreedingRule(section: ConfigurationSection?): BreedingRule {
            if (section == null) return BreedingRule("breeding_token", 168, 600L)
            return BreedingRule(
                item = section.getString("item", "breeding_token")!!,
                cooldownHours = section.getLong("cooldown-hours", 168L).coerceAtLeast(0L),
                durationSeconds = section.getLong("duration-seconds", 600L).coerceAtLeast(1L)
            )
        }

        private fun parseDrops(section: ConfigurationSection?): Map<String, DropRule> {
            if (section == null) return emptyMap()
            val result = LinkedHashMap<String, DropRule>()
            for (key in section.getKeys(false)) {
                val s = section.getConfigurationSection(key) ?: continue
                result[key] = DropRule(
                    item = s.getString("item", key)!!,
                    min = s.getInt("min", 1),
                    max = s.getInt("max", 1)
                )
            }
            return result
        }

        private fun parseHologram(section: ConfigurationSection?): HologramRule {
            if (section == null) return HologramRule("&a급식 {current}/{max}", "&e번식 대기 {hours}시간")
            return HologramRule(
                feedFormat = section.getString("feed-format", "&a급식 {current}/{max}")!!,
                breedFormat = section.getString("breed-format", "&e번식 대기 {hours}시간")!!
            )
        }
    }
}
