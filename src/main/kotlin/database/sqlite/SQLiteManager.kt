package org.hwabaeg.hwaskyblock.database.sqlite

import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.HwaSkyBlock
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SQLiteManager(private val plugin: JavaPlugin) {
    private lateinit var connection: Connection

    fun init() {
        val file = File(plugin.dataFolder, "data.db")
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        connection = DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")
        plugin.logger.info("✅ SQLite 연결 완료")
        instance = this
    }

    fun getConnection(): Connection {
        return connection
    }

    fun close() {
        connection.close()
    }

    companion object {
        private lateinit var instance: SQLiteManager

        fun get(): SQLiteManager {
            if (!::instance.isInitialized) {
                throw IllegalStateException("SQLiteManager is not initialized!")
            }
            return instance
        }

        fun getConnection(): Connection {
            return get().getConnection()
        }

        fun init(block: HwaSkyBlock) {
            val manager = SQLiteManager(block)
            manager.init()
        }
    }
}