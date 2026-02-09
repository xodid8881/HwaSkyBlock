package org.hwabaeg.hwaskyblock.addon.cattle

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.data.type.Gate
import org.bukkit.command.CommandSender
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.hwabaeg.hwaskyblock.addon.cattle.command.CattleCommand
import org.hwabaeg.hwaskyblock.addon.cattle.command.CommandRegistrar
import org.hwabaeg.hwaskyblock.addon.cattle.config.AddonConfig
import org.hwabaeg.hwaskyblock.addon.cattle.model.BreedingItemDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.CattleDefinition
import org.hwabaeg.hwaskyblock.addon.cattle.model.CustomItemType
import org.hwabaeg.hwaskyblock.addon.cattle.model.KeyRing
import org.hwabaeg.hwaskyblock.addon.cattle.model.ShopHolder
import org.hwabaeg.hwaskyblock.addon.cattle.storage.CattleState
import org.hwabaeg.hwaskyblock.addon.cattle.storage.CattleStateStore
import org.hwabaeg.hwaskyblock.addon.cattle.util.IslandResolver
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import org.hwabaeg.hwaskyblock.world.SkyblockIslandCreateEvent
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ceil

class SkyblockCattleAddon : HwaSkyBlockAddon, Listener {
    private lateinit var plugin: JavaPlugin
    private lateinit var config: AddonConfig
    private lateinit var key: KeyRing
    private lateinit var store: CattleStateStore
    private var nameTask: BukkitTask? = null
    private var economy: Economy? = null

