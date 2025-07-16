package org.hwabeag.hwaskyblock.database.mysql.user

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class InsertUser {
    private var connection: Connection? = null
    private var statement: Statement? = null

    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    fun openConnection(): Connection? {
        try {
            if (connection != null && !connection!!.isClosed) return connection

            synchronized(this) {
                if (connection != null && !connection!!.isClosed) return connection

                Class.forName("com.mysql.jdbc.Driver")
                val host = Config.getString("database.mysql.host")
                val port = Config.getInt("database.mysql.port")
                val database = Config.getString("database.mysql.database")
                val username = Config.getString("database.mysql.user")
                val password = Config.getString("database.mysql.password")

                connection = DriverManager.getConnection(
                    "jdbc:mysql://$host:$port/$database?autoReconnect=true",
                    username,
                    password
                )
                statement = connection!!.createStatement()

                val createStr = """
                    CREATE TABLE IF NOT EXISTS hwaskyblock_user (
                        player_uuid VARCHAR(50) NOT NULL PRIMARY KEY,
                        player_setting VARCHAR(50) NOT NULL,
                        player_possession_count INT NOT NULL,
                        player_pos INT NOT NULL,
                        player_page INT NOT NULL
                    )
                """.trimIndent()

                statement!!.executeUpdate(createStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun UserInsert(player: Player) {
        val player_UUID = player.uniqueId.toString()
        try {
            openConnection()?.use { conn ->
                val sql = """
                    INSERT INTO hwaskyblock_user 
                    (player_uuid, player_setting, player_possession_count, player_pos, player_page) 
                    VALUES (?, ?, ?, ?, ?)
                """.trimIndent()
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, player_UUID)
                    pstmt.setString(2, player_UUID) // player_setting은 기본값으로 UUID 사용
                    pstmt.setInt(3, 0)
                    pstmt.setInt(4, 0)
                    pstmt.setInt(5, 0)
                    pstmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
