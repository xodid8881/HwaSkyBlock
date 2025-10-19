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

    fun getSkyBlockDataStatic(skyblockId: String, type: String?): Any? {
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

    fun setSkyBlockDataStatic(skyblockId: String, value: Any?, type: String?) {
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
                    "player_page" to 1,
                    "player_event" to ""
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
                    "player_page" to 1,
                    "player_event" to ""
                )
                dao.insertUser(userData)
            }
        }
    }

    fun getUserDataStatic(data: String, player: Player?, type: String?): Any? {
        val uuid = player?.uniqueId.toString()
        if (type == null) return null

        val typeToColumn = mapOf(
            "getPlayerPossessionCount" to "player_possession_count",
            "getPlayerPage" to "player_page",
            "getPlayerPos" to "player_pos",
            "getPlayerSetting" to "player_setting",
            "getPlayerEvent" to "player_event",
            "getPlayerPossession" to "player_setting",
            "getPlayerSharer" to "player_setting"
        )

        val column = typeToColumn[type] ?: return null

        fun parseSetting(raw: Any?): Map<String, Any> {
            return when (raw) {
                is Map<*, *> -> raw.entries.associate { it.key.toString() to it.value as Any }
                is String -> {
                    try {
                        val parser = org.json.simple.parser.JSONParser()
                        val obj = parser.parse(raw) as JSONObject
                        obj.entries.associate { (it as Map.Entry<*, *>).key.toString() to it.value as Any }
                    } catch (e: Exception) {
                        emptyMap()
                    }
                }

                else -> emptyMap()
            }
        }

        fun extractTrueMap(container: Any?): Map<String, Boolean> {
            val map = when (container) {
                is Map<*, *> -> container
                is JSONObject -> container
                else -> return emptyMap()
            }
            return map.mapNotNull { (k, v) ->
                val key = k?.toString()
                val value = (v as? Boolean) == true
                if (key != null && value) key to true else null
            }.toMap()
        }

        return when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = UserDAO()
                val result = dao.getUser(uuid) ?: return null

                if (column == "player_setting") {
                    val setting = parseSetting(result["player_setting"])
                    when (type) {
                        "getPlayerPossession" -> extractTrueMap(setting["possession"])
                        "getPlayerSharer" -> extractTrueMap(setting["sharer"])
                        "getPlayerSetting" -> setting
                        else -> setting[data] // 필요 시 개별 키 접근
                    }
                } else {
                    result[column]
                }
            }

            "mysql" -> {
                val dao = MySQLUserDAO()
                val result = dao.getUser(uuid) ?: return null

                if (column == "player_setting") {
                    val setting = parseSetting(result["player_setting"])
                    when (type) {
                        "getPlayerPossession" -> extractTrueMap(setting["possession"])
                        "getPlayerSharer" -> extractTrueMap(setting["sharer"])
                        "getPlayerSetting" -> setting
                        else -> setting[data]
                    }
                } else {
                    result[column]
                }
            }

            else -> null
        }
    }


    fun setUserDataStatic(data: String, player: Player, value: Any?, type: String?) {
        val uuid = player.uniqueId.toString()
        val stringValue = value?.toString() ?: return

        val typeToColumn = mapOf(
            "setPlayerPossessionCount" to "player_possession_count",
            "setPlayerPage" to "player_page",
            "setPlayerPos" to "player_pos",
            "setPlayerSetting" to "player_setting",
            "setPlayerEvent" to "player_event",
            "setPlayerPossession" to "player_setting",
            "setPlayerSharer" to "player_setting"
        )

        val column = typeToColumn[type] ?: return
        val jsonParser = org.json.simple.parser.JSONParser()

        when (Config.get("database.type")) {
            "sqlite" -> {
                val dao = UserDAO()

                if (column == "player_setting" &&
                    (data.contains(".skyblock.possession.") || data.contains(".skyblock.sharer."))
                ) {
                    val userData = dao.getUser(uuid)?.toMutableMap() ?: return

                    val rawSetting = userData["player_setting"]
                    val settingMap: MutableMap<String, Any> = when (rawSetting) {
                        is Map<*, *> -> rawSetting.entries.associate { it.key.toString() to it.value as Any }
                            .toMutableMap()

                        is String -> {
                            try {
                                val obj = jsonParser.parse(rawSetting) as JSONObject
                                obj.entries.associate { (it as Map.Entry<*, *>).key.toString() to it.value as Any }
                                    .toMutableMap()
                            } catch (e: Exception) {
                                mutableMapOf()
                            }
                        }

                        else -> mutableMapOf()
                    }

                    val newSetting = settingMap.toMutableMap()
                    val islandId = data.substringAfterLast(".")

                    val keyType = if (data.contains(".skyblock.possession.")) "possession" else "sharer"

                    val oldSubMap = (newSetting[keyType] as? Map<*, *>)?.toMutableMap()
                        ?.mapKeys { it.key.toString() }
                        ?.mapValues { it.value.toString().toBoolean() }
                        ?.toMutableMap() ?: mutableMapOf()

                    if (stringValue.equals("false", ignoreCase = true)) {
                        oldSubMap.remove(islandId)
                    } else {
                        oldSubMap[islandId] = true
                    }

                    newSetting[keyType] = oldSubMap
                    dao.updateUser(uuid, mapOf(column to JSONObject(newSetting).toJSONString()))
                } else {
                    dao.updateUser(uuid, mapOf(column to stringValue))
                }
            }

            "mysql" -> {
                val dao = MySQLUserDAO()

                if (column == "player_setting" &&
                    (data.contains(".skyblock.possession.") || data.contains(".skyblock.sharer."))
                ) {
                    val userData = dao.getUser(uuid)?.toMutableMap() ?: return

                    val rawSetting = userData["player_setting"]
                    val settingMap: MutableMap<String, Any> = when (rawSetting) {
                        is Map<*, *> -> rawSetting.entries.associate { it.key.toString() to it.value as Any }
                            .toMutableMap()

                        is String -> {
                            try {
                                val obj = jsonParser.parse(rawSetting) as JSONObject
                                obj.entries.associate { (it as Map.Entry<*, *>).key.toString() to it.value as Any }
                                    .toMutableMap()
                            } catch (e: Exception) {
                                mutableMapOf()
                            }
                        }

                        else -> mutableMapOf()
                    }

                    val newSetting = settingMap.toMutableMap()
                    val islandId = data.substringAfterLast(".")
                    val keyType = if (data.contains(".skyblock.possession.")) "possession" else "sharer"

                    val oldSubMap = (newSetting[keyType] as? Map<*, *>)?.toMutableMap()
                        ?.mapKeys { it.key.toString() }
                        ?.mapValues { it.value.toString().toBoolean() }
                        ?.toMutableMap() ?: mutableMapOf()

                    if (stringValue.equals("false", ignoreCase = true)) {
                        oldSubMap.remove(islandId)
                    } else {
                        oldSubMap[islandId] = true
                    }

                    newSetting[keyType] = oldSubMap
                    dao.updateUser(uuid, mapOf(column to JSONObject(newSetting).toJSONString()))
                } else {
                    dao.updateUser(uuid, mapOf(column to stringValue))
                }
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

    fun getShareDataStatic(skyblockId: String, playerName: String, type: String?): Any? {
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

    fun setShareDataStatic(skyblockId: String, playerName: String, value: Any?, type: String?) {
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
        fun getSkyBlockData(skyblockId: String, type: String?) =
            DatabaseManager().getSkyBlockDataStatic(skyblockId, type)

        @JvmStatic
        fun setSkyBlockData(skyblockId: String, value: Any?, type: String?) =
            DatabaseManager().setSkyBlockDataStatic(skyblockId, value, type)

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
        fun getShareData(skyblockId: String, playerName: String, type: String?) =
            DatabaseManager().getShareDataStatic(skyblockId, playerName, type)

        @JvmStatic
        fun setShareData(skyblockId: String, playerName: String, value: Any?, type: String?) =
            DatabaseManager().setShareDataStatic(skyblockId, playerName, value, type)

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