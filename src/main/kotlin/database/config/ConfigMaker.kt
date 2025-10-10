package org.hwabaeg.hwaskyblock.database.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigMaker(path: String?, fileName: String?) {
    private val file: File = File("$path/$fileName")
    var config: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        private set

    fun saveConfig() {
        try {
            config.save(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exists(): Boolean {
        return file.exists()
    }

    fun reloadConfig() {
        if (!exists()) return
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun getFile(): File {
        return file
    }
}
