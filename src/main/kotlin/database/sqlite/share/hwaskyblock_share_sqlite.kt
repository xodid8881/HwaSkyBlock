package org.hwabeag.hwaskyblock.database.sqlite.skyblock

import org.hwabeag.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

class hwaskyblock_share_sqlite {
    private val conn: Connection get() = SQLiteManager.getConnection()

    init {
        val sql = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_share (
                skyblock_id TEXT NOT NULL,
                player_name TEXT NOT NULL,
                can_break INTEGER DEFAULT 1,
                can_place INTEGER DEFAULT 1,
                use_door INTEGER DEFAULT 1,
                use_chest INTEGER DEFAULT 1,
                use_barrel INTEGER DEFAULT 1,
                use_hopper INTEGER DEFAULT 1,
                use_furnace INTEGER DEFAULT 1,
                use_blast_furnace INTEGER DEFAULT 1,
                use_shulker_box INTEGER DEFAULT 1,
                use_trapdoor INTEGER DEFAULT 1,
                use_button INTEGER DEFAULT 1,
                use_anvil INTEGER DEFAULT 1,
                use_farm INTEGER DEFAULT 1,
                use_beacon INTEGER DEFAULT 1,
                use_minecart INTEGER DEFAULT 1,
                use_boat INTEGER DEFAULT 1,
                PRIMARY KEY (skyblock_id, player_name)
            );
        """.trimIndent()
        conn.createStatement().use { it.execute(sql) }
    }

    fun getShareList(skyblockId: String): List<String> {
        val list = mutableListOf<String>()
        val sql = "SELECT player_name FROM hwaskyblock_share WHERE skyblock_id = ?"

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                list.add(rs.getString("player_name"))
            }
        }

        return list
    }

    fun insertShare(skyblockId: String, playerName: String) {
        val sql = """
            INSERT OR IGNORE INTO hwaskyblock_share (
                skyblock_id, player_name,
                can_break, can_place,
                use_door, use_chest, use_barrel, use_hopper,
                use_furnace, use_blast_furnace, use_shulker_box,
                use_trapdoor, use_button, use_anvil,
                use_farm, use_beacon, use_minecart, use_boat
            )
            VALUES (?, ?, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        """.trimIndent()

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            stmt.executeUpdate()
        }
    }

    fun deleteShare(skyblockId: String, playerName: String) {
        val sql = "DELETE FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            stmt.executeUpdate()
        }
    }

    fun updatePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        val allowedPermissions = setOf(
            "can_break", "can_place",
            "use_door", "use_chest", "use_barrel", "use_hopper",
            "use_furnace", "use_blast_furnace", "use_shulker_box",
            "use_trapdoor", "use_button", "use_anvil",
            "use_farm", "use_beacon", "use_minecart", "use_boat"
        )

        if (!allowedPermissions.contains(permission)) {
            println("⛔ 잘못된 권한 필드 요청: $permission")
            return
        }

        val sql = "UPDATE hwaskyblock_share SET $permission = ? WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setInt(1, if (value) 1 else 0)
            stmt.setString(2, skyblockId)
            stmt.setString(3, playerName)
            stmt.executeUpdate()
        }
    }

    fun getPermission(skyblockId: String, playerName: String, permission: String): Boolean? {
        val allowedPermissions = setOf(
            "can_break", "can_place",
            "use_door", "use_chest", "use_barrel", "use_hopper",
            "use_furnace", "use_blast_furnace", "use_shulker_box",
            "use_trapdoor", "use_button", "use_anvil",
            "use_farm", "use_beacon", "use_minecart", "use_boat"
        )

        if (!allowedPermissions.contains(permission)) {
            println("⛔ 잘못된 권한 필드 요청: $permission")
            return null
        }

        val sql = "SELECT $permission FROM hwaskyblock_share WHERE skyblock_id = ? AND player_name = ?"
        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, skyblockId)
            stmt.setString(2, playerName)
            val rs = stmt.executeQuery()
            return if (rs.next()) rs.getInt(permission) == 1 else null
        }
    }
}
