package org.hwabeag.hwaskyblock.database.mysql.user

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class SelectUser {
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

    fun UserSelect(player: Player): Int {
        val playerUUID = player.uniqueId.toString()
        val user = hwaskyblock_user()
        var conn: Connection? = null

        try {
            conn = openConnection()
            val sql = """
                SELECT player_uuid, player_setting, player_possession_count, player_pos, player_page
                FROM hwaskyblock_user
                WHERE player_uuid = ?
            """.trimIndent()

            val pstmt = conn!!.prepareStatement(sql)
            pstmt.setString(1, playerUUID)
            val rs = pstmt.executeQuery()

            if (rs.next()) {
                user.setPlayerUuid(rs.getString("player_uuid"))
                user.setPlayerSetting(rs.getString("player_setting"))
                user.setPlayerPossessionCount(rs.getInt("player_possession_count"))
                user.setPlayerPos(rs.getInt("player_pos"))
                user.setPlayerPage(rs.getInt("player_page"))

                // 전역 Map 등록
                DatabaseManager.Select_User_List[playerUUID] = user

                rs.close()
                pstmt.close()
                return 0
            } else {
                rs.close()
                pstmt.close()
                return 1
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                conn?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return 2
    }
}
