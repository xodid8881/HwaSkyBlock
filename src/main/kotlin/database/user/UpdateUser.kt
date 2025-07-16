package database.user

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.config.ConfigManager
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

                val createStr = "CREATE TABLE IF NOT EXISTS hwaskyblock_user(player_uuid varchar(50) not null, player_setting varchar(50) not null, player_possession_count varchar(50) not null, player_pos varchar(50) not null, player_page varchar(50) not null)"

                statement!!.executeUpdate(createStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun UserUpdate_Setting(player: Player, setting: String) {
        val player_UUID = player.uniqueId
        try {
            this.openConnection().use { conn ->
                val sql =
                    "UPDATE hwaskyblock_user SET player_setting=$setting WHERE player_uuid='$player_UUID'"
                val pstmt = conn!!.prepareStatement(sql)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Possession_Count(player: Player, player_possession_count: Int) {
        val player_UUID = player.uniqueId
        try {
            this.openConnection().use { conn ->
                val sql =
                    "UPDATE hwaskyblock_user SET player_possession_count=$player_possession_count WHERE player_uuid='$player_UUID'"
                val pstmt = conn!!.prepareStatement(sql)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Pos(player: Player, player_pos: Int) {
        val player_UUID = player.uniqueId
        try {
            this.openConnection().use { conn ->
                val sql =
                    "UPDATE hwaskyblock_user SET player_pos=$player_pos WHERE player_uuid='$player_UUID'"
                val pstmt = conn!!.prepareStatement(sql)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun UserUpdate_Page(player: Player, player_page: Int) {
        val player_UUID = player.uniqueId
        try {
            this.openConnection().use { conn ->
                val sql =
                    "UPDATE hwaskyblock_user SET player_page=$player_page WHERE player_uuid='$player_UUID'"
                val pstmt = conn!!.prepareStatement(sql)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}