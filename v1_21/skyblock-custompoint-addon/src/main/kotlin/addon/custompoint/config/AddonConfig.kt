package org.hwabaeg.hwaskyblock.addon.custompoint.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object AddonConfig {

    private lateinit var config: FileConfiguration

    var enabled = true

    var enableVanillaBlock = true
    var enableCustomBlock = true
    var enableCustomCrops = true

    var debug = false

    fun load(plugin: JavaPlugin) {

        val dir = File(plugin.dataFolder, "addons/SkyblockCustomPointAddon")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "config.yml")

        if (!file.exists()) {
            val jarFile = File(
                AddonConfig::class.java
                    .protectionDomain
                    .codeSource
                    .location
                    .toURI()
            )

            java.util.jar.JarFile(jarFile).use { jar ->
                jar.getJarEntry("config.yml")?.let { entry ->
                    jar.getInputStream(entry).use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                } ?: plugin.logger.severe(
                    "[SkyblockCustomPointAddon] config.yml not found in addon jar"
                )
            }
        }

        config = YamlConfiguration.loadConfiguration(file)
        read()
    }

    private fun read() {
        enabled = config.getBoolean("enabled", true)

        enableVanillaBlock = config.getBoolean("feature.vanilla-block", true)
        enableCustomBlock = config.getBoolean("feature.custom-block", true)
        enableCustomCrops = config.getBoolean("feature.custom-crops", true)

        debug = config.getBoolean("debug", false)
    }
}
