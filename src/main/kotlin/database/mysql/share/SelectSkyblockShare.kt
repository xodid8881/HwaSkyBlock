package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class SelectSkyblockShare {
    private var connection: Connection? = null
    private var statement: Statement? = null
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
                statement = connection!!.createStatement()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun getShareList(skyblockId: String): List<String> {
        val shareList = mutableListOf<String>()
        try {
            val conn = openConnection() ?: return shareList

            val sql = "SELECT player_name FROM hwaskyblock_share WHERE skyblock_id = ?"
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, skyblockId)

            val rs = pstmt.executeQuery()
            while (rs.next()) {
                shareList.add(rs.getString("player_name"))
            }

            rs.close()
            pstmt.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return shareList
    }

    fun getSharePermissions(skyblockId: String, playerName: String): Map<String, Boolean> {
        val permissions = mutableMapOf<String, Boolean>()
        try {
            val conn = openConnection() ?: return permissions

            val sql = "SELECT * FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, skyblockId)
            pstmt.setString(2, playerName)

            val rs = pstmt.executeQuery()
            if (rs.next()) {
                permissions["can_break"] = rs.getBoolean("can_break")
                permissions["can_place"] = rs.getBoolean("can_place")
                permissions["use_door"] = rs.getBoolean("use_door")
                permissions["use_chest"] = rs.getBoolean("use_chest")
                permissions["use_barrel"] = rs.getBoolean("use_barrel")
                permissions["use_hopper"] = rs.getBoolean("use_hopper")
                permissions["use_furnace"] = rs.getBoolean("use_furnace")
                permissions["use_blast_furnace"] = rs.getBoolean("use_blast_furnace")
                permissions["use_shulker_box"] = rs.getBoolean("use_shulker_box")
                permissions["use_trapdoor"] = rs.getBoolean("use_trapdoor")
                permissions["use_button"] = rs.getBoolean("use_button")
                permissions["use_anvil"] = rs.getBoolean("use_anvil")
                permissions["use_farm"] = rs.getBoolean("use_farm")
                permissions["use_beacon"] = rs.getBoolean("use_beacon")
                permissions["use_minecart"] = rs.getBoolean("use_minecart")
                permissions["use_boat"] = rs.getBoolean("use_boat")

                DatabaseManager.Select_Share_List[skyblockId] = permissions
            }

            rs.close()
            pstmt.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return permissions
    }

    fun getPermission(skyblockId: String, playerName: String, permission: String): Boolean? {
        var result: Boolean? = null
        try {
            val conn = openConnection() ?: return null

            val sql = "SELECT $permission FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
            val pstmt = conn.prepareStatement(sql)
            pstmt.setString(1, skyblockId)
            pstmt.setString(2, playerName)

            val rs = pstmt.executeQuery()
            if (rs.next()) {
                result = rs.getBoolean(permission)
            }

            rs.close()
            pstmt.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }

}
