package database.user

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.config.ConfigManager
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

                val createStr = "CREATE TABLE IF NOT EXISTS hwaskyblock_user(player_uuid varchar(50) not null, player_setting varchar(50) not null, player_possession_count varchar(50) not null, player_pos varchar(50) not null, player_page varchar(50) not null)"

                statement!!.executeUpdate(createStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun UserInsert(player: Player) {
        val player_UUID = player.uniqueId
        try {
            this.openConnection().use { conn ->
                val sql =
                    "INSERT INTO hwaskyblock_user (player_uuid, player_setting, player_possession_count, player_pos, player_page) " +
                            "VALUES (?, ?)"
                val pstmt = conn!!.prepareStatement(sql)
                pstmt.setString(1, player_UUID.toString())
                pstmt.setString(2, player_UUID.toString())
                pstmt.setInt(3, 0)
                pstmt.setInt(4, 0)
                pstmt.setInt(5, 0)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
