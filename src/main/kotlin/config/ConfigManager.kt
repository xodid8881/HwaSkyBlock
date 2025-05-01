package org.hwabeag.hwaskyblock.config

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.HwaSkyBlock

class ConfigManager {
    init {
        val path = plugin!!.dataFolder.absolutePath
        configSet.put("setting", ConfigMaker(path, "config.yml"))
        configSet.put("message", ConfigMaker(path, "message.yml"))
        configSet.put("skyblock", ConfigMaker(path, "skyblock.yml"))
        configSet.put("player", ConfigMaker(path, "player.yml"))
        loadSettings()
        saveConfigs()
    }

    companion object {
        private val plugin: HwaSkyBlock? = HwaSkyBlock.plugin

        private val configSet = HashMap<String?, ConfigMaker?>()

        fun reloadConfigs() {
            for (key in configSet.keys) {
                plugin!!.logger.info(key)
                configSet.get(key)!!.reloadConfig()
            }
        }

        fun saveConfigs() {
            for (key in configSet.keys) configSet.get(key)!!.saveConfig()
        }

        fun getConfig(fileName: String?): FileConfiguration? {
            return configSet.get(fileName)!!.config
        }

        fun loadSettings() {
            saveConfigs()
        }
    }
}