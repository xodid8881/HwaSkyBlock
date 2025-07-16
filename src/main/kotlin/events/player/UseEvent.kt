package org.hwabeag.hwaskyblock.events.player

import dev.lone.itemsadder.api.CustomBlock
import dev.lone.itemsadder.api.ItemsAdder
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Boat
import org.bukkit.entity.Entity
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import org.hwabeag.hwaskyblock.config.ConfigManager
import java.util.*

class UseEvent : Listener {
    var Config: FileConfiguration = ConfigManager.getConfig("setting")!!
    var MessageConfig: FileConfiguration = ConfigManager.getConfig("message")!!
    var SkyBlockConfig: FileConfiguration = ConfigManager.getConfig("skyblock")!!
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
            val leader = SkyBlockConfig.getString("$id.leader")
            if (leader != null && leader != name) {
                val permissionKey = "$id.sharer.$name"
                val useBreakPermission = if (SkyBlockConfig.getString(permissionKey) == null) {
                    SkyBlockConfig.getBoolean("$id.break")
                } else {
                    SkyBlockConfig.getBoolean("$permissionKey.use.break")
                }

                if (useBreakPermission != true) {
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

        if (SkyBlockConfig.getString("$id.leader") == null) return

        // 리더가 아니라면
        if (SkyBlockConfig.getString("$id.leader") != name) {
            val isSharer = SkyBlockConfig.getString("$id.sharer.$name") != null

            val hasBreakPermission = if (isSharer)
                SkyBlockConfig.getBoolean("$id.sharer.$name.use.break")
            else
                SkyBlockConfig.getBoolean("$id.use.break")

            if (!hasBreakPermission) {
                sendNoPermissionActionBar(damager)
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onEntityCollision(event: VehicleEntityCollisionEvent) {
        val vehicle: Entity = event.getVehicle()
        val entity = event.entity
        if (entity is Player && vehicle is Minecart) {
            val name = entity.name
            val world = entity.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (SkyBlockConfig.getString("$id.leader") != null) {
                    if (SkyBlockConfig.getString("$id.leader") != name) {
                        if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                            if (!SkyBlockConfig.getBoolean("$id.use.minecart")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                                return
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.minecart")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                                return
                            }
                        }
                    }
                }
            }
        }
        if (entity is Player && vehicle is Boat) {
            val name = entity.name
            val world = entity.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (SkyBlockConfig.getString("$id.leader") != null) {
                    if (SkyBlockConfig.getString("$id.leader") != name) {
                        if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                            if (!SkyBlockConfig.getBoolean("$id.use.boat")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.boat")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onEntityMount(event: EntityMountEvent) {
        val entity = event.getEntity()
        val vehicle = event.mount
        if (entity is Player && vehicle is Minecart) {
            val name = entity.name
            val world = entity.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (SkyBlockConfig.getString("$id.leader") != null) {
                    if (SkyBlockConfig.getString("$id.leader") != name) {
                        if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                            if (!SkyBlockConfig.getBoolean("$id.use.minecart")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                                return
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.minecart")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                                return
                            }
                        }
                    }
                }
            }
        }
        if (entity is Player && vehicle is Boat) {
            val name = entity.name
            val world = entity.world
            val world_name = world.worldFolder.getName()
            val number: Array<String?> = world_name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                if (SkyBlockConfig.getString("$id.leader") != null) {
                    if (SkyBlockConfig.getString("$id.leader") != name) {
                        if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                            if (!SkyBlockConfig.getBoolean("$id.use.boat")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                            }
                        } else {
                            if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.boat")) {
                                sendNoPermissionActionBar(entity)
                                event.isCancelled = true
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val name = player.name
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val block = event.clickedBlock ?: return
            val world = block.world
            val worldName = world.worldFolder.name
            val number = worldName.split('.')

            if (number[0] == "HwaSkyBlock") {
                val id = number[1]
                val leader = SkyBlockConfig.getString("$id.leader")
                if (leader != null && leader != name) {
                    val customBlock = CustomBlock.byAlreadyPlaced(block)
                    if (customBlock != null) {
                        handleCustomBlockInteraction(player, block, id, name, event)
                    } else {
                        handleStandardBlockInteraction(player, block, id, name, event)
                    }
                }
            }
        }
    }

    fun handleCustomBlockInteraction(
        player: Player,
        block: Block,
        id: String,
        name: String,
        event: PlayerInteractEvent
    ) {
        val customCrop = ItemsAdder.getCustomBlock(block)
        val customCropDisplayName = customCrop.itemMeta!!.displayName
        for (type in Config.getConfigurationSection("Custom-Crop-Interact")?.getKeys(false) ?: emptySet()) {
            if (customCropDisplayName.contains(type)) {
                if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
                    if (!SkyBlockConfig.getBoolean("$id.use.farm")) {
                        sendNoPermissionActionBar(player)
                        event.isCancelled = true
                        return
                    }
                } else {
                    if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.farm")) {
                        sendNoPermissionActionBar(player)
                        event.isCancelled = true
                        return
                    }
                }
            }
        }
    }

    fun handleStandardBlockInteraction(
        player: Player,
        block: Block,
        id: String,
        name: String,
        event: PlayerInteractEvent
    ) {
        val blockType = block.type.name
        val blockPermissions = mapOf(
            "CHEST" to "chest",
            "TRAPDOOR" to "trapdoor",
            "DOOR" to "door",
            "BARREL" to "barrel",
            "HOPPER" to "hopper",
            "FURNACE" to "furnace",
            "BLAST" to "furnace",
            "SHULKER" to "shulker",
            "BUTTON" to "button",
            "ANVIL" to "anvil",
            "BEACON" to "beacon"
        )

        for ((blockName, permission) in blockPermissions) {
            if (blockType.contains(blockName)) {
                checkPermissionAndCancel(player, block, id, name, permission, event)
                return
            }
        }
    }

    fun checkPermissionAndCancel(
        player: Player,
        block: Block,
        id: String,
        name: String,
        permission: String,
        event: PlayerInteractEvent
    ) {
        if (SkyBlockConfig.getString("$id.sharer.$name") == null) {
            if (!SkyBlockConfig.getBoolean("$id.use.$permission")) {
                sendNoPermissionActionBar(player)
                event.isCancelled = true
            }
        } else {
            if (!SkyBlockConfig.getBoolean("$id.sharer.$name.use.$permission")) {
                sendNoPermissionActionBar(player)
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity !is Player) return

        val world = entity.world
        val worldName = world.worldFolder.name
        val parts = worldName.split(".")

        if (parts.size >= 2 && parts[0] == "HwaSkyBlock") {
            val id = parts[1]
            if (SkyBlockConfig.getString("$id.leader") != null) {
                if (!SkyBlockConfig.getBoolean("$id.pvp")) {
                    event.isCancelled = true
                }
            }
        }
    }

}