package org.hwabaeg.hwaskyblock.database.mysql.utils

class hwaskyblock_skyblock {

    private var skyblockId: String? = null
    private var skyblockName: String? = null
    private var skyblockLeader: String? = null
    private var skyblockJoin: Boolean = false
    private var skyblockBreak: Boolean = false
    private var skyblockPlace: Boolean = false
    private var skyblockDoor: Boolean = false
    private var skyblockChest: Boolean = false
    private var skyblockBarrel: Boolean = false
    private var skyblockHopper: Boolean = false
    private var skyblockFurnace: Boolean = false
    private var skyblockBlastFurnace: Boolean = false
    private var skyblockShulkerBox: Boolean = false
    private var skyblockTrapdoor: Boolean = false
    private var skyblockButton: Boolean = false
    private var skyblockAnvil: Boolean = false
    private var skyblockFarm: Boolean = false
    private var skyblockBeacon: Boolean = false
    private var skyblockMinecart: Boolean = false
    private var skyblockBoat: Boolean = false
    private var skyblockPvp: Boolean = false
    private var skyblockWelcomeMessage: String? = null
    private var skyblockHome: Int = 0
    private var skyblockSize: Int = 0
    private var skyblockMonsterSpawn: Boolean = false
    private var skyblockAnimalSpawn: Boolean = false
    private var skyblockWeather: Boolean = false
    private var skyblockTime: Boolean = false
    private var skyblockWaterPhysics: Boolean = false
    private var skyblockLavaPhysics: Boolean = false

    fun getSkyBlockName(): String? {
        return skyblockName
    }

    constructor(data: Map<String, String>) {
        this.skyblockId = data["skyblock_id"]
        this.skyblockName = data["skyblock_name"]
        this.skyblockLeader = data["skyblock_leader"]
        this.skyblockJoin = data["skyblock_join"]?.toBooleanStrictOrNull() ?: false
        this.skyblockBreak = data["skyblock_break"]?.toBooleanStrictOrNull() ?: false
        this.skyblockPlace = data["skyblock_place"]?.toBooleanStrictOrNull() ?: false
        this.skyblockDoor = data["skyblock_door"]?.toBooleanStrictOrNull() ?: false
        this.skyblockChest = data["skyblock_chest"]?.toBooleanStrictOrNull() ?: false
        this.skyblockBarrel = data["skyblock_barrel"]?.toBooleanStrictOrNull() ?: false
        this.skyblockHopper = data["skyblock_hopper"]?.toBooleanStrictOrNull() ?: false
        this.skyblockFurnace = data["skyblock_furnace"]?.toBooleanStrictOrNull() ?: false
        this.skyblockBlastFurnace = data["skyblock_blast_furnace"]?.toBooleanStrictOrNull() ?: false
        this.skyblockShulkerBox = data["skyblock_shulker_box"]?.toBooleanStrictOrNull() ?: false
        this.skyblockTrapdoor = data["skyblock_trapdoor"]?.toBooleanStrictOrNull() ?: false
        this.skyblockButton = data["skyblock_button"]?.toBooleanStrictOrNull() ?: false
        this.skyblockAnvil = data["skyblock_anvil"]?.toBooleanStrictOrNull() ?: false
        this.skyblockFarm = data["skyblock_farm"]?.toBooleanStrictOrNull() ?: false
        this.skyblockBeacon = data["skyblock_beacon"]?.toBooleanStrictOrNull() ?: false
        this.skyblockMinecart = data["skyblock_minecart"]?.toBooleanStrictOrNull() ?: false
        this.skyblockBoat = data["skyblock_boat"]?.toBooleanStrictOrNull() ?: false
        this.skyblockPvp = data["skyblock_pvp"]?.toBooleanStrictOrNull() ?: false
        this.skyblockWelcomeMessage = data["skyblock_welcome_message"]
        this.skyblockHome = data["skyblock_home"]?.toIntOrNull() ?: 0
        this.skyblockSize = data["skyblock_size"]?.toIntOrNull() ?: 0
        this.skyblockMonsterSpawn = data["skyblock_monster_spawn"]?.toBooleanStrictOrNull() ?: false
        this.skyblockAnimalSpawn = data["skyblock_animal_spawn"]?.toBooleanStrictOrNull() ?: false
        this.skyblockWeather = data["skyblock_weather"]?.toBooleanStrictOrNull() ?: false
        this.skyblockTime = data["skyblock_time"]?.toBooleanStrictOrNull() ?: false
        this.skyblockWaterPhysics = data["skyblock_water_physics"]?.toBooleanStrictOrNull() ?: false
        this.skyblockLavaPhysics = data["skyblock_lava_physics"]?.toBooleanStrictOrNull() ?: false
    }

}
