package addon.dailymission.storage

import addon.dailymission.config.AddonConfig
import addon.dailymission.island.AssignedMission
import addon.dailymission.mission.MissionDifficulty
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Connection
import java.sql.SQLException

class MissionDataStore private constructor(
    private val db: AddonDatabase,
    private val type: DatabaseType
) {
    private var conn: Connection = db.getConnection()

    fun initTables() {
        ensureOpen()
        val createDaily = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_daily_mission (
                island_id VARCHAR(64) NOT NULL,
                mission_date VARCHAR(20) NOT NULL,
                difficulty VARCHAR(20) NOT NULL,
                mission_id VARCHAR(120) NOT NULL,
                progress INT NOT NULL,
                completed INT NOT NULL,
                PRIMARY KEY (island_id, mission_date, difficulty)
            );
        """.trimIndent()

        val createPoints = """
            CREATE TABLE IF NOT EXISTS hwaskyblock_daily_mission_point (
                island_id VARCHAR(64) NOT NULL PRIMARY KEY,
                mission_point INT NOT NULL
            );
        """.trimIndent()

        withConnection { c ->
            c.createStatement().use { stmt ->
                stmt.execute(createDaily)
                stmt.execute(createPoints)
            }
        }
    }

    fun getAssignments(islandId: String, date: String): List<AssignedMission> {
        val sql = """
            SELECT difficulty, mission_id, progress, completed
            FROM hwaskyblock_daily_mission
            WHERE island_id = ? AND mission_date = ?
        """.trimIndent()

        val result = mutableListOf<AssignedMission>()
        withConnection { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, islandId)
                stmt.setString(2, date)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val difficulty = MissionDifficulty.Companion.fromKey(rs.getString("difficulty")) ?: continue
                    result.add(
                        AssignedMission(
                            difficulty = difficulty,
                            missionId = rs.getString("mission_id"),
                            progress = rs.getInt("progress"),
                            completed = rs.getInt("completed") == 1
                        )
                    )
                }
            }
        }
        return result
    }

    fun insertAssignment(
        islandId: String,
        date: String,
        difficulty: MissionDifficulty,
        missionId: String
    ) {
        val sql = """
            INSERT INTO hwaskyblock_daily_mission
            (island_id, mission_date, difficulty, mission_id, progress, completed)
            VALUES (?, ?, ?, ?, 0, 0)
        """.trimIndent()

        withConnection { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, islandId)
                stmt.setString(2, date)
                stmt.setString(3, difficulty.key)
                stmt.setString(4, missionId)
                stmt.executeUpdate()
            }
        }
    }

    fun addProgress(
        islandId: String,
        date: String,
        difficulty: MissionDifficulty,
        amount: Int
    ): Int {
        val select = """
            SELECT progress FROM hwaskyblock_daily_mission
            WHERE island_id = ? AND mission_date = ? AND difficulty = ?
        """.trimIndent()

        val current = withConnection { c ->
            c.prepareStatement(select).use { stmt ->
                stmt.setString(1, islandId)
                stmt.setString(2, date)
                stmt.setString(3, difficulty.key)
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getInt("progress") else 0
            }
        }

        val total = current + amount
        val update = """
            UPDATE hwaskyblock_daily_mission
            SET progress = ?
            WHERE island_id = ? AND mission_date = ? AND difficulty = ?
        """.trimIndent()

        withConnection { c ->
            c.prepareStatement(update).use { stmt ->
                stmt.setInt(1, total)
                stmt.setString(2, islandId)
                stmt.setString(3, date)
                stmt.setString(4, difficulty.key)
                stmt.executeUpdate()
            }
        }
        return total
    }

    fun markCompleted(islandId: String, date: String, difficulty: MissionDifficulty) {
        val sql = """
            UPDATE hwaskyblock_daily_mission
            SET completed = 1
            WHERE island_id = ? AND mission_date = ? AND difficulty = ?
        """.trimIndent()

        withConnection { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, islandId)
                stmt.setString(2, date)
                stmt.setString(3, difficulty.key)
                stmt.executeUpdate()
            }
        }
    }

    fun addMissionPoint(islandId: String, amount: Int) {
        if (amount <= 0) return
        val current = getMissionPoint(islandId)
        val total = current + amount

        val sql = """
            INSERT INTO hwaskyblock_daily_mission_point (island_id, mission_point)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE mission_point = ?
        """.trimIndent()

        val sqliteSql = """
            INSERT INTO hwaskyblock_daily_mission_point (island_id, mission_point)
            VALUES (?, ?)
            ON CONFLICT(island_id) DO UPDATE SET mission_point = excluded.mission_point
        """.trimIndent()

        val finalSql = if (type == DatabaseType.MYSQL) sql else sqliteSql

        withConnection { c ->
            c.prepareStatement(finalSql).use { stmt ->
                stmt.setString(1, islandId)
                stmt.setInt(2, total)
                if (type == DatabaseType.MYSQL) stmt.setInt(3, total)
                stmt.executeUpdate()
            }
        }
    }

    fun getMissionPoint(islandId: String): Int {
        val sql = """
            SELECT mission_point FROM hwaskyblock_daily_mission_point
            WHERE island_id = ?
        """.trimIndent()

        return withConnection { c ->
            c.prepareStatement(sql).use { stmt ->
                stmt.setString(1, islandId)
                val rs = stmt.executeQuery()
                if (rs.next()) rs.getInt("mission_point") else 0
            }
        }
    }

    fun close() {
        db.close()
    }

    private fun ensureOpen() {
        val needsReconnect = try {
            conn.isClosed || !conn.isValid(1)
        } catch (e: Exception) {
            true
        }
        if (needsReconnect) {
            conn = db.init()
        }
    }

    private fun <T> withConnection(block: (Connection) -> T): T {
        ensureOpen()
        return try {
            block(conn)
        } catch (e: SQLException) {
            conn = db.init()
            block(conn)
        }
    }

    companion object {
        fun create(config: AddonConfig, plugin: JavaPlugin): MissionDataStore {
            val type = DatabaseType.from(config.databaseType)
            val db = AddonDatabase(type, plugin, config)
            db.init()
            return MissionDataStore(db, type).apply { initTables() }
        }
    }
}
