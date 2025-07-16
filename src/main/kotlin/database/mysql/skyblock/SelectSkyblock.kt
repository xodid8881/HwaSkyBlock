package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class SelectSkyblock {

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

    fun SelectSkyBlock(skyblockId: String): Int {
        val skyblock: hwaskyblock_skyblock = hwaskyblock_skyblock()
        var conn: Connection? = null
        try {
            conn = this.openConnection()
            val sql = """
                SELECT skyblock_id, skyblock_name, skyblock_leader,
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
                FROM hwaskyblock_skyblock
                WHERE skyblock_id = ?
            """.trimIndent()

            val pstmt = conn?.prepareStatement(sql)
            pstmt?.setString(1, skyblockId)
            val rs = pstmt?.executeQuery()

            if (rs != null) {
                if (rs.next()) {
                    skyblock.setSkyBlockId(rs.getString("skyblock_id") ?: "")
                    skyblock.setSkyBlockName(rs.getString("skyblock_name") ?: "")
                    skyblock.setSkyBlockLeader(rs.getString("skyblock_leader") ?: "")
                    skyblock.setSkyBlockJoin(rs.getString("skyblock_join")?.toBoolean() == true)
                    skyblock.setSkyBlockBreak(rs.getString("skyblock_break")?.toBoolean() == true)
                    skyblock.setSkyBlockPlace(rs.getString("skyblock_place")?.toBoolean() == true)
                    skyblock.setSkyBlockDoor(rs.getString("skyblock_door")?.toBoolean() == true)
                    skyblock.setSkyBlockChest(rs.getString("skyblock_chest")?.toBoolean() == true)
                    skyblock.setSkyBlockBarrel(rs.getString("skyblock_barrel")?.toBoolean() == true)
                    skyblock.setSkyBlockHopper(rs.getString("skyblock_hopper")?.toBoolean() == true)
                    skyblock.setSkyBlockFurnace(rs.getString("skyblock_furnace")?.toBoolean() == true)
                    skyblock.setSkyBlockBlastFurnace(rs.getString("skyblock_blast_furnace")?.toBoolean() == true)
                    skyblock.setSkyBlockShulkerBox(rs.getString("skyblock_shulker_box")?.toBoolean() == true)
                    skyblock.setSkyBlockTrapdoor(rs.getString("skyblock_trapdoor")?.toBoolean() == true)
                    skyblock.setSkyBlockButton(rs.getString("skyblock_button")?.toBoolean() == true)
                    skyblock.setSkyBlockAnvil(rs.getString("skyblock_anvil")?.toBoolean() == true)
                    skyblock.setSkyBlockFarm(rs.getString("skyblock_farm")?.toBoolean() == true)
                    skyblock.setSkyBlockBeacon(rs.getString("skyblock_beacon")?.toBoolean() == true)
                    skyblock.setSkyBlockMinecart(rs.getString("skyblock_minecart")?.toBoolean() == true)
                    skyblock.setSkyBlockBoat(rs.getString("skyblock_boat")?.toBoolean() == true)
                    skyblock.setSkyBlockPvp(rs.getString("skyblock_pvp")?.toBoolean() == true)
                    skyblock.setSkyBlockWelcomeMessage(rs.getString("skyblock_welcome_message") ?: "")
                    skyblock.setSkyBlockHome(rs.getString("skyblock_home")?.toInt() ?: 0)
                    skyblock.setSkyBlockSize(rs.getString("skyblock_size")?.toInt() ?: 0)
                    skyblock.setSkyBlockMonsterSpawn(rs.getString("skyblock_monster_spawn")?.toBoolean() == true)
                    skyblock.setSkyBlockAnimalSpawn(rs.getString("skyblock_animal_spawn")?.toBoolean() == true)
                    skyblock.setSkyBlockWeather(rs.getString("skyblock_weather")?.toBoolean() == true)
                    skyblock.setSkyBlockTime(rs.getString("skyblock_time")?.toBoolean() == true)
                    skyblock.setSkyBlockWaterPhysics(rs.getString("skyblock_water_physics")?.toBoolean() == true)
                    skyblock.setSkyBlockLavaPhysics(rs.getString("skyblock_lava_physics")?.toBoolean() == true)
                    DatabaseManager.Select_Skyblock_List.put(rs.getString("skyblock_id"), skyblock)
                    rs.close()
                    pstmt.close()
                    return 0
                } else {
                    rs.close()
                    pstmt.close()
                    return 1
                }
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
