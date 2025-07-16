package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class DeleteSkyblock {

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
                    CREATE TABLE IF NOT EXISTS hwaskyblock_skyblock (
                        skyblock_id VARCHAR(50) NOT NULL,
                        skyblock_name VARCHAR(50) NOT NULL,
                        skyblock_leader VARCHAR(50) NOT NULL,
                        skyblock_join VARCHAR(50) NOT NULL,
                        skyblock_break VARCHAR(50) NOT NULL,
                        skyblock_place VARCHAR(50) NOT NULL,
                        skyblock_door VARCHAR(50) NOT NULL,
                        skyblock_chest VARCHAR(50) NOT NULL,
                        skyblock_barrel VARCHAR(50) NOT NULL,
                        skyblock_hopper VARCHAR(50) NOT NULL,
                        skyblock_furnace VARCHAR(50) NOT NULL,
                        skyblock_blast_furnace VARCHAR(50) NOT NULL,
                        skyblock_shulker_box VARCHAR(50) NOT NULL,
                        skyblock_trapdoor VARCHAR(50) NOT NULL,
                        skyblock_button VARCHAR(50) NOT NULL,
                        skyblock_anvil VARCHAR(50) NOT NULL,
                        skyblock_farm VARCHAR(50) NOT NULL,
                        skyblock_beacon VARCHAR(50) NOT NULL,
                        skyblock_minecart VARCHAR(50) NOT NULL,
                        skyblock_boat VARCHAR(50) NOT NULL,
                        skyblock_pvp VARCHAR(50) NOT NULL,
                        skyblock_welcome_message VARCHAR(100) NOT NULL,
                        skyblock_home VARCHAR(50) NOT NULL,
                        skyblock_size VARCHAR(50) NOT NULL,
                        skyblock_monster_spawn VARCHAR(50) NOT NULL,
                        skyblock_animal_spawn VARCHAR(50) NOT NULL,
                        skyblock_weather VARCHAR(50) NOT NULL,
                        skyblock_time VARCHAR(50) NOT NULL,
                        skyblock_water_physics VARCHAR(50) NOT NULL,
                        skyblock_lava_physics VARCHAR(50) NOT NULL
                    );
                """.trimIndent()

                statement!!.executeUpdate(createStr)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    fun DeleteSkyBlock(skyblock_id: String): Int {
        var conn: Connection? = null
        try {
            conn = this.openConnection()

            val sql = """
                DELETE FROM hwaskyblock_skyblock
                WHERE skyblock_id = ?
            """.trimIndent()

            val pstmt = conn?.prepareStatement(sql)
            pstmt?.setString(1, skyblock_id)

            val rowsDeleted = pstmt?.executeUpdate()

            if (rowsDeleted != null && rowsDeleted > 0) {
                pstmt.close()
                return 0
            } else {
                pstmt?.close()
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
