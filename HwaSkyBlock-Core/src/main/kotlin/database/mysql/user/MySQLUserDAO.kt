package org.hwabaeg.hwaskyblock.database.mysql.user

import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.sql.Connection

class MySQLUserDAO {
    private val conn: Connection get() = MySQLManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_user (
                player_uuid VARCHAR(36) PRIMARY KEY,
                player_setting TEXT NOT NULL,
                player_possession_count INT NOT NULL,
                player_pos TEXT NOT NULL,
                player_page INT NOT NULL,
                player_event VARCHAR(255) DEFAULT ''
            );
        """.trimIndent()
        conn.createStatement().use { it.execute(sql) }
    }

    fun insertUser(data: Map<String, Any>) {
        val keys = data.keys.joinToString(", ")
        val placeholders = data.keys.joinToString(", ") { "?" }
        val sql = """
            INSERT INTO hwaskyblock_user ($keys) VALUES ($placeholders)
            ON DUPLICATE KEY UPDATE ${data.keys.joinToString(", ") { "$it = VALUES($it)" }}
        """.trimIndent()

        conn.prepareStatement(sql).use { stmt ->
            data.values.forEachIndexed { i, v ->
                when (v) {
                    is Int -> stmt.setInt(i + 1, v)
                    is String -> stmt.setString(i + 1, v)
                    else -> stmt.setObject(i + 1, v.toString())
                }
            }
            stmt.executeUpdate()
        }
    }

    fun getUser(uuid: String): Map<String, Any>? {
        val sql = "SELECT * FROM hwaskyblock_user WHERE player_uuid = ?"
        val parser = JSONParser()

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, uuid)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val meta = rs.metaData
                val result = mutableMapOf<String, Any>()
                for (i in 1..meta.columnCount) {
                    val col = meta.getColumnName(i)
                    val value = rs.getObject(i)
                    when {
                        col == "player_setting" && value is String -> {
                            result[col] = try {
                                val obj = parser.parse(value) as JSONObject
                                obj.mapKeys { it.key.toString() }
                            } catch (e: Exception) {
                                emptyMap<String, Any>()
                            }
                        }

                        col == "player_pos" && value is String -> {
                            result[col] = try {
                                val obj = parser.parse(value) as JSONObject
                                obj.mapKeys { it.key.toString() }
                            } catch (e: Exception) {
                                emptyMap<String, Any>()
                            }
                        }

                        else -> result[col] = value ?: ""
                    }
                }
                return result
            }
        }
        return null
    }

    fun updateUser(uuid: String, values: Map<String, Any>) {
        if (values.isEmpty()) return

        val setClause = values.keys.joinToString(", ") { "$it = ?" }
        val sql = "UPDATE hwaskyblock_user SET $setClause WHERE player_uuid = ?"

        conn.prepareStatement(sql).use { stmt ->
            values.entries.forEachIndexed { i, entry ->
                when (val v = entry.value) {
                    is Int -> stmt.setInt(i + 1, v)
                    is String -> stmt.setString(i + 1, v)
                    is Boolean -> stmt.setBoolean(i + 1, v)
                    else -> stmt.setObject(i + 1, v)
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
