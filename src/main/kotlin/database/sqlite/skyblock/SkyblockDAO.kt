package org.hwabeag.hwaskyblock.database.sqlite.skyblock

import org.hwabeag.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class SkyblockDAO {
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

    fun insertSkyblock(data: Map<String, String>) {
        val keys = data.keys.joinToString(", ") { "\"$it\"" } // ← 컬럼명 감싸기
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = "INSERT INTO hwaskyblock_skyblock ($keys) VALUES ($placeholders)"

        conn.prepareStatement(sql).use { stmt ->
            data.values.forEachIndexed { index, value ->
                stmt.setString(index + 1, value)
            }
            stmt.executeUpdate()
        }
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

    fun updateSkyblock(id: String, values: Map<String, String>) {
        val setClause = values.keys.joinToString(", ") { "\"$it\" = ?" } // ← 컬럼명 감싸기
        val sql = "UPDATE hwaskyblock_skyblock SET $setClause WHERE skyblock_id = ?"

        conn.prepareStatement(sql).use { stmt ->
            values.values.forEachIndexed { i, v ->
                stmt.setString(i + 1, v)
            }
            stmt.setString(values.size + 1, id)
            stmt.executeUpdate()
        }
    }

    fun deleteSkyblock(id: String) {
        val sql = "DELETE FROM hwaskyblock_skyblock WHERE skyblock_id = ?"
        conn.prepareStatement(sql).use {
            it.setString(1, id)
            it.executeUpdate()
        }
    }
}