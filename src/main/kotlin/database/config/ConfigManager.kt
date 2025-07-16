package org.hwabeag.hwaskyblock.database.config

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
            configSet["skyblock"] = ConfigMaker(path, "skyblock.yml")
            configSet["player"] = ConfigMaker(path, "player.yml")
            saveConfigs()
        }

        fun getConfig(fileName: String): FileConfiguration? = configSet[fileName]?.config

        fun reloadConfigs() {
            for ((key, config) in configSet) {
                plugin?.logger?.info("Reloading $key")
                config?.reloadConfig()
            }
        }

        fun saveConfigs() {
            for ((_, config) in configSet) {
                config?.saveConfig()
            }
        }
    }
}