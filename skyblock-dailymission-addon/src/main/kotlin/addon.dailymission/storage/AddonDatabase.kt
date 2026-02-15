package addon.dailymission.storage

import addon.dailymission.config.AddonConfig
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class AddonDatabase(
    private val type: DatabaseType,
    private val plugin: JavaPlugin,
    private val config: AddonConfig
) {
    private lateinit var connection: Connection

    fun init(): Connection {
        connection = when (type) {
            DatabaseType.SQLITE -> {
                val dir = File(plugin.dataFolder, "addons/SkyblockDailyMissionAddon")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, config.sqliteFile)
                DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")
            }

            DatabaseType.MYSQL -> {
                val url =
                    "jdbc:mysql://${config.mysqlHost}:${config.mysqlPort}/${config.mysqlDatabase}" +
                        "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Seoul"
                Class.forName("com.mysql.cj.jdbc.Driver")
                DriverManager.getConnection(url, config.mysqlUser, config.mysqlPassword)
            }
        }
        return connection
    }

    fun getConnection(): Connection = connection

    fun close() {
        if (this::connection.isInitialized) connection.close()
    }
}
