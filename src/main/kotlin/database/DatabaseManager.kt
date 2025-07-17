package org.hwabeag.hwaskyblock.database

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabeag.hwaskyblock.database.config.ConfigManager
import org.hwabeag.hwaskyblock.database.mysql.skyblock.*
import org.hwabeag.hwaskyblock.database.mysql.user.InsertUser
import org.hwabeag.hwaskyblock.database.mysql.user.SelectUser
import org.hwabeag.hwaskyblock.database.mysql.user.UpdateUser
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabeag.hwaskyblock.database.mysql.utils.hwaskyblock_user
import org.hwabeag.hwaskyblock.database.sqlite.skyblock.SkyblockDAO
import org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_share_sqlite
import org.hwabeag.hwaskyblock.database.sqlite.skyblock.hwaskyblock_skyblock_sqlite
import org.hwabeag.hwaskyblock.database.sqlite.user.UserDAO
import org.json.simple.JSONObject


class DatabaseManager {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var SkyBlockConfig: FileConfiguration? = null
    var PlayerConfig: FileConfiguration? = null

    init {
        val dbType = Config.getString("database.type")
        if (dbType == "yml") {
            SkyBlockConfig = ConfigManager.getConfig("skyblock")
            PlayerConfig = ConfigManager.getConfig("player")
        }
    }

    var User_Select: SelectUser = SelectUser()
    var Update_User: UpdateUser = UpdateUser()
    var Skyblock_Select: SelectSkyblock = SelectSkyblock()
    var Update_Skyblock: UpdateSkyblock = UpdateSkyblock()

    var Select_User_List: HashMap<String?, hwaskyblock_user?> = HashMap()
    var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> = HashMap()

    fun insertSkyBlockStatic(skyblockId: String, name: String) {
        when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig?.createSection(skyblockId)
            }

            "sqlite" -> {
                val dao = SkyblockDAO()
                val data = mutableMapOf<String, String>()

                data["skyblock_id"] = skyblockId
                data["skyblock_name"] = name
                data["skyblock_leader"] = name

                data["skyblock_join"] = "true"
                data["skyblock_break"] = "false"
                data["skyblock_place"] = "false"

                data["skyblock_door"] = "false"
                data["skyblock_chest"] = "false"
                data["skyblock_barrel"] = "false"
                data["skyblock_hopper"] = "false"
                data["skyblock_furnace"] = "false"
                data["skyblock_blast_furnace"] = "false"
                data["skyblock_shulker_box"] = "false"
                data["skyblock_trapdoor"] = "false"
                data["skyblock_button"] = "false"
                data["skyblock_anvil"] = "false"
                data["skyblock_farm"] = "false"
                data["skyblock_beacon"] = "false"
                data["skyblock_minecart"] = "false"
                data["skyblock_boat"] = "false"
                data["skyblock_pvp"] = "false"

                data["skyblock_welcome_message"] = "Welcome to $skyblockId Skyblock!"
                data["skyblock_home"] = "0"
                data["skyblock_size"] = "0"

                data["skyblock_monster_spawn"] = "true"
                data["skyblock_animal_spawn"] = "true"
                data["skyblock_weather"] = "basic"
                data["skyblock_time"] = "basic"
                data["skyblock_water_physics"] = "true"
                data["skyblock_lava_physics"] = "true"

                dao.insertSkyblock(data)
            }

