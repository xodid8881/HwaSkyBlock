package org.hwabaeg.hwaskyblock.database.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class ConfigManager {
    companion object {
        private var plugin: JavaPlugin? = null
        private val configSet = HashMap<String, ConfigMaker>()

        fun setupConfigs(p: JavaPlugin) {
            plugin = p
            val path = plugin!!.dataFolder.absolutePath

            configSet["setting"] = ConfigMaker(path, "config.yml")
            configSet["message"] = ConfigMaker(path, "message.yml")

            configSet["setting"]?.reloadConfig()
            configSet["message"]?.reloadConfig()

            configSet.forEach { (_, configMaker) ->
                val file = configMaker.getFile()
                if (!file.exists()) {
                    plugin?.saveResource(file.name, false)
                }
            }
            reloadConfigs()
        }


        fun getConfig(fileName: String): FileConfiguration? =
            configSet[fileName]?.config

        fun reloadConfigs() {
            configSet.forEach { (key, config) ->
                plugin?.logger?.info("🔄 Reloading $key.yml")
                config.reloadConfig()
            }
        }

        fun saveConfigs() {
            configSet.forEach { (_, config) ->
                config.saveConfig()
            }
        }
    }
}
