package org.hwabeag.hwaskyblock.database.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.sql.*

class InsertSkyblock {

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

    fun insertSkyblock(skyblock_id: String) {

        try {
            val conn = openConnection() ?: return

            val insertSql = """
                INSERT INTO hwaskyblock_skyblock (
                    skyblock_id, skyblock_name, skyblock_leader,
                    skyblock_join, skyblock_break, skyblock_place,
                    skyblock_door, skyblock_chest, skyblock_barrel,
                    skyblock_hopper, skyblock_furnace, skyblock_blast_furnace,
                    skyblock_shulker_box, skyblock_trapdoor, skyblock_button,
                    skyblock_anvil, skyblock_farm, skyblock_beacon,
                    skyblock_minecart, skyblock_boat, skyblock_pvp,
                    skyblock_welcome_message, skyblock_home, skyblock_size,
                    skyblock_monster_spawn, skyblock_animal_spawn,
                    skyblock_weather, skyblock_time,
                    skyblock_water_physics, skyblock_lava_physics
                ) VALUES (${List(30) { "?" }.joinToString(", ")} )
            """.trimIndent()

            val pstmt: PreparedStatement = conn.prepareStatement(insertSql)

            val defaultBoolean = "true"
            val defaultNumber = "0"
            val welcomeMessage = "Welcome to $skyblock_id Skyblock!"

            var idx = 1
            pstmt.setString(idx++, skyblock_id)
            pstmt.setString(idx++, skyblock_id)
            pstmt.setString(idx++, skyblock_id)

            pstmt.setString(idx++, "true")
            pstmt.setString(idx++, "false")
            pstmt.setString(idx++, "false")

            repeat(15) { pstmt.setString(idx++, "false") }

            pstmt.setString(idx++, "false")

            pstmt.setString(idx++, welcomeMessage)

            pstmt.setString(idx++, defaultNumber)
            pstmt.setString(idx++, defaultNumber)

            listOf("true", "true", "basic", "basic", "true", "true").forEach { pstmt.setString(idx++, it) }

            pstmt.executeUpdate()
            pstmt.close()

        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
