package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class UpdateSkyblockShare {
    private var connection: Connection? = null
    private val config: FileConfiguration = ConfigManager.getConfig("setting")!!

    private fun openConnection(): Connection? {
        try {
            if (connection != null && !connection!!.isClosed) return connection
            synchronized(this) {
                if (connection != null && !connection!!.isClosed) return connection

                Class.forName("com.mysql.jdbc.Driver")
                val host = config.getString("database.mysql.host")
                val port = config.getInt("database.mysql.port")
                val database = config.getString("database.mysql.database")
                val username = config.getString("database.mysql.user")
                val password = config.getString("database.mysql.password")

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

    fun updatePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        val allowedPermissions = setOf(
            "can_break", "can_place",
            "use_door", "use_chest", "use_barrel", "use_hopper",
            "use_furnace", "use_blast_furnace", "use_shulker_box",
            "use_trapdoor", "use_button", "use_anvil",
            "use_farm", "use_beacon", "use_minecart", "use_boat"
        )

        if (!allowedPermissions.contains(permission)) {
            println("⛔ 잘못된 권한 필드 요청: $permission")
            return
        }

        try {
            val conn = openConnection() ?: return
            val sql = """
                UPDATE hwaskyblock_share
                SET $permission = ?
                WHERE skyblock_id = ? AND player_name = ?
            """.trimIndent()

            val pstmt: PreparedStatement = conn.prepareStatement(sql)
            pstmt.setBoolean(1, value)
            pstmt.setString(2, skyblockId)
            pstmt.setString(3, playerName)

            pstmt.executeUpdate()
            pstmt.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
