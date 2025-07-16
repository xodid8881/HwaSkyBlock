package database.user

import database.utils.hwaskyblock_user
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.config.ConfigManager
import org.hwabeag.hwaskyblock.events.click.InvBuyClickEvent
import org.hwabeag.hwaskyblock.events.click.InvGlobalFragClickEvent
import org.hwabeag.hwaskyblock.events.click.InvGlobalUseClickEvent
import org.hwabeag.hwaskyblock.events.click.InvMenuClickEvent
import org.hwabeag.hwaskyblock.events.click.InvSettingClickEvent
import org.hwabeag.hwaskyblock.events.click.InvSharerClickEvent
import org.hwabeag.hwaskyblock.events.click.InvSharerUseClickEvent
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

                val createStr = "CREATE TABLE IF NOT EXISTS hwaskyblock_user(player_uuid varchar(50) not null, player_setting varchar(50) not null, player_possession_count varchar(50) not null, player_pos varchar(50) not null, player_page varchar(50) not null)"

                statement!!.executeUpdate(createStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun UserSelect(player: Player): Int {
        val player_UUID = player.uniqueId
        val user: hwaskyblock_user = hwaskyblock_user()
        var conn: Connection? = null
        try {
            conn = this.openConnection()
            val sql = "SELECT player_uuid, player_setting, player_possession_count, player_pos, player_page " +
                    "FROM hwaskyblock_user " +
                    "WHERE player_uuid=?"
            val pstmt = conn!!.prepareStatement(sql)
            pstmt.setString(1, player_UUID.toString())
            val rs = pstmt.executeQuery()
            if (rs.next()) {
                user.setPlayerUuid(rs.getString("player_uuid"))
                user.setPlayerSetting(rs.getString("player_setting"))
                user.setPlayerPossessionCount(rs.getInt("player_possession_count"))
                user.setPlayerPos(rs.getInt("player_pos"))
                user.setPlayerPage(rs.getInt("player_page"))
                InvBuyClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvGlobalFragClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvGlobalUseClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvMenuClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvSettingClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvSharerClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
                InvSharerUseClickEvent.Select_User_List.put(rs.getString("player_uuid"), user)
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
                conn!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return 2
    }
}
