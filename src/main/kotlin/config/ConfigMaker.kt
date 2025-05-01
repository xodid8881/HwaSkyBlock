package org.hwabeag.hwaskyblock.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigMaker(path: String?, fileName: String?) {
    private val file: File? = File("$path/$fileName")
    var config: FileConfiguration?
        private set

    init {
        this.config = this.file?.let { YamlConfiguration.loadConfiguration(it) }
    }

    fun saveConfig() {
        if (config == null) return
        try {
            this.file?.let { this.config!!.save(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exists(): Boolean {
        return file != null && file.exists()
    }

    fun reloadConfig() {
        if (!exists()) return
        config = file?.let { YamlConfiguration.loadConfiguration(it) }
    }
}