package org.hwabaeg.hwaskyblock.events.player

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Boat
import org.bukkit.entity.Entity
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import org.hwabaeg.hwaskyblock.api.customblock.CustomBlockManager
import org.hwabaeg.hwaskyblock.database.DatabaseManager
import org.hwabaeg.hwaskyblock.database.config.ConfigManager
import java.util.*

class UseEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var Prefix: String = ChatColor.translateAlternateColorCodes(
        '&',
        Objects.requireNonNull<String?>(Config.getString("hwaskyblock-system.prefix"))
    )

    fun sendNoPermissionActionBar(player: Player) {
        val message = ChatColor.translateAlternateColorCodes(
            '&',
            Prefix + Objects.requireNonNull<String?>(MessageConfig.getString("message-event.no_permission"))
        )
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
    }

    private fun extractSkyblockId(worldName: String): String? {
        if (!worldName.startsWith("HwaSkyBlock.")) return null
        val raw = worldName.substringAfter("HwaSkyBlock.")
        if (raw.isBlank()) return null
        return raw.substringBefore("_")
    }

    private fun canInteractIslandEntity(player: Player, islandId: String): Boolean {
        val leader = DatabaseManager.getSkyBlockData(islandId, "getSkyBlockLeader") as? String ?: return true
        if (leader.equals(player.name, ignoreCase = true)) return true
        return DatabaseManager.getShareDataList(islandId).any { it.equals(player.name, ignoreCase = true) }
    }

    @EventHandler
    fun onInteractEntity(event: PlayerInteractEntityEvent) {
        if (event.rightClicked is Player) return

        val islandId = extractSkyblockId(event.player.world.name) ?: return
        if (canInteractIslandEntity(event.player, islandId)) return

        sendNoPermissionActionBar(event.player)
        event.isCancelled = true
    }

    @EventHandler
    fun onInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked is Player) return

        val islandId = extractSkyblockId(event.player.world.name) ?: return
        if (canInteractIslandEntity(event.player, islandId)) return

        sendNoPermissionActionBar(event.player)
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDamageMinecart(event: VehicleDamageEvent) {
        val vehicle = event.vehicle
        if (vehicle !is Minecart) return
        if (event.attacker !is Player) return

        val damager = event.attacker as Player
        val name = damager.name
        val world = vehicle.world
        val worldName = world.worldFolder.name
        val number = worldName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (number[0] == "HwaSkyBlock") {
            val id = number[1]
            val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader") as? String
            if (leader != null && leader != name) {
                val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }
                val useBreakPermission = if (isSharer) {
                    DatabaseManager.getShareData(id, name, "isUseBreak") as? Boolean ?: false
                } else {
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockBreak") as? Boolean ?: false
                }

                if (!useBreakPermission) {
                    sendNoPermissionActionBar(damager)
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onPlayerDamageBoat(event: VehicleDamageEvent) {
        val vehicle = event.vehicle
        if (vehicle !is Boat) return

        val damager = event.attacker
        if (damager !is Player) return

        val name = damager.name
        val world = vehicle.world
        val worldName = world.worldFolder.name
        val parts = worldName.split('.')

        if (parts.size < 2 || parts[0] != "HwaSkyBlock") return

        val id = parts[1]

        if (DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader") == null) return

        val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }

        val hasBreakPermission = if (isSharer)
            DatabaseManager.getShareData(id, name, "isUseBreak") as? Boolean ?: false
        else
            DatabaseManager.getSkyBlockData(id, "isSkyBlockBreak") as? Boolean ?: false

        if (!hasBreakPermission) {
            sendNoPermissionActionBar(damager)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityCollision(event: VehicleEntityCollisionEvent) {
        val vehicle: Entity = event.vehicle
        val entity = event.entity

        if (entity is Player && (vehicle is Minecart || vehicle is Boat)) {
            val name = entity.name
            val world = entity.world
            val worldName = world.worldFolder.name
            val parts = worldName.split('.')
            if (parts[0] != "HwaSkyBlock" || parts.size < 2) return

            val id = parts[1]

            val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader")
            if (leader == null || leader == name) return

            val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }

            if (vehicle is Minecart) {
                val hasPermission = if (isSharer)
                    DatabaseManager.getShareData(id, name, "isUseMinecart") as? Boolean ?: false
                else
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockMinecart") as? Boolean ?: false

                if (!hasPermission) {
                    sendNoPermissionActionBar(entity)
                    event.isCancelled = true
                }
            }
            if (vehicle is Boat) {
                val hasPermission = if (isSharer)
                    DatabaseManager.getShareData(id, name, "isUseBoat") as? Boolean ?: false
                else
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockBoat") as? Boolean ?: false

                if (!hasPermission) {
                    sendNoPermissionActionBar(entity)
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onVehicleEnter(event: VehicleEnterEvent) {
        val vehicle = event.vehicle
        val entered = event.entered

        if (entered is Player && (vehicle is Minecart || vehicle is Boat)) {
            val name = entered.name
            val world = entered.world
            val worldName = world.name
            val parts = worldName.split('.')
            if (parts.size < 2 || parts[0] != "HwaSkyBlock") return

            val id = parts[1]
            val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader")
            if (leader == null || leader == name) return

            val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }

            val hasPermission = when (vehicle) {
                is Minecart -> if (isSharer)
                    DatabaseManager.getShareData(id, name, "isUseMinecart") as? Boolean ?: false
                else
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockMinecart") as? Boolean ?: false

                is Boat -> if (isSharer)
                    DatabaseManager.getShareData(id, name, "isUseBoat") as? Boolean ?: false
                else
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockBoat") as? Boolean ?: false

                else -> true
            }

            if (!hasPermission) {
                sendNoPermissionActionBar(entered)
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val name = player.name

        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return

        val world = block.world
        val worldName = world.worldFolder.name
        val parts = worldName.split('.')
        if (parts.size < 2 || parts[0] != "HwaSkyBlock") return

        val id = parts[1]
        val leader = DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader") as? String ?: return

        if (leader == name) return

        val handler = CustomBlockManager.getHandler()
        val isCustomBlock = handler?.isCustomBlock(block) == true

        if (isCustomBlock) {
            handleCustomBlockInteraction(player, block, id, name, event)
        } else {
            handleStandardBlockInteraction(player, block, id, name, event)
        }
    }

    fun handleCustomBlockInteraction(
        player: Player,
        block: Block,
        id: String,
        name: String,
        event: PlayerInteractEvent,
    ) {
        val customCropDisplayName = CustomBlockManager.getHandler()?.getCustomBlockDisplayName(block) ?: return
        val configSection = Config.getConfigurationSection("Custom-Crop-Interact") ?: return

        for (type in configSection.getKeys(false)) {
            if (customCropDisplayName.contains(type, ignoreCase = true)) {
                val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }

                val hasFarmPermission = if (isSharer)
                    DatabaseManager.getShareData(id, name, "isUseFarm") as? Boolean ?: false
                else
                    DatabaseManager.getSkyBlockData(id, "isSkyBlockFarm") as? Boolean ?: false

                if (!hasFarmPermission) {
                    sendNoPermissionActionBar(player)
                    event.isCancelled = true
                    return
                }
            }
        }
    }

    fun handleStandardBlockInteraction(
        player: Player,
        block: Block,
        id: String,
        name: String,
        event: PlayerInteractEvent,
    ) {
        val blockType = block.type.name
        val blockPermissions = mapOf(
            "CHEST" to ("isSkyBlockChest" to "isUseChest"),
            "TRAPDOOR" to ("isSkyBlockTrapdoor" to "isUseTrapdoor"),
            "DOOR" to ("isSkyBlockDoor" to "isUseDoor"),
            "BARREL" to ("isSkyBlockBarrel" to "isUseBarrel"),
            "HOPPER" to ("isSkyBlockHopper" to "isUseHopper"),
            "FURNACE" to ("isSkyBlockFurnace" to "isUseFurnace"),
            "BLAST" to ("isSkyBlockBlastFurnace" to "isUseBlastFurnace"),
            "SHULKER" to ("isSkyBlockShulkerBox" to "isUseShulkerBox"),
            "BUTTON" to ("isSkyBlockButton" to "isUseButton"),
            "ANVIL" to ("isSkyBlockAnvil" to "isUseAnvil"),
            "BEACON" to ("isSkyBlockBeacon" to "isUseBeacon")
        )

        for ((blockName, permissionKeys) in blockPermissions) {
            if (blockType.contains(blockName)) {
                checkPermissionAndCancel(
                    player,
                    id,
                    name,
                    permissionKeys.first,
                    permissionKeys.second,
                    event
                )
                return
            }
        }
    }

    fun checkPermissionAndCancel(
        player: Player,
        id: String,
        name: String,
        globalKey: String,
        sharerKey: String,
        event: PlayerInteractEvent,
    ) {
        val isSharer = DatabaseManager.getShareDataList(id).any { it.equals(name, ignoreCase = true) }
        if (!isSharer) {
            if (!(DatabaseManager.getSkyBlockData(id, globalKey) as? Boolean
                    ?: false)
            ) {
                sendNoPermissionActionBar(player)
                event.isCancelled = true
            }
        } else {
            if (!(DatabaseManager.getShareData(id, name, sharerKey) as? Boolean ?: false)
            ) {
                sendNoPermissionActionBar(player)
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val attacker = when (val damager = event.damager) {
            is Player -> damager
            is Projectile -> damager.shooter as? Player
            else -> null
        } ?: return

        if (event.entity !is Player) {
            val islandId = extractSkyblockId(event.entity.world.name) ?: return
            if (!canInteractIslandEntity(attacker, islandId)) {
                sendNoPermissionActionBar(attacker)
                event.isCancelled = true
            }
            return
        }

        val entity = event.entity
        if (entity !is Player) return

        val world = entity.world
        val id = extractSkyblockId(world.name) ?: return
        if (DatabaseManager.getSkyBlockData(id, "getSkyBlockLeader") != null) {
            if (!(DatabaseManager.getSkyBlockData(id, "isSkyBlockPvp") as? Boolean ?: false)) {
                event.isCancelled = true
            }
        }
    }

}
