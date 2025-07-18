package org.hwabeag.hwaskyblock.database.mysql

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.hwabeag.hwaskyblock.HwaSkyBlock
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager

class MySQLManager(private val plugin: JavaPlugin) {
    private lateinit var connection: Connection
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    fun init() {
        val host = Config.getString("database.mysql.host") ?: "localhost"
        val port = Config.getInt("database.mysql.port")
        val database = Config.getString("database.mysql.database") ?: "hwaskyblock"
        val username = Config.getString("database.mysql.user") ?: "root"
        val password = Config.getString("database.mysql.password") ?: ""
        val url = "jdbc:mysql://$host:$port/$database?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Seoul"

        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(url, username, password)
        plugin.logger.info("✅ MySQL 연결 완료")
        instance = this
    }

    fun getConnection(): Connection {
        return connection
    }

    fun close() {
        connection.close()
    }

    companion object {
        private lateinit var instance: MySQLManager

        fun get(): MySQLManager {
            if (!::instance.isInitialized) {
                throw IllegalStateException("MySQLManager is not initialized!")
            }
            return instance
        }

        fun getConnection(): Connection {
            return get().getConnection()
        }

        fun init(block: HwaSkyBlock) {
            val manager = MySQLManager(block)
            manager.init()
        }
    }
}
