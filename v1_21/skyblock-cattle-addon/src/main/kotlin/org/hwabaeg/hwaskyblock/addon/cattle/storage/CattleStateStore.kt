package org.hwabaeg.hwaskyblock.addon.cattle.storage

import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.MySQLManager
import org.hwabaeg.hwaskyblock.database.sqlite.SQLiteManager
import java.sql.Connection

data class CattleState(
    val entityUuid: String,
    val islandId: String,
    val cattleId: String,
    val feedDay: Long,
    val feedCount: Int,
    val breedReadyAt: Long,
    val breedMarkedUntil: Long
)

class CattleStateStore {
    private val cache = HashMap<String, CattleState>()

    fun init() {
        withConnection { conn, dbType ->
            val sql = when (dbType) {
                "mysql" -> """
                    CREATE TABLE IF NOT EXISTS hwaskyblock_cattle_state (
                        entity_uuid VARCHAR(40) PRIMARY KEY,
                        island_id VARCHAR(64) NOT NULL,
                        cattle_id VARCHAR(64) NOT NULL,
                        feed_day BIGINT NOT NULL,
                        feed_count INT NOT NULL,
                        breed_ready_at BIGINT NOT NULL,
                        breed_marked_until BIGINT NOT NULL,
                        updated_at BIGINT NOT NULL
                    )
                """.trimIndent()
                else -> """
                    CREATE TABLE IF NOT EXISTS hwaskyblock_cattle_state (
                        entity_uuid TEXT PRIMARY KEY,
                        island_id TEXT NOT NULL,
                        cattle_id TEXT NOT NULL,
                        feed_day INTEGER NOT NULL,
                        feed_count INTEGER NOT NULL,
                        breed_ready_at INTEGER NOT NULL,
                        breed_marked_until INTEGER NOT NULL,
                        updated_at INTEGER NOT NULL
                    )
                """.trimIndent()
            }
            conn.createStatement().use { it.execute(sql) }
        }
        loadAll()
    }

    fun get(entityUuid: String): CattleState? = cache[entityUuid]

    fun upsert(state: CattleState) {
        cache[state.entityUuid] = state
        withConnection { conn, dbType ->
            val now = System.currentTimeMillis() / 1000L
            val sql = when (dbType) {
                "mysql" -> """
                    INSERT INTO hwaskyblock_cattle_state
                    (entity_uuid, island_id, cattle_id, feed_day, feed_count, breed_ready_at, breed_marked_until, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    island_id = VALUES(island_id),
                    cattle_id = VALUES(cattle_id),
                    feed_day = VALUES(feed_day),
                    feed_count = VALUES(feed_count),
                    breed_ready_at = VALUES(breed_ready_at),
                    breed_marked_until = VALUES(breed_marked_until),
                    updated_at = VALUES(updated_at)
                """.trimIndent()
                else -> """
                    INSERT INTO hwaskyblock_cattle_state
                    (entity_uuid, island_id, cattle_id, feed_day, feed_count, breed_ready_at, breed_marked_until, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT(entity_uuid) DO UPDATE SET
                    island_id = excluded.island_id,
                    cattle_id = excluded.cattle_id,
                    feed_day = excluded.feed_day,
                    feed_count = excluded.feed_count,
                    breed_ready_at = excluded.breed_ready_at,
                    breed_marked_until = excluded.breed_marked_until,
                    updated_at = excluded.updated_at
                """.trimIndent()
            }

            conn.prepareStatement(sql).use { ps ->
                ps.setString(1, state.entityUuid)
                ps.setString(2, state.islandId)
                ps.setString(3, state.cattleId)
                ps.setLong(4, state.feedDay)
                ps.setInt(5, state.feedCount)
                ps.setLong(6, state.breedReadyAt)
                ps.setLong(7, state.breedMarkedUntil)
                ps.setLong(8, now)
                ps.executeUpdate()
            }
        }
    }

    fun delete(entityUuid: String) {
        cache.remove(entityUuid)
        withConnection { conn, _ ->
            conn.prepareStatement("DELETE FROM hwaskyblock_cattle_state WHERE entity_uuid = ?").use { ps ->
                ps.setString(1, entityUuid)
                ps.executeUpdate()
            }
        }
    }

    fun states(): Collection<CattleState> = cache.values

    private fun loadAll() {
        cache.clear()
        withConnection { conn, _ ->
            conn.prepareStatement(
                "SELECT entity_uuid, island_id, cattle_id, feed_day, feed_count, breed_ready_at, breed_marked_until FROM hwaskyblock_cattle_state"
            ).use { ps ->
                val rs = ps.executeQuery()
                while (rs.next()) {
                    val state = CattleState(
                        entityUuid = rs.getString("entity_uuid"),
                        islandId = rs.getString("island_id"),
                        cattleId = rs.getString("cattle_id"),
                        feedDay = rs.getLong("feed_day"),
                        feedCount = rs.getInt("feed_count"),
                        breedReadyAt = rs.getLong("breed_ready_at"),
                        breedMarkedUntil = rs.getLong("breed_marked_until")
                    )
                    cache[state.entityUuid] = state
                }
            }
        }
    }

    private inline fun withConnection(block: (Connection, String) -> Unit) {
        val dbType = ConfigManager.getConfig("setting")?.getString("database.type")?.lowercase() ?: "sqlite"
        val conn = when (dbType) {
            "mysql" -> MySQLManager.getConnection()
            else -> SQLiteManager.getConnection()
        }
        block(conn, dbType)
    }
}
