package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.*

class InsertSkyblockShare {
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
                CREATE TABLE IF NOT EXISTS hwaskyblock_share (
                    skyblock_id VARCHAR(50) NOT NULL,
                    player_name VARCHAR(50) NOT NULL,
                    can_break BOOLEAN DEFAULT TRUE,
                    can_place BOOLEAN DEFAULT TRUE,
                    use_door BOOLEAN DEFAULT TRUE,
                    use_chest BOOLEAN DEFAULT TRUE,
                    use_barrel BOOLEAN DEFAULT TRUE,
                    use_hopper BOOLEAN DEFAULT TRUE,
                    use_furnace BOOLEAN DEFAULT TRUE,
                    use_blast_furnace BOOLEAN DEFAULT TRUE,
                    use_shulker_box BOOLEAN DEFAULT TRUE,
                    use_trapdoor BOOLEAN DEFAULT TRUE,
                    use_button BOOLEAN DEFAULT TRUE,
                    use_anvil BOOLEAN DEFAULT TRUE,
                    use_farm BOOLEAN DEFAULT TRUE,
                    use_beacon BOOLEAN DEFAULT TRUE,
                    use_minecart BOOLEAN DEFAULT TRUE,
                    use_boat BOOLEAN DEFAULT TRUE
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

    fun insertShare(skyblockId: String, playerName: String) {
        try {
            val conn = openConnection() ?: return

            val sql = """
            INSERT INTO hwaskyblock_share (
                skyblock_id, player_name,
                can_break, can_place,
                use_door, use_chest, use_barrel, use_hopper,
                use_furnace, use_blast_furnace, use_shulker_box,
                use_trapdoor, use_button, use_anvil,
                use_farm, use_beacon, use_minecart, use_boat
            )
            VALUES (?, ?, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE)
        """.trimIndent()

            val pstmt: PreparedStatement = conn.prepareStatement(sql)
            pstmt.setString(1, skyblockId)
            pstmt.setString(2, playerName)
            pstmt.executeUpdate()
            pstmt.close()

        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
