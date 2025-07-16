package org.hwabeag.hwaskyblock.database.mysql.utils

class hwaskyblock_user() {

    private var playerUuid: String? = null
    private var playerSetting: String? = null
    private var playerPossessionCount: Int = 0
    private var playerPos: Int = 0
    private var playerPage: Int = 0

    // Map<String, String> 기반 생성자 추가
    constructor(data: Map<String, String>) : this() {
        this.playerUuid = data["player_uuid"]
        this.playerSetting = data["player_setting"]
        this.playerPossessionCount = data["player_possession_count"]?.toIntOrNull() ?: 0
        this.playerPos = data["player_pos"]?.toIntOrNull() ?: 0
        this.playerPage = data["player_page"]?.toIntOrNull() ?: 0
    }

    fun getPlayerUuid(): String? = playerUuid
    fun setPlayerUuid(value: String?) { this.playerUuid = value }

    fun getPlayerSetting(): String? = playerSetting
    fun setPlayerSetting(value: String?) { this.playerSetting = value }

    fun getPlayerPossessionCount(): Int = playerPossessionCount
    fun setPlayerPossessionCount(value: Int) { this.playerPossessionCount = value }

    fun getPlayerPos(): Int = playerPos
    fun setPlayerPos(value: Int) { this.playerPos = value }

    fun getPlayerPage(): Int = playerPage
    fun setPlayerPage(value: Int) { this.playerPage = value }
}