    override fun onEnable(main: JavaPlugin) {
        plugin = main
        key = KeyRing(plugin)
        config = AddonConfig.load(plugin)
        store = CattleStateStore()
        store.init()
        economy = findEconomy()

        Bukkit.getPluginManager().registerEvents(this, plugin)
        CommandRegistrar(Bukkit.getServer()).register(CattleCommand(this))

        nameTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable { refreshManagedNames() }, 40L, 40L)
        plugin.logger.info("[SkyblockCattleAddon] 활성화 완료")
    }

    override fun onDisable() {
        flushAllManagedToStore()
        nameTask?.cancel()
        plugin.logger.info("[SkyblockCattleAddon] 비활성화 완료")
    }

    fun reload() {
        config = AddonConfig.load(plugin)
        plugin.logger.info("[SkyblockCattleAddon] 설정 리로드 완료")
    }

    fun openShop(player: Player) {
        val rows = 3
        val inv = Bukkit.createInventory(ShopHolder(), rows * 9, color("&8섬 가축 상점"))
        var slot = 10
        config.cattles.values.forEach { cattle ->
            val egg = createSpawnEgg(cattle.id) ?: return@forEach
            inv.setItem(slot, egg)
            slot += 2
            if (slot >= rows * 9) return@forEach
        }
        player.openInventory(inv)
    }

    fun giveFeed(player: Player, feedId: String, amount: Int): Boolean {
        val item = createFeedItem(feedId) ?: return false
        item.amount = amount.coerceAtLeast(1)
        player.inventory.addItem(item).values.forEach { player.world.dropItemNaturally(player.location, it) }
        return true
    }

    fun giveBreed(player: Player, breedId: String, amount: Int): Boolean {
        val item = createBreedItem(breedId) ?: return false
        item.amount = amount.coerceAtLeast(1)
        player.inventory.addItem(item).values.forEach { player.world.dropItemNaturally(player.location, it) }
        return true
    }

    fun help(sender: CommandSender) {
        sender.sendMessage(color("&e/cattle 상점"))
        sender.sendMessage(color("&e/cattle 리로드"))
        sender.sendMessage(color("&e/cattle 사료지급 <플레이어> <사료ID> [수량]"))
        sender.sendMessage(color("&e/cattle 번식지급 <플레이어> <토큰ID> [수량]"))
    }

    @EventHandler(ignoreCancelled = true)
    fun onIslandCreate(event: SkyblockIslandCreateEvent) {
        val world = Bukkit.getWorld("HwaSkyBlock.${event.id}") ?: return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            buildDefaultPens(world)
        }, 40L)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onUseSpawnEgg(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return
        if (!isCustomType(item, CustomItemType.SPAWN)) return

        event.isCancelled = true
        val cattleId = getCustomItemId(item) ?: return
        val cattle = config.cattles[cattleId] ?: return

        if (!isIsland(player.world)) {
            player.sendMessage(color("&c섬 내부에서만 가축을 소환할 수 있습니다."))
            return
        }

        val clicked = event.clickedBlock ?: run {
            player.sendMessage(color("&c블럭을 우클릭해서 소환하세요."))
            return
        }

        val spawnLoc = clicked.location.add(0.5, 1.0, 0.5)
        val spawned = player.world.spawnEntity(spawnLoc, cattle.type)
        if (spawned !is Animals) {
            spawned.remove()
            return
        }

        markManaged(spawned, cattle.id)
        player.world.spawnParticle(Particle.HAPPY_VILLAGER, spawned.location.add(0.0, 0.8, 0.0), 8, 0.25, 0.25, 0.25)
        decrementHandItem(player)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onFeedOrBreed(event: PlayerInteractEntityEvent) {
        val player = event.player
        val target = event.rightClicked as? Animals ?: return
        if (!isManaged(target)) return

        val inHand = player.inventory.itemInMainHand
        val type = getCustomItemType(inHand)
        if (type == null) {
            if (isVanillaBreedingFood(target, inHand.type)) {
                event.isCancelled = true
                player.sendMessage(color("&c바닐라 먹이는 사용할 수 없습니다. 특수 사료를 사용하세요."))
            }
            return
        }
        val cattle = resolveCattle(target) ?: return
        if (!isIsland(target.world)) {
            player.sendMessage(color("&c섬 내부에서만 이 아이템을 사용할 수 있습니다."))
            return
        }

        event.isCancelled = true
        when (type) {
            CustomItemType.FEED -> handleFeed(player, target, cattle)
            CustomItemType.BREED -> handleBreed(player, target, cattle, inHand)
            CustomItemType.SPAWN -> Unit
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onVanillaBreed(event: EntityBreedEvent) {
        val mother = event.mother as? Animals
        val father = event.father as? Animals
        if (mother != null && isManaged(mother)) event.isCancelled = true
        if (father != null && isManaged(father)) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onManagedDeath(event: EntityDeathEvent) {
        val entity = event.entity as? Animals ?: return
        if (!isManaged(entity) || !isIsland(entity.world)) return

        val cattle = resolveCattle(entity) ?: return
        store.delete(entity.uniqueId.toString())
        event.drops.clear()
        for (drop in cattle.drops.values) {
            val amount = if (drop.min >= drop.max) drop.min else ThreadLocalRandom.current().nextInt(drop.min, drop.max + 1)
            if (amount <= 0) continue
            val base = createCustomItem(drop.item)
            if (base != null) {
                base.amount = amount
                event.drops.add(base)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onShopClick(event: InventoryClickEvent) {
        event.view.topInventory.holder as? ShopHolder ?: return
        if (event.clickedInventory == null || event.clickedInventory?.type == InventoryType.PLAYER) return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        val item = event.currentItem ?: return
        if (!isCustomType(item, CustomItemType.SPAWN)) return
        val cattleId = getCustomItemId(item) ?: return
        val cattle = config.cattles[cattleId] ?: return

        if (!isIsland(player.world)) {
            player.sendMessage(color("&c섬 내부에서만 구매할 수 있습니다."))
            return
        }

        if (cattle.shopPrice > 0.0) {
            val eco = economy
            if (eco == null) {
                player.sendMessage(color("&c경제 플러그인(Vault)을 찾을 수 없습니다."))
                return
            }
            if (eco.getBalance(player) < cattle.shopPrice) {
                player.sendMessage(color("&c잔액이 부족합니다. 필요: &f${cattle.shopPrice}"))
                return
            }
            eco.withdrawPlayer(player, cattle.shopPrice)
        }

        val buy = createSpawnEgg(cattle.id) ?: return
        val left = player.inventory.addItem(buy)
        if (left.isNotEmpty()) {
            player.world.dropItemNaturally(player.location, buy)
        }
        player.sendMessage(color("&a${cattle.displayName} 스폰알을 구매했습니다."))
    }

    private fun findEconomy(): Economy? {
        val rsp: RegisteredServiceProvider<Economy> =
            Bukkit.getServicesManager().getRegistration(Economy::class.java) ?: return null
        return rsp.provider
    }

    private fun refreshManagedNames() {
        for (world in Bukkit.getWorlds()) {
            for (entity in world.entities) {
                if (entity !is Animals) continue
                if (!isManaged(entity)) {
                    restoreFromStore(entity)
                }
                if (!isManaged(entity)) continue
                val cattle = resolveCattle(entity) ?: continue
                entity.customName = renderStatus(entity, cattle)
                entity.isCustomNameVisible = true
                persistEntityState(entity, cattle.id)
            }
        }
    }

    private fun handleFeed(player: Player, animal: Animals, cattle: CattleDefinition) {
        val inHand = player.inventory.itemInMainHand
        val expected = cattle.feed.item
        val itemId = getCustomItemId(inHand)
        if (itemId != expected) {
            player.sendMessage(color("&c이 가축이 먹는 사료가 아닙니다."))
            return
        }

        val today = todayEpochDay()
        val lastDay = animal.persistentDataContainer.getOrDefault(key.feedDay, PersistentDataType.LONG, -1L)
        if (lastDay != today) {
            animal.persistentDataContainer.set(key.feedDay, PersistentDataType.LONG, today)
            animal.persistentDataContainer.set(key.feedCount, PersistentDataType.INTEGER, 0)
        }

        val fed = animal.persistentDataContainer.getOrDefault(key.feedCount, PersistentDataType.INTEGER, 0)
        if (fed >= cattle.feed.dailyAmount) {
            player.sendMessage(color("&e이미 오늘 먹이 요구량을 충족했습니다."))
            return
        }

        animal.persistentDataContainer.set(key.feedCount, PersistentDataType.INTEGER, fed + 1)
        decrementHandItem(player)
        animal.world.spawnParticle(Particle.HAPPY_VILLAGER, animal.location.add(0.0, 1.0, 0.0), 8, 0.25, 0.25, 0.25)
        animal.customName = renderStatus(animal, cattle)
        animal.isCustomNameVisible = true
        persistEntityState(animal, cattle.id)
    }

    private fun handleBreed(player: Player, animal: Animals, cattle: CattleDefinition, inHand: ItemStack) {
        val expected = cattle.breeding.item
        val itemId = getCustomItemId(inHand)
        if (itemId != expected) {
            player.sendMessage(color("&c이 가축 번식 토큰이 아닙니다."))
            return
        }
        if (remainingFeed(animal, cattle) > 0) {
            player.sendMessage(color("&c먼저 사료를 모두 지급해야 번식할 수 있습니다."))
            return
        }
        if (remainingCooldownHours(animal) > 0L) {
            player.sendMessage(color("&c이 가축은 아직 번식 쿨타임입니다."))
            return
        }

        val until = epochSecond() + cattle.breeding.durationSeconds
        animal.persistentDataContainer.set(key.breedMarkedUntil, PersistentDataType.LONG, until)
        animal.world.spawnParticle(Particle.HEART, animal.location.add(0.0, 1.0, 0.0), 8, 0.35, 0.35, 0.35)

        val partner = findPartner(animal, cattle.id)
        if (partner == null) {
            consumeBreedToken(player, inHand, onSuccess = false)
            player.sendMessage(color("&e번식 대상 대기 상태로 등록되었습니다. (10분 유지)"))
            persistEntityState(animal, cattle.id)
            return
        }

        val baby = animal.world.spawnEntity(mid(animal.location, partner.location), cattle.type)
        if (baby is Animals) {
            baby.setAdult()
            baby.age = -24000
            markManaged(baby, cattle.id)
        }

        val nextReadyAt = epochSecond() + (cattle.breeding.cooldownHours * 3600L)
        animal.persistentDataContainer.set(key.breedReadyAt, PersistentDataType.LONG, nextReadyAt)
        partner.persistentDataContainer.set(key.breedReadyAt, PersistentDataType.LONG, nextReadyAt)
        animal.persistentDataContainer.remove(key.breedMarkedUntil)
        partner.persistentDataContainer.remove(key.breedMarkedUntil)

        animal.world.spawnParticle(Particle.HEART, animal.location.add(0.0, 1.0, 0.0), 12, 0.45, 0.45, 0.45)
        partner.world.spawnParticle(Particle.HEART, partner.location.add(0.0, 1.0, 0.0), 12, 0.45, 0.45, 0.45)
        consumeBreedToken(player, inHand, onSuccess = true)
        player.sendMessage(color("&a번식에 성공했습니다."))
        persistEntityState(animal, cattle.id)
        persistEntityState(partner, cattle.id)
    }

    private fun consumeBreedToken(player: Player, inHand: ItemStack, onSuccess: Boolean) {
        if (player.gameMode.name.contains("CREATIVE")) return
        if (onSuccess) {
            inHand.amount = inHand.amount - 1
            return
        }

        val maxUses = getCustomMaxUses(inHand)
        if (maxUses <= 1) {
            inHand.amount = inHand.amount - 1
            return
        }

        val nowUses = getCustomUses(inHand)
        val next = nowUses - 1
        if (next <= 0) {
            inHand.amount = inHand.amount - 1
            return
        }

        val meta = inHand.itemMeta ?: return
        meta.persistentDataContainer.set(key.itemUses, PersistentDataType.INTEGER, next)
        meta.lore = mutableListOf(color("&7남은 사용 횟수: &f$next"))
        inHand.itemMeta = meta
    }

    private fun findPartner(self: Animals, cattleId: String): Animals? {
        val near = self.getNearbyEntities(8.0, 4.0, 8.0)
        for (entity in near) {
            val animal = entity as? Animals ?: continue
            if (animal.uniqueId == self.uniqueId) continue
            if (!isManaged(animal)) continue
            if (getCattleId(animal) != cattleId) continue
            val cattle = config.cattles[cattleId] ?: continue
            if (remainingFeed(animal, cattle) > 0) continue
            if (remainingCooldownHours(animal) > 0L) continue
            val markedUntil = animal.persistentDataContainer.getOrDefault(key.breedMarkedUntil, PersistentDataType.LONG, 0L)
            if (markedUntil < epochSecond()) continue
            return animal
        }
        return null
    }

    private fun mid(a: Location, b: Location): Location {
        val world = a.world ?: b.world
        return Location(world, (a.x + b.x) / 2.0, (a.y + b.y) / 2.0, (a.z + b.z) / 2.0)
    }

    private fun decrementHandItem(player: Player) {
        if (player.gameMode.name.contains("CREATIVE")) return
        val item = player.inventory.itemInMainHand
        item.amount = item.amount - 1
    }

    private fun isIsland(world: World): Boolean = IslandResolver.resolve(world) != null

    private fun markManaged(entity: Animals, cattleId: String) {
        entity.persistentDataContainer.set(key.managed, PersistentDataType.BYTE, 1)
        entity.persistentDataContainer.set(key.cattleId, PersistentDataType.STRING, cattleId)
        entity.persistentDataContainer.set(key.feedDay, PersistentDataType.LONG, todayEpochDay())
        entity.persistentDataContainer.set(key.feedCount, PersistentDataType.INTEGER, 0)
        entity.persistentDataContainer.set(key.breedReadyAt, PersistentDataType.LONG, 0L)

        val cattle = config.cattles[cattleId] ?: return
        entity.customName = renderStatus(entity, cattle)
        entity.isCustomNameVisible = true
        persistEntityState(entity, cattle.id)
    }

    private fun resolveCattle(entity: Entity): CattleDefinition? {
        val id = getCattleId(entity) ?: return null
        return config.cattles[id]
    }

    private fun getCattleId(entity: Entity): String? =
        entity.persistentDataContainer.get(key.cattleId, PersistentDataType.STRING)

    private fun isManaged(entity: Entity): Boolean =
        entity.persistentDataContainer.getOrDefault(key.managed, PersistentDataType.BYTE, 0).toInt() == 1

    private fun remainingFeed(entity: Animals, cattle: CattleDefinition): Int {
        val today = todayEpochDay()
        val day = entity.persistentDataContainer.getOrDefault(key.feedDay, PersistentDataType.LONG, -1L)
        val fed = if (day == today) entity.persistentDataContainer.getOrDefault(key.feedCount, PersistentDataType.INTEGER, 0) else 0
        return (cattle.feed.dailyAmount - fed).coerceAtLeast(0)
    }

    private fun remainingCooldownHours(entity: Animals): Long {
        val now = epochSecond()
        val readyAt = entity.persistentDataContainer.getOrDefault(key.breedReadyAt, PersistentDataType.LONG, 0L)
        if (readyAt <= now) return 0L
        val seconds = readyAt - now
        return ceil(seconds / 3600.0).toLong()
    }

    private fun renderStatus(entity: Animals, cattle: CattleDefinition): String {
        val remain = remainingFeed(entity, cattle)
        val hours = remainingCooldownHours(entity)
        if (remain > 0) {
            val current = cattle.feed.dailyAmount - remain
            val feedLine = cattle.hologram.feedFormat
                .replace("{current}", current.toString())
                .replace("{max}", cattle.feed.dailyAmount.toString())
            val breedLine = cattle.hologram.breedFormat.replace("{hours}", hours.toString())
            return color("$feedLine\n$breedLine")
        }
        return color(cattle.hologram.breedFormat.replace("{hours}", hours.toString()))
    }

    private fun createCustomItem(id: String): ItemStack? {
        val def = config.customItems[id] ?: return null
        val material = Material.matchMaterial(def.material) ?: return null
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(def.displayName))
        if (def.lore.isNotEmpty()) meta.lore = def.lore.map { color(it) }
        stack.itemMeta = meta
        return stack
    }

    private fun createSpawnEgg(cattleId: String): ItemStack? {
        val cattle = config.cattles[cattleId] ?: return null
        val material = Material.matchMaterial("${cattle.type.name}_SPAWN_EGG") ?: Material.COW_SPAWN_EGG
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(cattle.displayName))
        meta.persistentDataContainer.set(key.itemType, PersistentDataType.STRING, CustomItemType.SPAWN.name)
        meta.persistentDataContainer.set(key.itemId, PersistentDataType.STRING, cattle.id)
        val lore = mutableListOf(color("&7섬 전용 가축 스폰알"))
        if (cattle.shopPrice > 0.0) lore.add(color("&e가격: &f${cattle.shopPrice}"))
        meta.lore = lore
        stack.itemMeta = meta
        return stack
    }

    private fun createFeedItem(feedId: String): ItemStack? {
        val def = config.feeds[feedId] ?: return null
        val material = Material.matchMaterial(def.material) ?: return null
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(def.displayName))
        meta.lore = def.lore.map { color(it) }
        meta.persistentDataContainer.set(key.itemType, PersistentDataType.STRING, CustomItemType.FEED.name)
        meta.persistentDataContainer.set(key.itemId, PersistentDataType.STRING, def.id)
        stack.itemMeta = meta
        return stack
    }

    private fun createBreedItem(breedId: String): ItemStack? {
        val def: BreedingItemDefinition = config.breedingItems[breedId] ?: return null
        val material = Material.matchMaterial(def.material) ?: return null
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(def.displayName))
        val lore = def.lore.map { color(it) }.toMutableList()
        lore.add(color("&7남은 사용 횟수: &f${def.maxUses}"))
        meta.lore = lore
        meta.persistentDataContainer.set(key.itemType, PersistentDataType.STRING, CustomItemType.BREED.name)
        meta.persistentDataContainer.set(key.itemId, PersistentDataType.STRING, def.id)
        meta.persistentDataContainer.set(key.itemMaxUses, PersistentDataType.INTEGER, def.maxUses)
        meta.persistentDataContainer.set(key.itemUses, PersistentDataType.INTEGER, def.maxUses)
        stack.itemMeta = meta
        return stack
    }

    private fun getCustomItemType(item: ItemStack): CustomItemType? {
        val raw = item.itemMeta?.persistentDataContainer?.get(key.itemType, PersistentDataType.STRING) ?: return null
        return runCatching { CustomItemType.valueOf(raw) }.getOrNull()
    }

    private fun isCustomType(item: ItemStack, type: CustomItemType): Boolean = getCustomItemType(item) == type

    private fun getCustomItemId(item: ItemStack): String? =
        item.itemMeta?.persistentDataContainer?.get(key.itemId, PersistentDataType.STRING)

    private fun getCustomUses(item: ItemStack): Int =
        item.itemMeta?.persistentDataContainer?.get(key.itemUses, PersistentDataType.INTEGER) ?: 1

    private fun getCustomMaxUses(item: ItemStack): Int =
        item.itemMeta?.persistentDataContainer?.get(key.itemMaxUses, PersistentDataType.INTEGER) ?: 1

    private fun isVanillaBreedingFood(animal: Animals, material: Material): Boolean {
        return when (animal.type.name) {
            "COW", "SHEEP" -> material == Material.WHEAT
            "PIG" -> material == Material.CARROT || material == Material.POTATO || material == Material.BEETROOT
            "CHICKEN" -> material == Material.WHEAT_SEEDS ||
                material == Material.BEETROOT_SEEDS ||
                material == Material.MELON_SEEDS ||
                material == Material.PUMPKIN_SEEDS ||
                material == Material.TORCHFLOWER_SEEDS
            else -> false
        }
    }

    private fun persistEntityState(entity: Animals, cattleId: String) {
        val islandId = IslandResolver.resolve(entity.world) ?: return
        val state = CattleState(
            entityUuid = entity.uniqueId.toString(),
            islandId = islandId,
            cattleId = cattleId,
            feedDay = entity.persistentDataContainer.getOrDefault(key.feedDay, PersistentDataType.LONG, todayEpochDay()),
            feedCount = entity.persistentDataContainer.getOrDefault(key.feedCount, PersistentDataType.INTEGER, 0),
            breedReadyAt = entity.persistentDataContainer.getOrDefault(key.breedReadyAt, PersistentDataType.LONG, 0L),
            breedMarkedUntil = entity.persistentDataContainer.getOrDefault(key.breedMarkedUntil, PersistentDataType.LONG, 0L)
        )
        store.upsert(state)
    }

    private fun restoreFromStore(entity: Animals) {
        val state = store.get(entity.uniqueId.toString()) ?: return
        val islandId = IslandResolver.resolve(entity.world) ?: return
        if (state.islandId != islandId) return
        if (config.cattles[state.cattleId] == null) return

        entity.persistentDataContainer.set(key.managed, PersistentDataType.BYTE, 1)
        entity.persistentDataContainer.set(key.cattleId, PersistentDataType.STRING, state.cattleId)
        entity.persistentDataContainer.set(key.feedDay, PersistentDataType.LONG, state.feedDay)
        entity.persistentDataContainer.set(key.feedCount, PersistentDataType.INTEGER, state.feedCount)
        entity.persistentDataContainer.set(key.breedReadyAt, PersistentDataType.LONG, state.breedReadyAt)
        entity.persistentDataContainer.set(key.breedMarkedUntil, PersistentDataType.LONG, state.breedMarkedUntil)
    }

    private fun flushAllManagedToStore() {
        for (world in Bukkit.getWorlds()) {
            for (entity in world.entities) {
                val animal = entity as? Animals ?: continue
                if (!isManaged(animal)) continue
                val cattleId = getCattleId(animal) ?: continue
                persistEntityState(animal, cattleId)
            }
        }
    }

    private fun buildDefaultPens(world: World) {
        val origin = world.spawnLocation.clone()
        val y = origin.blockY
        val pens = listOf(
            Pair(-14, -14),
            Pair(14, -14),
            Pair(-14, 14),
            Pair(14, 14)
        )
        pens.forEachIndexed { index, offset ->
            createPen(world, origin.blockX + offset.first, y, origin.blockZ + offset.second, index)
        }
    }

    private fun createPen(world: World, centerX: Int, y: Int, centerZ: Int, index: Int) {
        val half = 3
        for (x in centerX - half..centerX + half) {
            for (z in centerZ - half..centerZ + half) {
                val floor = world.getBlockAt(x, y, z)
                if (floor.type == Material.AIR) floor.type = Material.GRASS_BLOCK
                val fenceBlock = world.getBlockAt(x, y + 1, z)
                val border = x == centerX - half || x == centerX + half || z == centerZ - half || z == centerZ + half
                if (!border) {
                    if (fenceBlock.type == Material.OAK_FENCE || fenceBlock.type == Material.OAK_FENCE_GATE) {
                        fenceBlock.type = Material.AIR
                    }
                    continue
                }
                fenceBlock.type = Material.OAK_FENCE
            }
        }

        val gatePos = when (index % 4) {
            0 -> Pair(centerX, centerZ + half)
            1 -> Pair(centerX - half, centerZ)
            2 -> Pair(centerX + half, centerZ)
            else -> Pair(centerX, centerZ - half)
        }

        val gateBlock = world.getBlockAt(gatePos.first, y + 1, gatePos.second)
        gateBlock.type = Material.OAK_FENCE_GATE
        val gateData = gateBlock.blockData as? Gate
        if (gateData != null) {
            gateData.isOpen = false
            gateBlock.blockData = gateData
        }
    }

    private fun color(text: String): String = text.replace("&", "§")
    private fun epochSecond(): Long = System.currentTimeMillis() / 1000L
    private fun todayEpochDay(): Long = System.currentTimeMillis() / 86_400_000L
}
