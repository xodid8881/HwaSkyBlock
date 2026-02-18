package org.hwabaeg.hwaskyblock.addon.custompoint.config

import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.platform.MaterialResolver
import org.hwabaeg.hwaskyblock.platform.PlatformFactory
import java.io.File
import kotlin.random.Random

object BlockPointConfig {

    private lateinit var config: FileConfiguration

    private val vanillaBlockPoints: MutableMap<Material, Int> = mutableMapOf()
    private val customBlockPoints: MutableMap<String, Int> = mutableMapOf()
    private val customCropPoints: MutableMap<String, Int> = mutableMapOf()
    private lateinit var materialResolver: MaterialResolver

    fun load(plugin: JavaPlugin) {
        materialResolver = PlatformFactory.createMaterialResolver(plugin.server)

        val dir = File(plugin.dataFolder, "addons/SkyblockCustomPointAddon")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "block-point.yml")

        if (!file.exists()) {
            val input = javaClass.classLoader
                .getResourceAsStream("block-point.yml")
                ?: throw IllegalStateException("block-point.yml not found inside addon jar")

            input.use { inputStream ->
                file.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
            }
        }

        config = YamlConfiguration.loadConfiguration(file)
        read()
    }

    private fun read() {
        vanillaBlockPoints.clear()
        customBlockPoints.clear()
        customCropPoints.clear()

        loadVanillaBlocks()
        loadCustomBlocks()
        loadCustomCrops()
    }

    // 🔧 핵심 수정 부분
    private fun loadVanillaBlocks() {
        val section = config.getConfigurationSection("vanilla-blocks") ?: return

        for (key in section.getKeys(false)) {
            runCatching {
                val material = materialResolver.fromKeyOrNull(key) ?: return@runCatching
                val blockSection = section.getConfigurationSection(key) ?: return@runCatching

                val mode = blockSection.getString("mode")?.uppercase() ?: return@runCatching

                val fixed = blockSection.getInt("fixed", 0)
                val min = blockSection.getInt("random.min", 0)
                val max = blockSection.getInt("random.max", 0)

                val point = when (mode) {
                    "FIXED" -> fixed
                    "RANDOM" -> {
                        if (min > 0 && max >= min) Random.nextInt(min, max + 1) else 0
                    }
                    "BOTH" -> {
                        val random = if (min > 0 && max >= min)
                            Random.nextInt(min, max + 1)
                        else 0
                        fixed + random
                    }
                    else -> 0
                }

                if (point > 0) {
                    vanillaBlockPoints[material] = point
                }
            }
        }
    }

    private fun loadCustomBlocks() {
        val section = config.getConfigurationSection("custom-blocks") ?: return
        for (key in section.getKeys(false)) {
            customBlockPoints[key] = section.getInt(key)
        }
    }

    private fun loadCustomCrops() {
        val section = config.getConfigurationSection("custom-crops") ?: return

        for (key in section.getKeys(false)) {
            runCatching {
                val cropSection = section.getConfigurationSection(key) ?: return@runCatching

                val mode = cropSection.getString("mode")?.uppercase() ?: return@runCatching

                val fixed = cropSection.getInt("fixed", 0)
                val min = cropSection.getInt("random.min", 0)
                val max = cropSection.getInt("random.max", 0)

                val point = when (mode) {
                    "FIXED" -> fixed
                    "RANDOM" -> {
                        if (min in 1..max)
                            kotlin.random.Random.nextInt(min, max + 1)
                        else 0
                    }
                    "BOTH" -> {
                        val random =
                            if (min in 1..max)
                                kotlin.random.Random.nextInt(min, max + 1)
                            else 0
                        fixed + random
                    }
                    else -> 0
                }

                if (point > 0) {
                    customCropPoints[key] = point
                }
            }
        }
    }

    fun getPoint(id: String): Int? {
        return customBlockPoints[id] ?: customCropPoints[id]
    }

    fun getPoint(material: Material): Int? {
        return vanillaBlockPoints[material]
    }
}
