package org.hwabaeg.hwaskyblock.database

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import org.hwabaeg.hwaskyblock.database.mysql.share.MySQLShareDAO
import org.hwabaeg.hwaskyblock.database.mysql.skyblock.MySQLSkyblockDAO
import org.hwabaeg.hwaskyblock.database.mysql.skyblock.hwaskyblock_skyblock_mysql
import org.hwabaeg.hwaskyblock.database.mysql.user.MySQLUserDAO
import org.hwabaeg.hwaskyblock.database.mysql.utils.hwaskyblock_skyblock
import org.hwabaeg.hwaskyblock.database.mysql.utils.hwaskyblock_user
import org.hwabaeg.hwaskyblock.database.sqlite.share.ShareDAO
import org.hwabaeg.hwaskyblock.database.sqlite.skyblock.SkyblockDAO
import org.hwabaeg.hwaskyblock.database.sqlite.skyblock.hwaskyblock_skyblock_sqlite
import org.hwabaeg.hwaskyblock.database.sqlite.user.UserDAO
import org.json.simple.JSONObject


class DatabaseManager {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!

    fun insertSkyBlockStatic(skyblockId: String, name: String) {
        when (Config.get("database.type")) {
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
                val dao = MySQLSkyblockDAO()
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
        }
    }

    fun getSkyBlockDataStatic(skyblockId: String, data: String, type: String?): Any? {
        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = SkyblockDAO()
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
                val dao = MySQLSkyblockDAO()
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

            else -> null
        }
    }

    fun setSkyBlockDataStatic(skyblockId: String, data: String, value: Any?, type: String?) {
        val stringValue = value?.toString() ?: return

        val typeToColumn = mapOf(
            "setSkyBlockName" to "skyblock_name",
            "setSkyBlockLeader" to "skyblock_leader",
            "setSkyBlockJoin" to "skyblock_join",
            "setSkyBlockBreak" to "skyblock_break",
            "setSkyBlockPlace" to "skyblock_place",
            "setSkyBlockDoor" to "skyblock_door",
            "setSkyBlockChest" to "skyblock_chest",
            "setSkyBlockBarrel" to "skyblock_barrel",
            "setSkyBlockHopper" to "skyblock_hopper",
            "setSkyBlockFurnace" to "skyblock_furnace",
            "setSkyBlockBlastFurnace" to "skyblock_blast_furnace",
            "setSkyBlockShulkerBox" to "skyblock_shulker_box",
            "setSkyBlockTrapdoor" to "skyblock_trapdoor",
            "setSkyBlockButton" to "skyblock_button",
            "setSkyBlockAnvil" to "skyblock_anvil",
            "setSkyBlockFarm" to "skyblock_farm",
            "setSkyBlockBeacon" to "skyblock_beacon",
            "setSkyBlockMinecart" to "skyblock_minecart",
            "setSkyBlockBoat" to "skyblock_boat",
            "setSkyBlockPvp" to "skyblock_pvp",
            "setSkyBlockWelcomeMessage" to "skyblock_welcome_message",
            "setSkyBlockHome" to "skyblock_home",
            "setSkyBlockSize" to "skyblock_size",
            "setSkyBlockMonsterSpawn" to "skyblock_monster_spawn",
            "setSkyBlockAnimalSpawn" to "skyblock_animal_spawn",
            "setSkyBlockWeather" to "skyblock_weather",
            "setSkyBlockTime" to "skyblock_time",
            "setSkyBlockWaterPhysics" to "skyblock_water_physics",
            "setSkyBlockLavaPhysics" to "skyblock_lava_physics"
        )

        val column = typeToColumn[type] ?: return

        when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = SkyblockDAO()
                dao.updateSkyblock(skyblockId, mutableMapOf(column to stringValue))
            }

            "mysql" -> {
                val dao = MySQLSkyblockDAO()
                dao.updateSkyblock(skyblockId, mutableMapOf(column to stringValue))
            }
        }
    }

    fun insertUserStatic(player: Player) {
        val uuid = player.uniqueId.toString()

        when (Config.get("database.type")) {
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
                val dao = MySQLUserDAO()
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
        }
    }

    fun getUserDataStatic(data: String, player: Player?, type: String?): Any? {
        val uuid = player?.uniqueId.toString()

        return when (Config.get("database.type")) {
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
                val dao = MySQLUserDAO()
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

            else -> null
        }
    }

    fun setUserDataStatic(data: String, player: Player, value: Any?, type: String?) {
        val uuid = player.uniqueId.toString()

        when (Config.get("database.type")) {
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
                val dao = MySQLUserDAO()
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
        }
    }

    fun DeleteSkyBlockStatic(skyblockId: String) {
        when (Config.get("database.type")) {
            "mysql" -> hwaskyblock_skyblock_mysql().deleteSkyblock(skyblockId)
            "sqlite" -> hwaskyblock_skyblock_sqlite().deleteSkyblock(skyblockId)
        }
        Select_Skyblock_List.remove(skyblockId)
    }

    fun insertShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                val data = mutableMapOf<String, String>()
                data["skyblock_id"] = skyblockId
                data["player_name"] = playerName
                listOf(
                    "can_join", "can_break", "can_place", "use_door", "use_chest", "use_barrel", "use_hopper",
                    "use_furnace", "use_blast_furnace", "use_shulker_box", "use_trapdoor", "use_button",
                    "use_anvil", "use_farm", "use_beacon", "use_minecart", "use_boat"
                ).forEach { key -> data[key] = "true" }
                dao.insertShare(data)
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                val data = mutableMapOf<String, String>()
                data["skyblock_id"] = skyblockId
                data["player_name"] = playerName
                listOf(
                    "can_join", "can_break", "can_place", "use_door", "use_chest", "use_barrel", "use_hopper",
                    "use_furnace", "use_blast_furnace", "use_shulker_box", "use_trapdoor", "use_button",
                    "use_anvil", "use_farm", "use_beacon", "use_minecart", "use_boat"
                ).forEach { key -> data[key] = "true" }
                dao.insertShare(data)
            }
        }
    }

    fun getShareDataStatic(skyblockId: String, playerName: String, data: String, type: String?): Any? {
        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                val result = dao.getShare(skyblockId, playerName) ?: return null
                val column = when (type) {
                    "isUseJoin" -> "can_join"
                    "isUseBreak" -> "can_break"
                    "isUsePlace" -> "can_place"
                    "isUseDoor" -> "use_door"
                    "isUseChest" -> "use_chest"
                    "isUseBarrel" -> "use_barrel"
                    "isUseHopper" -> "use_hopper"
                    "isUseFurnace" -> "use_furnace"
                    "isUseBlastFurnace" -> "use_blast_furnace"
                    "isUseShulkerBox" -> "use_shulker_box"
                    "isUseTrapdoor" -> "use_trapdoor"
                    "isUseButton" -> "use_button"
                    "isUseAnvil" -> "use_anvil"
                    "isUseFarm" -> "use_farm"
                    "isUseBeacon" -> "use_beacon"
                    "isUseMinecart" -> "use_minecart"
                    "isUseBoat" -> "use_boat"
                    else -> null
                }
                column?.let { result[it] }?.let {
                    when (it.lowercase()) {
                        "true", "1" -> true
                        "false", "0" -> false
                        else -> it
                    }
                }
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                val result = dao.getShare(skyblockId, playerName) ?: return null
                val column = when (type) {
                    "isUseJoin" -> "can_join"
                    "isUseBreak" -> "can_break"
                    "isUsePlace" -> "can_place"
                    "isUseDoor" -> "use_door"
                    "isUseChest" -> "use_chest"
                    "isUseBarrel" -> "use_barrel"
                    "isUseHopper" -> "use_hopper"
                    "isUseFurnace" -> "use_furnace"
                    "isUseBlastFurnace" -> "use_blast_furnace"
                    "isUseShulkerBox" -> "use_shulker_box"
                    "isUseTrapdoor" -> "use_trapdoor"
                    "isUseButton" -> "use_button"
                    "isUseAnvil" -> "use_anvil"
                    "isUseFarm" -> "use_farm"
                    "isUseBeacon" -> "use_beacon"
                    "isUseMinecart" -> "use_minecart"
                    "isUseBoat" -> "use_boat"
                    else -> null
                }
                column?.let { result[it] }?.let {
                    when (it.lowercase()) {
                        "true", "1" -> true
                        "false", "0" -> false
                        else -> it
                    }
                }
            }

            else -> null
        }
    }

    fun setShareDataStatic(skyblockId: String, playerName: String, data: String, value: Any?, type: String?) {
        when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                val column = when (type) {
                    "setUseJoin" -> "can_join"
                    "setUseBreak" -> "can_break"
                    "setUsePlace" -> "can_place"
                    "setUseDoor" -> "use_door"
                    "setUseChest" -> "use_chest"
                    "setUseBarrel" -> "use_barrel"
                    "setUseHopper" -> "use_hopper"
                    "setUseFurnace" -> "use_furnace"
                    "setUseBlastFurnace" -> "use_blast_furnace"
                    "setUseShulkerBox" -> "use_shulker_box"
                    "setUseTrapdoor" -> "use_trapdoor"
                    "setUseButton" -> "use_button"
                    "setUseAnvil" -> "use_anvil"
                    "setUseFarm" -> "use_farm"
                    "setUseBeacon" -> "use_beacon"
                    "setUseMinecart" -> "use_minecart"
                    "setUseBoat" -> "use_boat"
                    else -> null
                }
                column?.let {
                    dao.updateShare(skyblockId, playerName, mapOf(it to value.toString()))
                }
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                val column = when (type) {
                    "setUseJoin" -> "can_join"
                    "setUseBreak" -> "can_break"
                    "setUsePlace" -> "can_place"
                    "setUseDoor" -> "use_door"
                    "setUseChest" -> "use_chest"
                    "setUseBarrel" -> "use_barrel"
                    "setUseHopper" -> "use_hopper"
                    "setUseFurnace" -> "use_furnace"
                    "setUseBlastFurnace" -> "use_blast_furnace"
                    "setUseShulkerBox" -> "use_shulker_box"
                    "setUseTrapdoor" -> "use_trapdoor"
                    "setUseButton" -> "use_button"
                    "setUseAnvil" -> "use_anvil"
                    "setUseFarm" -> "use_farm"
                    "setUseBeacon" -> "use_beacon"
                    "setUseMinecart" -> "use_minecart"
                    "setUseBoat" -> "use_boat"
                    else -> null
                }
                column?.let {
                    dao.updateShare(skyblockId, playerName, mapOf(it to value.toString()))
                }
            }
        }
    }

    fun getShareListStatic(skyblockId: String): List<String> {
        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                dao.getShareList(skyblockId)
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                dao.getShareList(skyblockId)
            }

            else -> emptyList()
        }
    }

    fun deleteShareStatic(skyblockId: String, playerName: String) {
        when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                dao.deleteShare(skyblockId, playerName)
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                dao.deleteShare(skyblockId, playerName)
            }
        }
    }

    fun getShareDataListStatic(skyblockId: String): List<String> {
        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                dao.getShareList(skyblockId)
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                dao.getShareList(skyblockId)
            }

            else -> emptyList()
        }
    }

    fun getSharePermissionsMapStatic(skyblockId: String): Map<String, Map<String, Boolean>> {
        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = ShareDAO()
                val shareList = dao.getShareList(skyblockId)
                val result = mutableMapOf<String, Map<String, Boolean>>()

                for (playerName in shareList) {
                    val rawMap = dao.getShare(skyblockId, playerName) ?: continue
                    val permissionMap = mutableMapOf<String, Boolean>()

                    for ((key, value) in rawMap) {
                        if (key != "skyblock_id" && key != "player_name") {
                            permissionMap[key] = value.lowercase() == "true"
                        }
                    }

                    result[playerName] = permissionMap
                }

                result
            }

            "mysql" -> {
                val dao = MySQLShareDAO()
                val shareList = dao.getShareList(skyblockId)
                val result = mutableMapOf<String, Map<String, Boolean>>()

                for (playerName in shareList) {
                    val rawMap = dao.getShare(skyblockId, playerName) ?: continue
                    val permissionMap = mutableMapOf<String, Boolean>()

                    for ((key, value) in rawMap) {
                        if (key != "skyblock_id" && key != "player_name") {
                            permissionMap[key] = value.lowercase() == "true"
                        }
                    }

                    result[playerName] = permissionMap
                }

                result
            }

            else -> emptyMap()
        }
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
        fun getUserData(data: String, player: Player?, type: String?) =
            DatabaseManager().getUserDataStatic(data, player, type)

        @JvmStatic
        fun setUserData(data: String, player: Player, value: Any?, type: String?) =
            DatabaseManager().setUserDataStatic(data, player, value, type)

        @JvmStatic
        fun DeleteSkyBlock(skyblockId: String) =
            DatabaseManager().DeleteSkyBlockStatic(skyblockId)

        @JvmStatic
        fun insertShare(skyblockId: String, playerName: String) =
            DatabaseManager().insertShareStatic(skyblockId, playerName)

        @JvmStatic
        fun getShareData(skyblockId: String, playerName: String, data: String, type: String?) =
            DatabaseManager().getShareDataStatic(skyblockId, playerName, data, type)

        @JvmStatic
        fun setShareData(skyblockId: String, playerName: String, data: String, value: Any?, type: String?) =
            DatabaseManager().setShareDataStatic(skyblockId, playerName, data, value, type)

        @JvmStatic
        fun getShareList(skyblockId: String): List<String> =
            DatabaseManager().getShareListStatic(skyblockId)

        @JvmStatic
        fun deleteShare(skyblockId: String, playerName: String) =
            DatabaseManager().deleteShareStatic(skyblockId, playerName)

        @JvmStatic
        fun getShareDataList(skyblockId: String): List<String> =
            DatabaseManager().getShareDataListStatic(skyblockId)

        @JvmStatic
        fun getSharePermissionsMap(skyblockId: String): Map<String, Map<String, Boolean>> {
            return DatabaseManager().getSharePermissionsMapStatic(skyblockId)
        }
    }
}