package org.hwabeag.hwaskyblock.database.sqlite.user

import org.hwabeag.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class UserDAO {
    private val conn: Connection get() = SQLiteManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_user (
                player_uuid TEXT PRIMARY KEY,
                player_setting TEXT NOT NULL,
                player_possession_count INTEGER NOT NULL,
                player_pos TEXT NOT NULL,
                player_page INTEGER NOT NULL
            );
        """.trimIndent()

        conn.createStatement().use { it.execute(sql) }
    }

    fun insertUser(data: Map<String, Any>) {
        val keys = data.keys.joinToString(", ")
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = "INSERT OR REPLACE INTO hwaskyblock_user ($keys) VALUES ($placeholders)"

        conn.prepareStatement(sql).use { stmt ->
            data.values.forEachIndexed { index, value ->
                when (value) {
                    is Int -> stmt.setInt(index + 1, value)
                    is String -> stmt.setString(index + 1, value)
                    else -> stmt.setObject(index + 1, value.toString())
                }
            }
            stmt.executeUpdate()
        }
    }

    fun getUser(uuid: String): Map<String, Any>? {
        val sql = "SELECT * FROM hwaskyblock_user WHERE player_uuid = ?"

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, uuid)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val meta = rs.metaData
                val map = mutableMapOf<String, Any>()
                for (i in 1..meta.columnCount) {
                    val colName = meta.getColumnName(i)
                    val value = rs.getObject(i)
                    map[colName] = value
                }
                return map
            }
        }
        return null
    }

    fun updateUser(uuid: String, values: Map<String, Any>) {
        val setClause = values.keys.joinToString(", ") { "$it = ?" }
        val sql = "UPDATE hwaskyblock_user SET $setClause WHERE player_uuid = ?"

        conn.prepareStatement(sql).use { stmt ->
            values.values.forEachIndexed { i, v ->
                when (v) {
                    is Int -> stmt.setInt(i + 1, v)
                    is String -> stmt.setString(i + 1, v)
                    else -> stmt.setObject(i + 1, v.toString())
                }
            }
            stmt.setString(values.size + 1, uuid)
            stmt.executeUpdate()
        }
    }

    fun deleteUser(uuid: String) {
        val sql = "DELETE FROM hwaskyblock_user WHERE player_uuid = ?"
        conn.prepareStatement(sql).use {
            it.setString(1, uuid)
            it.executeUpdate()
        }
    }
}
