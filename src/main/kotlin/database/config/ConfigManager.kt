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

            configSet["setting"]?.reloadConfig()
            configSet["message"]?.reloadConfig()

            val dbType = getConfig("setting")?.getString("database.type") ?: "yml"

            if (dbType == "yml") {
                configSet["skyblock"] = ConfigMaker(path, "skyblock.yml")
                configSet["player"] = ConfigMaker(path, "player.yml")
            }

            configSet.forEach { (_, configMaker) ->
                val file = configMaker.getFile()
                if (!file.exists()) {
                    if (file.name == "skyblock.yml" || file.name == "player.yml") {
                        file.createNewFile()
                        plugin?.logger?.info("✅ ${file.name} 파일이 없어서 새로 생성했습니다.")
                    } else {
                        plugin?.saveResource(file.name, false)
                    }
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
