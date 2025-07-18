package org.hwabeag.hwaskyblock.database.sqlite.share

import org.hwabeag.hwaskyblock.database.DatabaseManager
import org.hwabeag.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class ShareDAO {
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

    fun insertShare(data: Map<String, String>) {
        val keys = data.keys.joinToString(", ") { "\"$it\"" }
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = "INSERT INTO hwaskyblock_share ($keys) VALUES ($placeholders)"

        conn.prepareStatement(sql).use { stmt ->
            data.values.forEachIndexed { index, value ->
                stmt.setString(index + 1, value)
            }
            stmt.executeUpdate()
        }
    }

    fun getShare(skyblockId: String, playerName: String): Map<String, String>? {
        val sql = "SELECT * FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val meta = rs.metaData
                val data = mutableMapOf<String, String>()
                for (i in 1..meta.columnCount) {
                    data[meta.getColumnName(i)] = rs.getString(i)
                }
                return data
            }
        }
        return null
    }

    fun updateShare(skyblockId: String, playerName: String, updates: Map<String, Any>) {
        val setClause = updates.keys.joinToString(", ") { "\"$it\" = ?" }
        val sql = "UPDATE hwaskyblock_share SET $setClause WHERE skyblock_id = ? AND player_name = ?"

        conn.prepareStatement(sql).use { stmt ->
            updates.values.forEachIndexed { i, v ->
                stmt.setString(i + 1, v.toString())
            }
            stmt.setString(updates.size + 1, skyblockId)
            stmt.setString(updates.size + 2, playerName)
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

        val cache = list.associateWith { true }
        DatabaseManager.Select_Share_List[skyblockId] = cache
        return list
    }

    fun getSharePermission(skyblockId: String, playerName: String, permission: String): Boolean? {
        val sql = "SELECT \"$permission\" FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
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

    fun setSharePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        val sql = "UPDATE hwaskyblock_share SET \"$permission\" = ? WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, value.toString())
            stmt.setString(2, skyblockId)
            stmt.setString(3, playerName)
            stmt.executeUpdate()
        }
    }
}
