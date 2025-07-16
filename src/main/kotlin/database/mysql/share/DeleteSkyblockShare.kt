package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DeleteSkyblockShare {
    private var connection: Connection? = null
    private var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

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
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun deleteShare(skyblockId: String, playerName: String) {
        val conn = openConnection() ?: return

        try {
            val sql = "DELETE FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, skyblockId)
                pstmt.setString(2, playerName)
                pstmt.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