            "mysql" -> {
                val insert = InsertSkyblock()
                insert.insertSkyblock(skyblockId, name, name)

                val defaults = mapOf(
                    "setSkyBlockJoin" to "true",
                    "setSkyBlockBreak" to "false",
                    "setSkyBlockPlace" to "false",
                    "setSkyBlockDoor" to "false",
                    "setSkyBlockChest" to "false",
                    "setSkyBlockBarrel" to "false",
                    "setSkyBlockHopper" to "false",
                    "setSkyBlockFurnace" to "false",
                    "setSkyBlockBlastFurnace" to "false",
                    "setSkyBlockShulkerBox" to "false",
                    "setSkyBlockTrapdoor" to "false",
                    "setSkyBlockButton" to "false",
                    "setSkyBlockAnvil" to "false",
                    "setSkyBlockFarm" to "false",
                    "setSkyBlockBeacon" to "false",
                    "setSkyBlockMinecart" to "false",
                    "setSkyBlockBoat" to "false",
                    "setSkyBlockPvp" to "false",
                    "setSkyBlockWelcomeMessage" to "Welcome to $skyblockId Skyblock!",
                    "setSkyBlockHome" to "0",
                    "setSkyBlockSize" to "0",
                    "setSkyBlockMonsterSpawn" to "true",
                    "setSkyBlockAnimalSpawn" to "true",
                    "setSkyBlockWeather" to "basic",
                    "setSkyBlockTime" to "basic",
                    "setSkyBlockWaterPhysics" to "true",
                    "setSkyBlockLavaPhysics" to "true"
                )

                for ((type, value) in defaults) {
                    setSkyBlockData(skyblockId, "$skyblockId.${type.removePrefix("setSkyBlock").lowercase()}", value, type)
                }

                Skyblock_Select.SelectSkyBlock(skyblockId)
            }
        }
    }

    fun getSkyBlockDataStatic(skyblockId: String, data: String, type: String?): Any? {
        return when (Config.get("database.type")) {
            "yml" -> {
                SkyBlockConfig?.get(data)
            }

            "sqlite" -> {
                val dao = org.hwabeag.hwaskyblock.database.sqlite.skyblock.SkyblockDAO()
                val result = dao.getSkyblock(skyblockId) ?: return null

                val column = when (type) {
                    "getSkyBlockName" -> "skyblock_name"
                    "getSkyBlockLeader" -> "skyblock_leader"
                    "isSkyBlockJoin" -> "skyblock_join"
                    "isSkyBlockBreak" -> "skyblock_break"
                    "isSkyBlockPlace" -> "skyblock_place"
                    "isSkyBlockDoor" -> "skyblock_door"
                    "isSkyBlockChest" -> "skyblock_chest"
                    "isSkyBlockBarrel" -> "skyblock_barrel"
                    "isSkyBlockHopper" -> "skyblock_hopper"
                    "isSkyBlockFurnace" -> "skyblock_furnace"
                    "isSkyBlockBlastFurnace" -> "skyblock_blast_furnace"
                    "isSkyBlockShulkerBox" -> "skyblock_shulker_box"
                    "isSkyBlockTrapdoor" -> "skyblock_trapdoor"
                    "isSkyBlockButton" -> "skyblock_button"
                    "isSkyBlockAnvil" -> "skyblock_anvil"
                    "isSkyBlockFarm" -> "skyblock_farm"
                    "isSkyBlockBeacon" -> "skyblock_beacon"
                    "isSkyBlockMinecart" -> "skyblock_minecart"
                    "isSkyBlockBoat" -> "skyblock_boat"
                    "isSkyBlockPvp" -> "skyblock_pvp"
                    "getSkyBlockWelcomeMessage" -> "skyblock_welcome_message"
                    "getSkyBlockHome" -> "skyblock_home"
                    "getSkyBlockSize" -> "skyblock_size"
                    "isSkyBlockMonsterSpawn" -> "skyblock_monster_spawn"
                    "isSkyBlockAnimalSpawn" -> "skyblock_animal_spawn"
                    "isSkyBlockWeather" -> "skyblock_weather"
                    "isSkyBlockTime" -> "skyblock_time"
                    "isSkyBlockWaterPhysics" -> "skyblock_water_physics"
                    "isSkyBlockLavaPhysics" -> "skyblock_lava_physics"
                    else -> null
                }

                column?.let { result[it] }
            }

            "mysql" -> {
                if (Skyblock_Select.SelectSkyBlock(skyblockId) != 0) return null
                val skyblock = Select_Skyblock_List[skyblockId] ?: return null

                return when (type) {
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
                SkyBlockConfig?.set(data, value)
            }

            "sqlite" -> {
                val stringValue = value?.toString() ?: return
                val dao = org.hwabeag.hwaskyblock.database.sqlite.skyblock.SkyblockDAO()

                val column = when (type) {
                    "setSkyBlockName" -> "skyblock_name"
                    "setSkyBlockLeader" -> "skyblock_leader"
                    "setSkyBlockJoin" -> "skyblock_join"
                    "setSkyBlockBreak" -> "skyblock_break"
                    "setSkyBlockPlace" -> "skyblock_place"
                    "setSkyBlockDoor" -> "skyblock_door"
                    "setSkyBlockChest" -> "skyblock_chest"
                    "setSkyBlockBarrel" -> "skyblock_barrel"
                    "setSkyBlockHopper" -> "skyblock_hopper"
                    "setSkyBlockFurnace" -> "skyblock_furnace"
                    "setSkyBlockBlastFurnace" -> "skyblock_blast_furnace"
                    "setSkyBlockShulkerBox" -> "skyblock_shulker_box"
                    "setSkyBlockTrapdoor" -> "skyblock_trapdoor"
                    "setSkyBlockButton" -> "skyblock_button"
                    "setSkyBlockAnvil" -> "skyblock_anvil"
                    "setSkyBlockFarm" -> "skyblock_farm"
                    "setSkyBlockBeacon" -> "skyblock_beacon"
                    "setSkyBlockMinecart" -> "skyblock_minecart"
                    "setSkyBlockBoat" -> "skyblock_boat"
                    "setSkyBlockPvp" -> "skyblock_pvp"
                    "setSkyBlockWelcomeMessage" -> "skyblock_welcome_message"
                    "setSkyBlockHome" -> "skyblock_home"
                    "setSkyBlockSize" -> "skyblock_size"
                    "setSkyBlockMonsterSpawn" -> "skyblock_monster_spawn"
                    "setSkyBlockAnimalSpawn" -> "skyblock_animal_spawn"
                    "setSkyBlockWeather" -> "skyblock_weather"
                    "setSkyBlockTime" -> "skyblock_time"
                    "setSkyBlockWaterPhysics" -> "skyblock_water_physics"
                    "setSkyBlockLavaPhysics" -> "skyblock_lava_physics"
                    else -> null
                }

                if (column != null) {
                    dao.updateSkyblock(skyblockId, mapOf(column to stringValue))
                }
            }

            "mysql" -> {
                val stringValue = value?.toString() ?: return
                if (Skyblock_Select.SelectSkyBlock(skyblockId) != 0) return

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
                    "setSkyBlockWelcomeMessage" -> Update_Skyblock.updateSkyblockWelcomeMessage(skyblockId, stringValue)
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

    fun insertUserStatic(player: Player) {
        val uuid = player.uniqueId.toString()

        when (Config.get("database.type")) {
            "yml" -> {
                PlayerConfig?.set("${player.name}.skyblock.possession_count", 0)
                PlayerConfig?.set("${player.name}.skyblock.pos", 0)
                PlayerConfig?.set("${player.name}.skyblock.page", 1)
                PlayerConfig?.set("${player.name}.skyblock.setting", "")
            }

            "sqlite" -> {
                val dao = UserDAO()
                if (dao.getUser(uuid) != null) return

                val initialSetting = mapOf(
                    "possession" to emptyMap<String, Boolean>(),
                    "sharer" to emptyMap<String, Boolean>()
                )
                val jsonSetting = JSONObject(initialSetting).toJSONString()

                val userData = mapOf(
                    "player_uuid" to uuid,
                    "player_setting" to jsonSetting,
                    "player_possession_count" to 0,
                    "player_pos" to "0",
                    "player_page" to 1
                )
                dao.insertUser(userData)
            }

            "mysql" -> {
                val update = InsertUser()
                update.UserInsert(player)
            }
        }
    }

    fun updateUserStatic(player: Player, values: Map<String, Any>) {
        val uuid = player.uniqueId.toString()

        when (Config.get("database.type")) {
            "yml" -> {
                values.forEach { (key, value) ->
                    PlayerConfig?.set("${player.name}.skyblock.$key", value)
                }
            }

            "sqlite" -> {
                val dao = UserDAO()
                dao.updateUser(uuid, values)
            }

            "mysql" -> {
                if (User_Select.UserSelect(player) == 0) {
                    values.forEach { (key, value) ->
                        when (key) {
                            "player_setting" -> Update_User.UserUpdate_Setting(player, value.toString())
                            "player_possession_count" -> Update_User.UserUpdate_Possession_Count(player, (value as? Int) ?: value.toString().toInt())
                            "player_pos" -> Update_User.UserUpdate_Pos(player, (value as? Int) ?: value.toString().toInt())
                            "player_page" -> Update_User.UserUpdate_Page(player, (value as? Int) ?: value.toString().toInt())
                        }
                    }
                }
            }
        }
    }

    fun deleteUserStatic(player: Player) {
        val uuid = player.uniqueId.toString()

        when (Config.get("database.type")) {
            "yml" -> PlayerConfig?.set(player.name, null)
            "sqlite" -> UserDAO().deleteUser(uuid)
            "mysql" -> {
                // MySQL의 삭제 클래스/메서드가 없으면 구현 필요
                // 예시: DeleteUser().delete(player)
            }
        }

        Select_User_List.remove(uuid)
    }

    fun getUserDataStatic(data: String, player: Player, type: String?): Any? {
        val uuid = player.uniqueId.toString()

        return when (Config.get("database.type")) {
            "yml" -> PlayerConfig?.get(data)

            "sqlite" -> {
                val dao = UserDAO()
                val result = dao.getUser(uuid) ?: return null

                val path = data.split(".")
                if (path.size >= 3 && path[1] == "skyblock") {
                    when (path[2]) {
                        "possession" -> {
                            val playerSetting = result["player_setting"] as? Map<*, *> ?: return null
                            val rawPossession = playerSetting["possession"] as? Map<*, *> ?: return null
                            return rawPossession.filterValues { it == true }
                        }
                        "sharer" -> {
                            val playerSetting = result["player_setting"] as? Map<*, *> ?: return null
                            val rawSharer = playerSetting["sharer"] as? Map<*, *> ?: return null
                            return rawSharer.filterValues { it == true }
                        }
                        "page" -> return result["player_page"]
                    }
                }
                return result[data]
            }

            "mysql" -> {
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
            "yml" -> PlayerConfig?.set(data, value)

            "sqlite" -> {
                val dao = UserDAO()
                val uuid = player.uniqueId.toString()
                val userData = dao.getUser(uuid)?.toMutableMap() ?: return
                val settingMap = userData["player_setting"] as? Map<*, *> ?: emptyMap<Any, Any>()

                val newSetting = settingMap.toMutableMap()

                if (data.startsWith("${player.name}.skyblock.possession.")) {
                    val islandId = data.substringAfterLast(".")
                    val possession = (newSetting["possession"] as? MutableMap<String, Boolean> ?: mutableMapOf())
                    possession[islandId] = true
                    newSetting["possession"] = possession
                }

                val jsonString = JSONObject(newSetting).toJSONString()
                dao.updateUser(uuid, mapOf("player_setting" to jsonString))
            }

            "mysql" -> {
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
            "yml" -> SkyBlockConfig?.getConfigurationSection("$skyblockId.sharer")?.getKeys(false)?.toList()
                ?: emptyList()

            "mysql" -> SelectSkyblockShare().getShareList(skyblockId)
            "sqlite" -> hwaskyblock_share_sqlite().getShareList(skyblockId)
            else -> emptyList()
        }
    }

    fun addSkyBlockShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.set("$skyblockId.sharer.$playerName", true)
            "mysql" -> InsertSkyblockShare().insertShare(skyblockId, playerName)
            "sqlite" -> hwaskyblock_share_sqlite().insertShare(skyblockId, playerName)
        }
    }

    fun removeSkyBlockShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.set("$skyblockId.sharer.$playerName", null)
            "mysql" -> DeleteSkyblockShare().deleteShare(skyblockId, playerName)
            "sqlite" -> hwaskyblock_share_sqlite().deleteShare(skyblockId, playerName)
        }
    }

    fun getSkyBlockSharePermissionStatic(skyblockId: String, playerName: String, permission: String): Boolean? {
        return when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.getBoolean("$skyblockId.sharer.$playerName.$permission", true)
            "mysql" -> SelectSkyblockShare().getPermission(skyblockId, playerName, permission)
            "sqlite" -> hwaskyblock_share_sqlite().getPermission(skyblockId, playerName, permission)
            else -> null
        }
    }

    fun setSkyBlockSharePermissionStatic(skyblockId: String, playerName: String, permission: String, value: Boolean) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.set("$skyblockId.sharer.$playerName.$permission", value)
            "mysql" -> UpdateSkyblockShare().updatePermission(skyblockId, playerName, permission, value)
            "sqlite" -> hwaskyblock_share_sqlite().updatePermission(skyblockId, playerName, permission, value)
        }
    }

    fun removeSkyBlockSharePermissionStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.set("$skyblockId.sharer.$playerName", null)
            "mysql" -> DeleteSkyblockShare().deleteShare(skyblockId, playerName)
            "sqlite" -> hwaskyblock_share_sqlite().deleteShare(skyblockId, playerName)
        }
    }

    fun DeleteSkyBlockStatic(skyblockId: String) {
        when (Config.get("database.type")) {
            "yml" -> SkyBlockConfig?.set(skyblockId, null)
            "mysql" -> DeleteSkyblock().deleteSkyblock(skyblockId)
            "sqlite" -> hwaskyblock_skyblock_sqlite().deleteSkyblock(skyblockId)
        }
        Select_Skyblock_List.remove(skyblockId)
    }

    companion object {
        var Select_Skyblock_List: HashMap<String?, hwaskyblock_skyblock?> = HashMap()
        var Select_User_List: HashMap<String?, hwaskyblock_user?> = HashMap()
        var Select_Share_List: HashMap<String?, Map<String, Boolean>> = HashMap()

        @JvmStatic
        fun insertSkyBlock(skyblockId: String, name: String) =
            DatabaseManager().insertSkyBlockStatic(skyblockId, name)

        @JvmStatic
        fun getSkyBlockData(skyblockId: String, data: String, type: String?) =
            DatabaseManager().getSkyBlockDataStatic(skyblockId, data, type)

        @JvmStatic
        fun setSkyBlockData(skyblockId: String, data: String, value: Any?, type: String?) =
            DatabaseManager().setSkyBlockDataStatic(skyblockId, data, value, type)

        @JvmStatic
        fun insertUser(player: Player) =
            DatabaseManager().insertUserStatic(player)

        @JvmStatic
        fun getUserData(data: String, player: Player, type: String?) =
            DatabaseManager().getUserDataStatic(data, player, type)

        @JvmStatic
        fun setUserData(data: String, player: Player, value: Any?, type: String?) =
            DatabaseManager().setUserDataStatic(data, player, value, type)

        @JvmStatic
        fun getSkyBlockShareList(skyblockId: String): List<String> =
            DatabaseManager().getSkyBlockShareListStatic(skyblockId)

        @JvmStatic
        fun addSkyBlockShare(skyblockId: String, playerName: String) =
            DatabaseManager().addSkyBlockShareStatic(skyblockId, playerName)

        @JvmStatic
        fun removeSkyBlockShare(skyblockId: String, playerName: String) =
            DatabaseManager().removeSkyBlockShareStatic(skyblockId, playerName)

        @JvmStatic
        fun getSkyBlockSharePermission(skyblockId: String, playerName: String, permission: String): Boolean? =
            DatabaseManager().getSkyBlockSharePermissionStatic(skyblockId, playerName, permission)

        @JvmStatic
        fun setSkyBlockSharePermission(skyblockId: String, playerName: String, permission: String, value: Boolean) =
            DatabaseManager().setSkyBlockSharePermissionStatic(skyblockId, playerName, permission, value)

        @JvmStatic
        fun removeSkyBlockSharePermission(skyblockId: String, playerName: String) =
            DatabaseManager().removeSkyBlockSharePermissionStatic(skyblockId, playerName)

        @JvmStatic
        fun DeleteSkyBlock(skyblockId: String) =
            DatabaseManager().DeleteSkyBlockStatic(skyblockId)

        @JvmStatic
        fun updateUser(player: Player, values: Map<String, Any>) =
            DatabaseManager().updateUserStatic(player, values)

        @JvmStatic
        fun deleteUser(player: Player) =
            DatabaseManager().deleteUserStatic(player)
    }
}