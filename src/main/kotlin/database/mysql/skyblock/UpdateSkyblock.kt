package org.hwabeag.hwaskyblock.database.mysql.skyblock

import org.bukkit.configuration.file.FileConfiguration
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Statement

class UpdateSkyblock {
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

    private fun updateSkyblockAttribute(skyblock_id: String, columnName: String, value: String) {
        try {
            this.openConnection()?.use { conn ->
                val sql = "UPDATE hwaskyblock_skyblock SET $columnName=? WHERE skyblock_id=?"
                val pstmt: PreparedStatement = conn.prepareStatement(sql)
                pstmt.setString(1, value)
                pstmt.setString(2, skyblock_id)
                pstmt.executeUpdate()
                pstmt.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSkyblockName(skyblock_id: String, skyblockName: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_name", skyblockName)
    }

    fun updateSkyblockLeader(skyblock_id: String, skyblockLeader: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_leader", skyblockLeader)
    }

    fun updateSkyblockJoin(skyblock_id: String, skyblockJoin: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_join", skyblockJoin)
    }

    fun updateSkyblockBreak(skyblock_id: String, skyblockBreak: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_break", skyblockBreak)
    }

    fun updateSkyblockPlace(skyblock_id: String, skyblockPlace: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_place", skyblockPlace)
    }

    fun updateSkyblockDoor(skyblock_id: String, skyblockDoor: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_door", skyblockDoor)
    }

    fun updateSkyblockChest(skyblock_id: String, skyblockChest: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_chest", skyblockChest)
    }

    fun updateSkyblockBarrel(skyblock_id: String, skyblockBarrel: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_barrel", skyblockBarrel)
    }

    fun updateSkyblockHopper(skyblock_id: String, skyblockHopper: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_hopper", skyblockHopper)
    }

    fun updateSkyblockFurnace(skyblock_id: String, skyblockFurnace: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_furnace", skyblockFurnace)
    }

    fun updateSkyblockBlastFurnace(skyblock_id: String, skyblockBlastFurnace: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_blast_furnace", skyblockBlastFurnace)
    }

    fun updateSkyblockShulkerBox(skyblock_id: String, skyblockShulkerBox: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_shulker_box", skyblockShulkerBox)
    }

    fun updateSkyblockTrapdoor(skyblock_id: String, skyblockTrapdoor: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_trapdoor", skyblockTrapdoor)
    }

    fun updateSkyblockButton(skyblock_id: String, skyblockButton: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_button", skyblockButton)
    }

    fun updateSkyblockAnvil(skyblock_id: String, skyblockAnvil: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_anvil", skyblockAnvil)
    }

    fun updateSkyblockFarm(skyblock_id: String, skyblockFarm: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_farm", skyblockFarm)
    }

    fun updateSkyblockBeacon(skyblock_id: String, skyblockBeacon: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_beacon", skyblockBeacon)
    }

    fun updateSkyblockMinecart(skyblock_id: String, skyblockMinecart: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_minecart", skyblockMinecart)
    }

    fun updateSkyblockBoat(skyblock_id: String, skyblockBoat: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_boat", skyblockBoat)
    }

    fun updateSkyblockPvp(skyblock_id: String, skyblockPvp: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_pvp", skyblockPvp)
    }

    fun updateSkyblockWelcomeMessage(skyblock_id: String, skyblockWelcomeMessage: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_welcome_message", skyblockWelcomeMessage)
    }

    fun updateSkyblockHome(skyblock_id: String, skyblockHome: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_home", skyblockHome)
    }

    fun updateSkyblockSize(skyblock_id: String, skyblockSize: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_size", skyblockSize)
    }

    fun updateSkyblockMonsterSpawn(skyblock_id: String, skyblockMonsterSpawn: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_monster_spawn", skyblockMonsterSpawn)
    }

    fun updateSkyblockAnimalSpawn(skyblock_id: String, skyblockAnimalSpawn: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_animal_spawn", skyblockAnimalSpawn)
    }

    fun updateSkyblockWeather(skyblock_id: String, skyblockWeather: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_weather", skyblockWeather)
    }

    fun updateSkyblockTime(skyblock_id: String, skyblockTime: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_time", skyblockTime)
    }

    fun updateSkyblockWaterPhysics(skyblock_id: String, skyblockWaterPhysics: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_water_physics", skyblockWaterPhysics)
    }

    fun updateSkyblockLavaPhysics(skyblock_id: String, skyblockLavaPhysics: String) {
        updateSkyblockAttribute(skyblock_id, "skyblock_lava_physics", skyblockLavaPhysics)
    }
}
