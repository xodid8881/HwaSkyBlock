package org.hwabeag.hwaskyblock.database.sqlite

import org.bukkit.plugin.java.JavaPlugin
import org.hwabeag.hwaskyblock.HwaSkyBlock
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SQLiteManager(private val plugin: JavaPlugin) {
    private lateinit var connection: Connection

    fun init() {
        val file = File(plugin.dataFolder, "data.db")
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        connection = DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")
        plugin.logger.info("✅ SQLite 연결 완료")
        instance = this
    }

    fun getConnection(): Connection {
        return connection
    }

    fun close() {
        connection.close()
    }

    companion object {
        private lateinit var instance: SQLiteManager

        fun get(): SQLiteManager {
            if (!::instance.isInitialized) {
                throw IllegalStateException("SQLiteManager is not initialized!")
            }
            return instance
        }

        fun getConnection(): Connection {
            return get().getConnection()
        }

        fun init(block: HwaSkyBlock) {
            val manager = SQLiteManager(block)
            manager.init()
        }
    }
}

/*
val skyblockData = mapOf(
    "skyblock_id" to "island001",
    "skyblock_name" to "네버팜",
    "skyblock_leader" to "xodid8881",
    "skyblock_join" to "true",
    "skyblock_break" to "true",
    "skyblock_place" to "true",
    "skyblock_door" to "true",
    "skyblock_chest" to "true",
    "skyblock_barrel" to "true",
    "skyblock_hopper" to "true",
    "skyblock_furnace" to "true",
    "skyblock_blast_furnace" to "true",
    "skyblock_shulker_box" to "true",
    "skyblock_trapdoor" to "true",
    "skyblock_button" to "true",
    "skyblock_anvil" to "true",
    "skyblock_farm" to "true",
    "skyblock_beacon" to "true",
    "skyblock_minecart" to "true",
    "skyblock_boat" to "true",
    "skyblock_pvp" to "false",
    "skyblock_welcome_message" to "어서오세요, 네버팜입니다!",
    "skyblock_home" to "world,100,65,100",
    "skyblock_size" to "100",
    "skyblock_monster_spawn" to "true",
    "skyblock_animal_spawn" to "true",
    "skyblock_weather" to "sunny",
    "skyblock_time" to "day",
    "skyblock_water_physics" to "true",
    "skyblock_lava_physics" to "true"
)

SkyblockDAO.insertSkyblock(skyblockData)

val skyblock = SkyblockDAO.getSkyblock("island001")
if (skyblock != null) {
    val name = skyblock["skyblock_name"]
    val leader = skyblock["skyblock_leader"]
    val welcome = skyblock["skyblock_welcome_message"]
    println("섬 이름: $name, 리더: $leader, 환영 메시지: $welcome")
}

SkyblockDAO.updateSkyblock(
    id = "island001",
    values = mapOf(
        "skyblock_name" to "수정된네버팜",
        "skyblock_pvp" to "true",
        "skyblock_welcome_message" to "이제 PVP 가능! 조심하세요!"
    )
)

SkyblockDAO.deleteSkyblock("island001")
 */