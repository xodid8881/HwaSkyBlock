package org.hwabeag.hwaskyblock.database

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.skyblock.SelectSkyblock
import org.hwabeag.hwaskyblock.database.mysql.skyblock.UpdateSkyblock
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user


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

    fun getSkyBlockData(skyblockId: String, type: String?): Any? {
        return when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig.get(skyblockId)

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

    fun setSkyBlockData(skyblockId: String, value: String, type: String?) {
        when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig.set(skyblockId, value)
            }

            "mysql", "sqlite" -> {
                if (Skyblock_Select.SelectSkyBlock(skyblockId) == 0) {
                    when (type) {
                        "setSkyBlockName" -> Update_Skyblock.updateSkyblockName(skyblockId, value)
                        "setSkyBlockLeader" -> Update_Skyblock.updateSkyblockLeader(skyblockId, value)
                        "setSkyBlockJoin" -> Update_Skyblock.updateSkyblockJoin(skyblockId, value)
                        "setSkyBlockBreak" -> Update_Skyblock.updateSkyblockBreak(skyblockId, value)
                        "setSkyBlockPlace" -> Update_Skyblock.updateSkyblockPlace(skyblockId, value)
                        "setSkyBlockDoor" -> Update_Skyblock.updateSkyblockDoor(skyblockId, value)
                        "setSkyBlockChest" -> Update_Skyblock.updateSkyblockChest(skyblockId, value)
                        "setSkyBlockBarrel" -> Update_Skyblock.updateSkyblockBarrel(skyblockId, value)
                        "setSkyBlockHopper" -> Update_Skyblock.updateSkyblockHopper(skyblockId, value)
                        "setSkyBlockFurnace" -> Update_Skyblock.updateSkyblockFurnace(skyblockId, value)
                        "setSkyBlockBlastFurnace" -> Update_Skyblock.updateSkyblockBlastFurnace(skyblockId, value)
                        "setSkyBlockShulkerBox" -> Update_Skyblock.updateSkyblockShulkerBox(skyblockId, value)
                        "setSkyBlockTrapdoor" -> Update_Skyblock.updateSkyblockTrapdoor(skyblockId, value)
                        "setSkyBlockButton" -> Update_Skyblock.updateSkyblockButton(skyblockId, value)
                        "setSkyBlockAnvil" -> Update_Skyblock.updateSkyblockAnvil(skyblockId, value)
                        "setSkyBlockFarm" -> Update_Skyblock.updateSkyblockFarm(skyblockId, value)
                        "setSkyBlockBeacon" -> Update_Skyblock.updateSkyblockBeacon(skyblockId, value)
                        "setSkyBlockMinecart" -> Update_Skyblock.updateSkyblockMinecart(skyblockId, value)
                        "setSkyBlockBoat" -> Update_Skyblock.updateSkyblockBoat(skyblockId, value)
                        "setSkyBlockPvp" -> Update_Skyblock.updateSkyblockPvp(skyblockId, value)
                        "setSkyBlockWelcomeMessage" -> Update_Skyblock.updateSkyblockWelcomeMessage(skyblockId, value)
                        "setSkyBlockHome" -> Update_Skyblock.updateSkyblockHome(skyblockId, value)
                        "setSkyBlockSize" -> Update_Skyblock.updateSkyblockSize(skyblockId, value)
                        "setSkyBlockMonsterSpawn" -> Update_Skyblock.updateSkyblockMonsterSpawn(skyblockId, value)
                        "setSkyBlockAnimalSpawn" -> Update_Skyblock.updateSkyblockAnimalSpawn(skyblockId, value)
                        "setSkyBlockWeather" -> Update_Skyblock.updateSkyblockWeather(skyblockId, value)
                        "setSkyBlockTime" -> Update_Skyblock.updateSkyblockTime(skyblockId, value)
                        "setSkyBlockWaterPhysics" -> Update_Skyblock.updateSkyblockWaterPhysics(skyblockId, value)
                        "setSkyBlockLavaPhysics" -> Update_Skyblock.updateSkyblockLavaPhysics(skyblockId, value)
                    }
                }
            }
        }
    }

    fun getUserData(player: Player, type: String?): Any? {
        val uuid = player.uniqueId.toString()
        return when (Config.get("database.type")) {
            "yml" -> PlayerConfig.get(uuid)

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

    fun setUserData(player: Player, value: String, type: String?) {
        val uuid = player.uniqueId.toString()
        when (Config.get("database.type")) {
            "yml" -> {
                PlayerConfig.set(uuid, value)
            }

            "mysql", "sqlite" -> {
                if (User_Select.UserSelect(player) == 0) {
                    when (type) {
                        "setPlayerSetting" -> Update_User.UserUpdate_Setting(player, value)
                        "setPlayerPossessionCount" -> Update_User.UserUpdate_Possession_Count(player, value.toIntOrNull() ?: 0)
                        "setPlayerPos" -> Update_User.UserUpdate_Pos(player, value.toIntOrNull() ?: 0)
                        "setPlayerPage" -> Update_User.UserUpdate_Page(player, value.toIntOrNull() ?: 0)
                    }
                }
            }
        }
    }

    companion object {
        var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> =
            HashMap<String?, hwaskyblock_skyblock?>()
        var Select_User_List: HashMap<String?, hwaskyblock_user?> =
            HashMap<String?, hwaskyblock_user?>()
    }
}