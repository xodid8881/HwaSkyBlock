package org.hwabaeg.hwaskyblock.database.sqlite.skyblock

import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabaeg.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class hwaskyblock_skyblock_sqlite {
    private val conn: Connection get() = SQLiteManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_skyblock (
                skyblock_id VARCHAR(50) NOT NULL PRIMARY KEY,
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
        conn.createStatement().use { it.execute(sql) }
    }

    fun insertSkyblockDefault(id: String) {
        insertSkyblockDefault(id, id, id)
    }

    fun insertSkyblockDefault(id: String, skyblockName: String, skyblockLeader: String) {
        val data = mutableMapOf<String, String>().apply {
            this["skyblock_id"] = id
            this["skyblock_name"] = skyblockName
            this["skyblock_leader"] = skyblockLeader
            this["skyblock_join"] = "true"
            this["skyblock_break"] = "false"
            this["skyblock_place"] = "false"
            listOf(
                "door", "chest", "barrel", "hopper", "furnace", "blast_furnace", "shulker_box",
                "trapdoor", "button", "anvil", "farm", "beacon", "minecart", "boat", "pvp"
            ).forEach { this["skyblock_$it"] = "false" }
            this["skyblock_welcome_message"] = "Welcome to $id Skyblock!"
            this["skyblock_home"] = "0"
            this["skyblock_size"] = "0"
            this["skyblock_monster_spawn"] = "true"
            this["skyblock_animal_spawn"] = "true"
            this["skyblock_weather"] = "basic"
            this["skyblock_time"] = "basic"
            this["skyblock_water_physics"] = "true"
            this["skyblock_lava_physics"] = "true"
        }

        val keys = data.keys.joinToString(", ")
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = "INSERT INTO hwaskyblock_skyblock ($keys) VALUES ($placeholders)"

        conn.prepareStatement(sql).use { stmt ->
            data.values.forEachIndexed { i, v -> stmt.setString(i + 1, v) }
            stmt.executeUpdate()
        }

        // 캐싱 처리
        DatabaseManager.Select_Skyblock_List[id] = hwaskyblock_skyblock(data)
    }

    fun getSkyblock(id: String): Map<String, String>? {
        val sql = "SELECT * FROM hwaskyblock_skyblock WHERE skyblock_id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, id)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val meta = rs.metaData
                val map = mutableMapOf<String, String>()
                for (i in 1..meta.columnCount) {
                    map[meta.getColumnName(i)] = rs.getString(i)
                }
                return map
            }
        }
        return null
    }

    fun updateSkyblock(id: String, key: String, value: String) {
        val sql = "UPDATE hwaskyblock_skyblock SET $key = ? WHERE skyblock_id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, value)
            stmt.setString(2, id)
            stmt.executeUpdate()
        }

        val old = getSkyblock(id)
        if (old != null) {
            DatabaseManager.Select_Skyblock_List[id] = hwaskyblock_skyblock(old)
        }
    }

    fun selectSkyblock(id: String): Int {
        if (DatabaseManager.Select_Skyblock_List.containsKey(id)) return 0

        val data = getSkyblock(id) ?: run {
            insertSkyblockDefault(id)
            return 0
        }

        DatabaseManager.Select_Skyblock_List[id] = hwaskyblock_skyblock(data)
        return 0
    }

    fun deleteSkyblock(skyblockId: String) {
        val sql = "DELETE FROM hwaskyblock_skyblock WHERE skyblock_id = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.executeUpdate()
        }
    }
}
