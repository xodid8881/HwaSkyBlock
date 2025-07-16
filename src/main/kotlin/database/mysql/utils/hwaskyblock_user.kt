package org.hwabeag.hwaskyblock.database.mysql.utils

class hwaskyblock_user {

    private var playerUuid: String? = null
    private var playerSetting: String? = null
    private var playerPossessionCount: Int = 0
    private var playerPos: Int = 0
    private var playerPage: Int = 0

    fun getPlayerUuid(): String? {
        return playerUuid
    }

    fun setPlayerUuid(value: String?) {
        this.playerUuid = value
    }

    fun getPlayerSetting(): String? {
        return playerSetting
    }

    fun setPlayerSetting(value: String?) {
        this.playerSetting = value
    }

    fun getPlayerPossessionCount(): Int {
        return playerPossessionCount
    }

    fun setPlayerPossessionCount(value: Int) {
        this.playerPossessionCount = value
    }

    fun getPlayerPos(): Int {
        return playerPos
    }

    fun setPlayerPos(value: Int) {
        this.playerPos = value
    }

    fun getPlayerPage(): Int {
        return playerPage
    }

    fun setPlayerPage(value: Int) {
        this.playerPage = value
    }
}
