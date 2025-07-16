package org.hwabeag.hwaskyblock.database.mysql.user

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class UpdateUser {
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

    fun UserUpdate_Setting(player: Player, setting: String) {
        val playerUUID = player.uniqueId.toString()
        try {
            this.openConnection().use { conn ->
                val sql = "UPDATE hwaskyblock_user SET player_setting = ? WHERE player_uuid = ?"
                conn!!.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, setting)
                    pstmt.setString(2, playerUUID)
                    pstmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Possession_Count(player: Player, count: Int) {
        val playerUUID = player.uniqueId.toString()
        try {
            this.openConnection().use { conn ->
                val sql = "UPDATE hwaskyblock_user SET player_possession_count = ? WHERE player_uuid = ?"
                conn!!.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, count)
                    pstmt.setString(2, playerUUID)
                    pstmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Pos(player: Player, pos: Int) {
        val playerUUID = player.uniqueId.toString()
        try {
            this.openConnection().use { conn ->
                val sql = "UPDATE hwaskyblock_user SET player_pos = ? WHERE player_uuid = ?"
                conn!!.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, pos)
                    pstmt.setString(2, playerUUID)
                    pstmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Page(player: Player, page: Int) {
        val playerUUID = player.uniqueId.toString()
        try {
            this.openConnection().use { conn ->
                val sql = "UPDATE hwaskyblock_user SET player_page = ? WHERE player_uuid = ?"
                conn!!.prepareStatement(sql).use { pstmt ->
                    pstmt.setInt(1, page)
                    pstmt.setString(2, playerUUID)
                    pstmt.executeUpdate()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
