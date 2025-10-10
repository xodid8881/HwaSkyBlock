package org.hwabaeg.hwaskyblock.database.mysql.user

import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.hwabaeg.hwaskyblock.database.mysql.utils.hwaskyblock_user
import java.sql.Connection

class hwaskyblock_user_mysql {
    private val conn: Connection get() = MySQLManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_user (
                player_uuid VARCHAR(50) PRIMARY KEY,
                player_setting VARCHAR(1000) NOT NULL,
                player_possession_count INT NOT NULL,
                player_pos INT NOT NULL,
                player_page INT NOT NULL
            );
        """.trimIndent()

        conn.createStatement().use { it.execute(sql) }
    }

    fun insertUser(user: hwaskyblock_user) {
        val sql = """
            INSERT INTO hwaskyblock_user
            (player_uuid, player_setting, player_possession_count, player_pos, player_page)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            player_setting=VALUES(player_setting),
            player_possession_count=VALUES(player_possession_count),
            player_pos=VALUES(player_pos),
            player_page=VALUES(player_page)
        """.trimIndent()

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, user.getPlayerUuid())
            stmt.setString(2, user.getPlayerSetting())
            stmt.setInt(3, user.getPlayerPossessionCount())
            stmt.setInt(4, user.getPlayerPos())
            stmt.setInt(5, user.getPlayerPage())
            stmt.executeUpdate()
        }
    }

    fun getUser(uuid: String): hwaskyblock_user? {
        val sql = "SELECT * FROM hwaskyblock_user WHERE player_uuid = ?"

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, uuid)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                val data = mutableMapOf<String, String>()
                data["player_uuid"] = rs.getString("player_uuid")
                data["player_setting"] = rs.getString("player_setting")
                data["player_possession_count"] = rs.getInt("player_possession_count").toString()
                data["player_pos"] = rs.getInt("player_pos").toString()
                data["player_page"] = rs.getInt("player_page").toString()
                return hwaskyblock_user(data)
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
