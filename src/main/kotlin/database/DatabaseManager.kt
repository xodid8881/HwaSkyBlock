package org.hwabeag.hwaskyblock.database

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.skyblock.*
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_share_sqlite
import org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_skyblock_sqlite


class DatabaseManager {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
    var PlayerConfig: FileConfiguration = ConfigManager.getConfig("player")!!

    var User_Select: SelectUser = SelectUser()
    var Update_User: UpdateUser = UpdateUser()
    var Skyblock_Select: SelectSkyblock = SelectSkyblock()
    var Update_Skyblock: UpdateSkyblock = UpdateSkyblock()

    var Select_User_List: HashMap<String?, hwaskyblock_user?> = HashMap<String?, hwaskyblock_user?>()
    var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> = HashMap<String?, hwaskyblock_skyblock?>()

    fun getSkyBlockDataStatic(skyblockId: String, data: String, type: String?): Any? {
        return when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.get(data)
            "mysql", "sqlite" -> {
                if (Skyblock_Select.SelectSkyBlock(skyblockId) != 0) return null
                val skyblock = Select_Skyblock_List[skyblockId] ?: return null
                when (type) {
                    "getSkyBlockName" -> skyblock.getSkyBlockName()
                    "getSkyBlockLeader" -> skyblock.getSkyBlockLeader()
                    "isSkyBlockJoin" -> skyblock.isSkyBlockJoin()
                    "isSkyBlockBreak" -> skyblock.isSkyBlockBreak()
                    "isSkyBlockPlace" -> skyblock.isSkyBlockPlace()
                    "isSkyBlockDoor" -> skyblock.isSkyBlockDoor()
                    "isSkyBlockChest" -> skyblock.isSkyBlockChest()
                    "isSkyBlockBarrel" -> skyblock.isSkyBlockBarrel()
                    "isSkyBlockHopper" -> skyblock.isSkyBlockHopper()
                    "isSkyBlockFurnace" -> skyblock.isSkyBlockFurnace()
                    "isSkyBlockBlastFurnace" -> skyblock.isSkyBlockBlastFurnace()
                    "isSkyBlockShulkerBox" -> skyblock.isSkyBlockShulkerBox()
                    "isSkyBlockTrapdoor" -> skyblock.isSkyBlockTrapdoor()
                    "isSkyBlockButton" -> skyblock.isSkyBlockButton()
                    "isSkyBlockAnvil" -> skyblock.isSkyBlockAnvil()
                    "isSkyBlockFarm" -> skyblock.isSkyBlockFarm()
                    "isSkyBlockBeacon" -> skyblock.isSkyBlockBeacon()
                    "isSkyBlockMinecart" -> skyblock.isSkyBlockMinecart()
                    "isSkyBlockBoat" -> skyblock.isSkyBlockBoat()
                    "isSkyBlockPvp" -> skyblock.isSkyBlockPvp()
                    "getSkyBlockWelcomeMessage" -> skyblock.getSkyBlockWelcomeMessage()
                    "getSkyBlockHome" -> skyblock.getSkyBlockHome()
                    "getSkyBlockSize" -> skyblock.getSkyBlockSize()
                    "isSkyBlockMonsterSpawn" -> skyblock.isSkyBlockMonsterSpawn()
                    "isSkyBlockAnimalSpawn" -> skyblock.isSkyBlockAnimalSpawn()
                    "isSkyBlockWeather" -> skyblock.isSkyBlockWeather()
                    "isSkyBlockTime" -> skyblock.isSkyBlockTime()
                    "isSkyBlockWaterPhysics" -> skyblock.isSkyBlockWaterPhysics()
                    "isSkyBlockLavaPhysics" -> skyblock.isSkyBlockLavaPhysics()
                    else -> null
                }
            }

            else -> null
        }
    }

    fun setSkyBlockDataStatic(skyblockId: String, data: String, value: Any?, type: String?) {
        when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig.set(data, value)
            }

            "mysql", "sqlite" -> {
                if (Skyblock_Select.SelectSkyBlock(skyblockId) == 0) {
                    val stringValue = when (value) {
                        is Boolean -> value.toString()
                        is Int -> value.toString()
                        is String -> value
                        else -> value.toString()
                    }

                    when (type) {
                        "setSkyBlockName" -> Update_Skyblock.updateSkyblockName(skyblockId, stringValue)
                        "setSkyBlockLeader" -> Update_Skyblock.updateSkyblockLeader(skyblockId, stringValue)
                        "setSkyBlockJoin" -> Update_Skyblock.updateSkyblockJoin(skyblockId, stringValue)
                        "setSkyBlockBreak" -> Update_Skyblock.updateSkyblockBreak(skyblockId, stringValue)
                        "setSkyBlockPlace" -> Update_Skyblock.updateSkyblockPlace(skyblockId, stringValue)
                        "setSkyBlockDoor" -> Update_Skyblock.updateSkyblockDoor(skyblockId, stringValue)
                        "setSkyBlockChest" -> Update_Skyblock.updateSkyblockChest(skyblockId, stringValue)
                        "setSkyBlockBarrel" -> Update_Skyblock.updateSkyblockBarrel(skyblockId, stringValue)
                        "setSkyBlockHopper" -> Update_Skyblock.updateSkyblockHopper(skyblockId, stringValue)
                        "setSkyBlockFurnace" -> Update_Skyblock.updateSkyblockFurnace(skyblockId, stringValue)
                        "setSkyBlockBlastFurnace" -> Update_Skyblock.updateSkyblockBlastFurnace(skyblockId, stringValue)
                        "setSkyBlockShulkerBox" -> Update_Skyblock.updateSkyblockShulkerBox(skyblockId, stringValue)
                        "setSkyBlockTrapdoor" -> Update_Skyblock.updateSkyblockTrapdoor(skyblockId, stringValue)
                        "setSkyBlockButton" -> Update_Skyblock.updateSkyblockButton(skyblockId, stringValue)
                        "setSkyBlockAnvil" -> Update_Skyblock.updateSkyblockAnvil(skyblockId, stringValue)
                        "setSkyBlockFarm" -> Update_Skyblock.updateSkyblockFarm(skyblockId, stringValue)
                        "setSkyBlockBeacon" -> Update_Skyblock.updateSkyblockBeacon(skyblockId, stringValue)
                        "setSkyBlockMinecart" -> Update_Skyblock.updateSkyblockMinecart(skyblockId, stringValue)
                        "setSkyBlockBoat" -> Update_Skyblock.updateSkyblockBoat(skyblockId, stringValue)
                        "setSkyBlockPvp" -> Update_Skyblock.updateSkyblockPvp(skyblockId, stringValue)
                        "setSkyBlockWelcomeMessage" -> Update_Skyblock.updateSkyblockWelcomeMessage(
                            skyblockId,
                            stringValue
                        )

                        "setSkyBlockHome" -> Update_Skyblock.updateSkyblockHome(skyblockId, stringValue)
                        "setSkyBlockSize" -> Update_Skyblock.updateSkyblockSize(skyblockId, stringValue)
                        "setSkyBlockMonsterSpawn" -> Update_Skyblock.updateSkyblockMonsterSpawn(skyblockId, stringValue)
                        "setSkyBlockAnimalSpawn" -> Update_Skyblock.updateSkyblockAnimalSpawn(skyblockId, stringValue)
                        "setSkyBlockWeather" -> Update_Skyblock.updateSkyblockWeather(skyblockId, stringValue)
                        "setSkyBlockTime" -> Update_Skyblock.updateSkyblockTime(skyblockId, stringValue)
                        "setSkyBlockWaterPhysics" -> Update_Skyblock.updateSkyblockWaterPhysics(skyblockId, stringValue)
                        "setSkyBlockLavaPhysics" -> Update_Skyblock.updateSkyblockLavaPhysics(skyblockId, stringValue)
                    }
                }
            }
        }
    }

    fun getUserDataStatic(data: String, player: Player, type: String?): Any? {
        val uuid = player.uniqueId.toString()
        return when (Config.get("database.type")) {
            "yml" -> PlayerConfig.get(data)
            "mysql", "sqlite" -> {
                if (User_Select.UserSelect(player) != 0) return null
                val user = Select_User_List[uuid] ?: return null
                when (type) {
                    "getPlayerSetting" -> user.getPlayerSetting()
                    "getPlayerPossessionCount" -> user.getPlayerPossessionCount()
                    "getPlayerPos" -> user.getPlayerPos()
                    "getPlayerPage" -> user.getPlayerPage()
                    else -> null
                }
            }

            else -> null
        }
    }

    fun setUserDataStatic(data: String, player: Player, value: Any?, type: String?) {
        val uuid = player.uniqueId.toString()
        when (Config.get("database.type")) {
            "yml" -> {
                PlayerConfig.set(data, value)
            }

            "mysql", "sqlite" -> {
                if (User_Select.UserSelect(player) == 0) {
                    when (type) {
                        "setPlayerSetting" -> Update_User.UserUpdate_Setting(player, value.toString())
                        "setPlayerPossessionCount" -> {
                            val intValue = (value as? Int) ?: value.toString().toIntOrNull() ?: 0
                            Update_User.UserUpdate_Possession_Count(player, intValue)
                        }

                        "setPlayerPos" -> {
                            val intValue = (value as? Int) ?: value.toString().toIntOrNull() ?: 0
                            Update_User.UserUpdate_Pos(player, intValue)
                        }

                        "setPlayerPage" -> {
                            val intValue = (value as? Int) ?: value.toString().toIntOrNull() ?: 0
                            Update_User.UserUpdate_Page(player, intValue)
                        }
                    }
                }
            }
        }
    }

    fun getSkyBlockShareListStatic(skyblockId: String): List<String> {
        return when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig.getConfigurationSection("$skyblockId.sharer")?.getKeys(false)?.toList() ?: emptyList()
            }

            "mysql" -> {
                val shareDAO = org.hwabeag.hwaskyblock.database.mysql.skyblock.SelectSkyblockShare()
                shareDAO.getShareList(skyblockId)
            }

            "sqlite" -> {
                val shareDAO = org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_share_sqlite()
                shareDAO.getShareList(skyblockId)
            }

            else -> emptyList()
        }
    }

    fun addSkyBlockShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.set("$skyblockId.sharer.$playerName", true)
            "mysql" -> {
                val dao = org.hwabeag.hwaskyblock.database.mysql.skyblock.InsertSkyblockShare()
                dao.insertShare(skyblockId, playerName)
            }

            "sqlite" -> {
                val dao = org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_share_sqlite()
                dao.insertShare(skyblockId, playerName)
            }
        }
    }

    fun removeSkyBlockShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.set("$skyblockId.sharer.$playerName", null)
            "mysql" -> {
                val dao = org.hwabeag.hwaskyblock.database.mysql.skyblock.DeleteSkyblockShare()
                dao.deleteShare(skyblockId, playerName)
            }

            "sqlite" -> {
                val dao = org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_share_sqlite()
                dao.deleteShare(skyblockId, playerName)
            }
        }
    }

    fun getSkyBlockSharePermissionStatic(skyblockId: String, playerName: String, permission: String): Boolean? {
        return when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.getBoolean("$skyblockId.sharer.$playerName.$permission", true)
            "mysql" -> {
                val dao = SelectSkyblockShare()
                dao.getPermission(skyblockId, playerName, permission)
            }

            "sqlite" -> {
                val dao = hwaskyblock_share_sqlite()
                dao.getPermission(skyblockId, playerName, permission)
            }

            else -> null
        }
    }

    fun setSkyBlockSharePermissionStatic(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.set("$skyblockId.sharer.$playerName.$permission", value)
            "mysql" -> {
                val dao = UpdateSkyblockShare()
                dao.updatePermission(skyblockId, playerName, permission, value)
            }

            "sqlite" -> {
                val dao = hwaskyblock_share_sqlite()
                dao.updatePermission(skyblockId, playerName, permission, value)
            }
        }
    }

    fun removeSkyBlockSharePermissionStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig.set("$skyblockId.sharer.$playerName", null)
            }

            "mysql" -> {
                val dao = DeleteSkyblockShare()
                dao.deleteShare(skyblockId, playerName)
            }

            "sqlite" -> {
                val dao = hwaskyblock_share_sqlite()
                dao.deleteShare(skyblockId, playerName)
            }
        }
    }

    fun DeleteSkyBlockStatic(skyblockId: String) {
        when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig.set(skyblockId, null)
            }

            "mysql" -> {
                val dao = DeleteSkyblock()
                dao.deleteSkyblock(skyblockId)
            }

            "sqlite" -> {
                val dao = hwaskyblock_skyblock_sqlite()
                dao.deleteSkyblock(skyblockId)
            }
        }
        Select_Skyblock_List.remove(skyblockId)
    }


    companion object {
        var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> =
            HashMap<String?, hwaskyblock_skyblock?>()
        var Select_User_List: HashMap<String?, hwaskyblock_user?> =
            HashMap<String?, hwaskyblock_user?>()
        var Select_Share_List: HashMap<String?, Map<String, Boolean>> = HashMap()

        @JvmStatic
        fun getSkyBlockData(skyblockId: String, data: String, type: String?): Any? {
            return DatabaseManager().getSkyBlockDataStatic(skyblockId, data, type)
        }

        @JvmStatic
        fun setSkyBlockData(skyblockId: String, data: String, value: Any?, type: String?) {
            DatabaseManager().setSkyBlockDataStatic(skyblockId, data, value, type)
        }

        @JvmStatic
        fun getUserData(data: String, player: Player, type: String?): Any? {
            return DatabaseManager().getUserDataStatic(data, player, type)
        }

        @JvmStatic
        fun setUserData(data: String, player: Player, value: Any?, type: String?) {
            DatabaseManager().setUserDataStatic(data, player, value, type)
        }

        @JvmStatic
        fun getSkyBlockShareList(skyblockId: String): List<String> {
            return DatabaseManager().getSkyBlockShareListStatic(skyblockId)
        }

        @JvmStatic
        fun addSkyBlockShare(skyblockId: String, playerName: String) {
            DatabaseManager().addSkyBlockShareStatic(skyblockId, playerName)
        }

        @JvmStatic
        fun removeSkyBlockShare(skyblockId: String, playerName: String) {
            DatabaseManager().removeSkyBlockShareStatic(skyblockId, playerName)
        }

        @JvmStatic
        fun getSkyBlockSharePermission(skyblockId: String, playerName: String, permission: String): Boolean? {
            return DatabaseManager().getSkyBlockSharePermissionStatic(skyblockId, playerName, permission)
        }

        @JvmStatic
        fun setSkyBlockSharePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) {
            DatabaseManager().setSkyBlockSharePermissionStatic(skyblockId, playerName, permission, value)
        }

        @JvmStatic
        fun removeSkyBlockSharePermission(skyblockId: String, playerName: String) {
            DatabaseManager().removeSkyBlockSharePermissionStatic(skyblockId, playerName)
        }

        @JvmStatic
        fun DeleteSkyBlock(skyblockId: String) {
            DatabaseManager().DeleteSkyBlockStatic(skyblockId)
        }
    }
}