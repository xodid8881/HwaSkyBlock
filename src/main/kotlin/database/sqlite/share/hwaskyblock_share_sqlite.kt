package org.hwabeag.hwaskyblock.database.sqlite.skyblock

import org.hwabeag.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class hwaskyblock_share_sqlite {
    private val conn: Connection get() = SQLiteManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_share (
                skyblock_id VARCHAR(50) NOT NULL,
                player_name VARCHAR(50) NOT NULL,
                use_break VARCHAR(50) NOT NULL,
                use_place VARCHAR(50) NOT NULL,
                use_door VARCHAR(50) NOT NULL,
                use_chest VARCHAR(50) NOT NULL,
                use_barrel VARCHAR(50) NOT NULL,
                use_hopper VARCHAR(50) NOT NULL,
                use_furnace VARCHAR(50) NOT NULL,
                use_blast_furnace VARCHAR(50) NOT NULL,
                use_shulker_box VARCHAR(50) NOT NULL,
                use_trapdoor VARCHAR(50) NOT NULL,
                use_button VARCHAR(50) NOT NULL,
                use_anvil VARCHAR(50) NOT NULL,
                use_farm VARCHAR(50) NOT NULL,
                use_beacon VARCHAR(50) NOT NULL,
                use_minecart VARCHAR(50) NOT NULL,
                use_boat VARCHAR(50) NOT NULL,
                PRIMARY KEY (skyblock_id, player_name)
            );
        """.trimIndent()
        conn.createStatement().use { it.execute(sql) }
    }

    fun insertShare(skyblockId: String, playerName: String) {
        val sql = """
            INSERT OR REPLACE INTO hwaskyblock_share (
                skyblock_id, player_name,
                use_break, use_place, use_door, use_chest, use_barrel,
                use_hopper, use_furnace, use_blast_furnace, use_shulker_box,
                use_trapdoor, use_button, use_anvil, use_farm, use_beacon,
                use_minecart, use_boat
            ) VALUES (
                ?, ?, 'true', 'true', 'true', 'true', 'true',
                'true', 'true', 'true', 'true',
                'true', 'true', 'true', 'true', 'true',
                'true', 'true'
            );
        """.trimIndent()

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            stmt.executeUpdate()
        }
    }

    fun deleteShare(skyblockId: String, playerName: String) {
        val sql = "DELETE FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use {
            it.setString(1, skyblockId)
            it.setString(2, playerName)
            it.executeUpdate()
        }
    }

    fun getShareList(skyblockId: String): List<String> {
        val sql = "SELECT player_name FROM hwaskyblock_share WHERE skyblock_id = ?"
        val list = mutableListOf<String>()

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                list.add(rs.getString("player_name"))
            }
        }
        return list
    }

    fun getPermission(skyblockId: String, playerName: String, permission: String): Boolean? {
        val sql = "SELECT $permission FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return rs.getString(permission).lowercase() == "true"
            }
        }
        return null
    }

    fun updatePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        val sql = "UPDATE hwaskyblock_share SET $permission = ? WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, value.toString())
            stmt.setString(2, skyblockId)
            stmt.setString(3, playerName)
            stmt.executeUpdate()
        }
    }
}
