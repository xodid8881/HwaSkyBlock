package org.hwabaeg.hwaskyblock.addon.cattle

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.data.type.Gate
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.hwabaeg.hwaskyblock.addon.cattle.command.CattleAdminCommand
import org.hwabaeg.hwaskyblock.addon.cattle.command.CattleUserCommand
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
import org.hwabaeg.hwaskyblock.platform.PlatformFactory
import org.hwabaeg.hwaskyblock.world.IslandWorlds
import org.hwabaeg.hwaskyblock.world.SkyblockIslandCreateEvent
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicLong
import java.util.jar.JarFile
import kotlin.math.ceil

class SkyblockCattleAddon : HwaSkyBlockAddon, Listener {
    private lateinit var plugin: JavaPlugin
    private lateinit var config: AddonConfig
    private lateinit var messageConfig: FileConfiguration
    private lateinit var key: KeyRing
    private lateinit var store: CattleStateStore
    private var nameTask: BukkitTask? = null
    private var flushTask: BukkitTask? = null
    private var startupTask: BukkitTask? = null
    private var economy: Economy? = null
    private var started = false
    private var commandRegistrar: CommandRegistrar? = null
    private var userCommand: CattleUserCommand? = null
    private var adminCommand: CattleAdminCommand? = null
    private val materialResolver = PlatformFactory.createMaterialResolver(Bukkit.getServer())
    private val pendingStateOps = LinkedHashMap<String, CattleState?>()
    private val stateOpLock = Any()
    private val flushRunLock = Any()
    private val refreshCycle = AtomicLong(0L)
    private val defaultPrefixRaw = "&6&l[가축 상점]&r "
    private val defaultShopTitleRaw = "&8가축 상점"

    override fun onEnable(main: JavaPlugin) {
        plugin = main
        key = KeyRing(plugin)
        config = AddonConfig.load(plugin)
        messageConfig = loadMessageConfig()
        store = CattleStateStore()
        startWhenDatabaseReady()
        economy = findEconomy()

        plugin.logger.info("[SkyblockCattleAddon] 활성화 완료")
    }

    override fun onDisable() {
        startupTask?.cancel()
        flushTask?.cancel()
        if (started) {
            flushAllManagedToStore()
            flushPendingStateOps()
        }
        HandlerList.unregisterAll(this)
        userCommand?.let { cmd -> commandRegistrar?.unregister(cmd) }
        adminCommand?.let { cmd -> commandRegistrar?.unregister(cmd) }
        userCommand = null
        adminCommand = null
        commandRegistrar = null
        nameTask?.cancel()
        plugin.logger.info("[SkyblockCattleAddon] 비활성화 완료")
    }

    fun reload() {
        config = AddonConfig.load(plugin)
        messageConfig = loadMessageConfig()
        plugin.logger.info("[SkyblockCattleAddon] 설정 리로드 완료")
    }

    private fun startWhenDatabaseReady() {
        if (started) return
        if (tryInitStore()) {
            finishStartup()
            return
        }

        plugin.logger.warning("[SkyblockCattleAddon] 메인 데이터베이스 준비 전입니다. 애드온 시작을 재시도합니다...")
        startupTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (started) {
                startupTask?.cancel()
                startupTask = null
                return@Runnable
            }
            if (!tryInitStore()) return@Runnable

            startupTask?.cancel()
            startupTask = null
            finishStartup()
        }, 20L, 20L)
    }

    private fun tryInitStore(): Boolean = runCatching {
        store.init()
    }.onFailure {
        plugin.logger.fine("[SkyblockCattleAddon] DB 초기화를 대기 중: ${it.message}")
    }.isSuccess

    private fun finishStartup() {
        if (started) return
        started = true

        Bukkit.getPluginManager().registerEvents(this, plugin)
        commandRegistrar = CommandRegistrar(Bukkit.getServer())
        userCommand = CattleUserCommand(this)
        adminCommand = CattleAdminCommand(this)
        commandRegistrar?.register(userCommand!!)
        commandRegistrar?.register(adminCommand!!)
        nameTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable { refreshManagedNames() }, 100L, 100L)
        flushTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable { flushPendingStateOps() }, 200L, 200L)
        plugin.logger.info("[SkyblockCattleAddon] 애드온 시작 완료")
    }

    fun openShop(player: Player) {
        val total = config.cattles.size
        val rows = (total.coerceAtLeast(1).let { ((it - 1) / 7) + 3 }).coerceAtMost(6)
        val inv = Bukkit.createInventory(ShopHolder(), rows * 9, color(msg("gui.shop.title", defaultShopTitleRaw)))
        decorateShopFrame(inv)

        val availableSlots = collectShopSlots(inv)
        var slotIndex = 0
        config.cattles.values.forEach { cattle ->
            if (slotIndex >= availableSlots.size) return@forEach
            val egg = createSpawnEgg(cattle.id) ?: return@forEach
            inv.setItem(availableSlots[slotIndex], egg)
            slotIndex++
        }
        setShopInfoItem(inv, total, availableSlots.size)
        if (total > availableSlots.size) {
            sendWarn(
                player,
                msg("warn.shop.limit", "상점 표시 한도({limit}개)를 초과해 일부 가축은 표시되지 않습니다.")
                    .replace("{limit}", availableSlots.size.toString())
            )
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

    fun helpUser(sender: CommandSender) {
        val lines = msgList(
            "help.user-lines",
            listOf(
                "&7사용 가능한 명령어",
                " &f- &e/가축 상점",
                " &8- &7관리자 명령어: /가축관리"
            )
        )
        if (lines.isEmpty()) return
        sendInfo(sender, lines.first())
        lines.drop(1).forEach { sender.sendMessage(color(it)) }
    }

    fun helpAdmin(sender: CommandSender) {
        val lines = msgList(
            "help.admin-lines",
            listOf(
                "&7관리자 명령어",
                " &f- &e/가축관리 리로드",
                " &f- &e/가축관리 급식지급 <플레이어> <사료ID> [수량]",
                " &f- &e/가축관리 번식지급 <플레이어> <토큰ID> [수량]"
            )
        )
        if (lines.isEmpty()) return
        sendInfo(sender, lines.first())
        lines.drop(1).forEach { sender.sendMessage(color(it)) }
    }

    fun help(sender: CommandSender) = helpUser(sender)

    @EventHandler(ignoreCancelled = true)
    fun onIslandCreate(event: SkyblockIslandCreateEvent) {
        val world = Bukkit.getWorld(IslandWorlds.worldName(event.id.toString())) ?: return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            buildDefaultPens(world)
        }, 40L)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onUseSpawnEgg(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val item = event.item ?: return
        if (!isCustomType(item, CustomItemType.SPAWN)) return

        event.isCancelled = true
        val cattleId = getCustomItemId(item) ?: return
        val cattle = config.cattles[cattleId] ?: return

        if (!isIsland(player.world)) {
            sendError(player, msg("error.spawn.island-only", "섬 내부에서만 가축을 소환할 수 있습니다."))
            return
        }

        val clicked = event.clickedBlock ?: run {
            sendError(player, msg("error.spawn.click-block", "블럭을 우클릭해서 소환하세요."))
            return
        }

        val spawnLoc = clicked.location.add(0.5, 1.0, 0.5)
        val spawned = player.world.spawnEntity(spawnLoc, cattle.type)
        if (spawned !is Animals) {
            spawned.remove()
            return
        }

        markManaged(spawned, cattle.id)
        spawnCompatParticle(player.world, "HAPPY_VILLAGER", spawned.location.add(0.0, 0.8, 0.0), 8, 0.25, 0.25, 0.25)
        decrementHandItem(player)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onFeedOrBreed(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val player = event.player
        val target = event.rightClicked as? Animals ?: return
        if (!isManaged(target)) return

        val inHand = player.inventory.itemInMainHand
        val type = getCustomItemType(inHand)
        if (type == null) {
            if (isVanillaBreedingFood(target, inHand.type)) {
                event.isCancelled = true
                sendError(player, msg("error.feed.no-vanilla", "바닐라 먹이는 사용할 수 없습니다. 특수 사료를 사용하세요."))
            }
            return
        }
        val cattle = resolveCattle(target) ?: return
        if (!isIsland(target.world)) {
            sendError(player, msg("error.use.island-only", "섬 내부에서만 이 아이템을 사용할 수 있습니다."))
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
        if (!isManaged(entity)) return
        queueDeleteState(entity.uniqueId.toString())
        if (!isIsland(entity.world)) return

        val cattle = resolveCattle(entity) ?: return
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

    @EventHandler(priority = EventPriority.HIGH)
    fun onShopClick(event: InventoryClickEvent) {
        val topHolder = org.hwabaeg.hwaskyblock.compat.inventoryViewTopHolder(event.view)
        val isShopByHolder = topHolder is ShopHolder
        val isShopByTitle = org.hwabaeg.hwaskyblock.compat.inventoryViewTitle(event.view) == color(msg("gui.shop.title", defaultShopTitleRaw))
        if (!isShopByHolder && !isShopByTitle) return
        event.isCancelled = true
        if (event.clickedInventory == null || event.clickedInventory?.type == InventoryType.PLAYER) return

        val player = event.whoClicked as? Player ?: return
        val item = event.currentItem ?: return
        if (!isCustomType(item, CustomItemType.SPAWN)) return
        val cattleId = getCustomItemId(item) ?: return
        val cattle = config.cattles[cattleId] ?: return

        if (!isIsland(player.world)) {
            sendError(player, msg("error.shop.island-only", "섬 내부에서만 구매할 수 있습니다."))
            return
        }

        if (cattle.shopPrice > 0.0) {
            val eco = economy
            if (eco == null) {
                sendError(player, msg("error.shop.no-economy", "경제 플러그인(Vault)을 찾을 수 없습니다."))
                return
            }
            if (eco.getBalance(player) < cattle.shopPrice) {
                sendError(
                    player,
                    msg("error.shop.not-enough-money", "잔액이 부족합니다. 필요 금액: {price}")
                        .replace("{price}", cattle.shopPrice.toString())
                )
                return
            }
            val response: EconomyResponse = eco.withdrawPlayer(player, cattle.shopPrice)
            if (!response.transactionSuccess()) {
                sendError(player, msg("error.shop.payment-failed", "결제 처리에 실패했습니다. 잠시 후 다시 시도해주세요."))
                return
            }
        }

        val buy = createSpawnEgg(cattle.id) ?: return
        val left = player.inventory.addItem(buy)
        if (left.isNotEmpty()) {
            left.values.forEach { leftover ->
                player.world.dropItemNaturally(player.location, leftover)
            }
        }
        sendSuccess(
            player,
            msg("success.shop.purchased", "{name} 스폰알을 구매했습니다.")
                .replace("{name}", cattle.displayName)
        )
    }

    private fun findEconomy(): Economy? {
        val rsp: RegisteredServiceProvider<Economy> =
            Bukkit.getServicesManager().getRegistration(Economy::class.java) ?: return null
        return rsp.provider
    }

    private fun refreshManagedNames() {
        val cycle = refreshCycle.incrementAndGet()
        val shouldRestore = cycle % 5L == 0L
        for (world in Bukkit.getWorlds()) {
            if (IslandResolver.maybeIslandId(world) == null) continue
            for (entity in world.entities) {
                if (entity !is Animals) continue
                if (!isManaged(entity) && shouldRestore) {
                    restoreFromStore(entity)
                }
                if (!isManaged(entity)) continue
                val cattle = resolveCattle(entity) ?: continue
                entity.customName = renderStatus(entity, cattle)
                entity.isCustomNameVisible = true
            }
        }
    }

    private fun handleFeed(player: Player, animal: Animals, cattle: CattleDefinition) {
        val inHand = player.inventory.itemInMainHand
        val expected = cattle.feed.item
        val itemId = getCustomItemId(inHand)
        if (itemId != expected) {
            sendError(player, msg("error.feed.wrong-item", "이 가축이 먹는 사료가 아닙니다."))
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
            sendWarn(player, msg("warn.feed.already-full", "이미 오늘 먹이 요구량을 충족했습니다."))
            return
        }

        animal.persistentDataContainer.set(key.feedCount, PersistentDataType.INTEGER, fed + 1)
        decrementHandItem(player)
        spawnCompatParticle(animal.world, "HAPPY_VILLAGER", animal.location.add(0.0, 1.0, 0.0), 8, 0.25, 0.25, 0.25)
        animal.customName = renderStatus(animal, cattle)
        animal.isCustomNameVisible = true
        persistEntityState(animal, cattle.id)
    }

    private fun handleBreed(player: Player, animal: Animals, cattle: CattleDefinition, inHand: ItemStack) {
        val expected = cattle.breeding.item
        val itemId = getCustomItemId(inHand)
        if (itemId != expected) {
            sendError(player, msg("error.breed.wrong-item", "이 가축 번식 토큰이 아닙니다."))
            return
        }
        if (remainingFeed(animal, cattle) > 0) {
            sendError(player, msg("error.breed.need-feed", "먼저 사료를 모두 지급해야 번식할 수 있습니다."))
            return
        }
        if (remainingCooldownHours(animal) > 0L) {
            sendError(player, msg("error.breed.cooldown", "이 가축은 아직 번식 쿨타임입니다."))
            return
        }

        val breedTokenDuration = config.breedingItems[itemId]?.durationSeconds ?: cattle.breeding.durationSeconds
        val until = epochSecond() + breedTokenDuration
        animal.persistentDataContainer.set(key.breedMarkedUntil, PersistentDataType.LONG, until)
        animal.world.spawnParticle(Particle.HEART, animal.location.add(0.0, 1.0, 0.0), 8, 0.35, 0.35, 0.35)

        val partner = findPartner(animal, cattle.id)
        if (partner == null) {
            consumeBreedToken(player, inHand)
            val keepMinutes = ceil(breedTokenDuration / 60.0).toLong()
            sendWarn(
                player,
                msg("warn.breed.waiting", "번식 대상 대기 상태로 등록되었습니다. ({minutes}분 유지)")
                    .replace("{minutes}", keepMinutes.toString())
            )
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
        consumeBreedToken(player, inHand)
        sendSuccess(player, msg("success.breed.done", "번식에 성공했습니다."))
        persistEntityState(animal, cattle.id)
        persistEntityState(partner, cattle.id)
    }

    private fun consumeBreedToken(player: Player, inHand: ItemStack) {
        if (player.gameMode.name.contains("CREATIVE")) {
            sendWarn(player, msg("warn.breed.creative-no-consume", "크리에이티브 모드에서는 번식 토큰이 소모되지 않습니다."))
            return
        }

        val maxUses = getCustomMaxUses(inHand)
        if (maxUses <= 1) {
            consumeMainHandOne(player)
            player.updateInventory()
            return
        }

        val nowUses = getCustomUses(inHand)
        val next = nowUses - 1
        if (inHand.amount > 1) {
            if (next <= 0) {
                consumeMainHandOne(player)
                player.updateInventory()
                return
            }

            val remainStack = inHand.clone()
            remainStack.amount = inHand.amount - 1

            val usedToken = inHand.clone()
            usedToken.amount = 1
            applyBreedTokenUses(usedToken, next)

            player.inventory.setItemInMainHand(usedToken)
            player.inventory.addItem(remainStack).values.forEach { remain ->
                player.world.dropItemNaturally(player.location, remain)
            }
            sendInfo(
                player,
                msg("info.breed.token-uses", "번식 토큰 남은 사용 횟수: {uses}")
                    .replace("{uses}", next.toString())
            )
            player.updateInventory()
            return
        }
        if (next <= 0) {
            consumeMainHandOne(player)
            player.updateInventory()
            return
        }

        applyBreedTokenUses(inHand, next)
        player.inventory.setItemInMainHand(inHand)
        sendInfo(
            player,
            msg("info.breed.token-uses", "번식 토큰 남은 사용 횟수: {uses}")
                .replace("{uses}", next.toString())
        )
        player.updateInventory()
    }

    private fun applyBreedTokenUses(item: ItemStack, nextUses: Int) {
        val meta = item.itemMeta ?: return
        meta.persistentDataContainer.set(key.itemUses, PersistentDataType.INTEGER, nextUses)
        val previousLore = meta.lore.orEmpty().filterNot { it.contains("남은 사용 횟수:") }.toMutableList()
        previousLore.add(
            color(
                msg("gui.breed-item.uses", "&7남은 사용 횟수: &f{uses}")
                    .replace("{uses}", nextUses.toString())
            )
        )
        meta.lore = previousLore
        item.itemMeta = meta
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
        val nextAmount = item.amount - 1
        if (nextAmount <= 0) {
            player.inventory.setItemInMainHand(ItemStack(Material.AIR))
            return
        }
        item.amount = nextAmount
        player.inventory.setItemInMainHand(item)
    }

    private fun consumeMainHandOne(player: Player) {
        val item = player.inventory.itemInMainHand
        val nextAmount = item.amount - 1
        if (nextAmount <= 0) {
            player.inventory.setItemInMainHand(ItemStack(Material.AIR))
            return
        }
        item.amount = nextAmount
        player.inventory.setItemInMainHand(item)
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
        val current = cattle.feed.dailyAmount - remain

        if (remain > 0) {
            return color(
                msg("tag.line-feed", "&6[가축] &f{name} &8| &e급식 &f{current}/{max}")
                    .replace("{name}", cattle.displayName)
                    .replace("{current}", current.toString())
                    .replace("{max}", cattle.feed.dailyAmount.toString())
            )
        }

        return color(
            if (hours > 0L) {
                msg("tag.line-breed-cooldown", "&6[가축] &f{name} &8| &c번식 대기 &f{hours}시간")
                    .replace("{name}", cattle.displayName)
                    .replace("{hours}", hours.toString())
            } else {
                msg("tag.line-breed-ready", "&6[가축] &f{name} &8| &a번식 가능")
                    .replace("{name}", cattle.displayName)
            }
        )
    }

    private fun createCustomItem(id: String): ItemStack? {
        val def = config.customItems[id] ?: return null
        val material = materialResolver.fromKeyOrNull(def.material) ?: return null
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(def.displayName))
        if (def.lore.isNotEmpty()) meta.lore = def.lore.map { color(it) }
        stack.itemMeta = meta
        return stack
    }

    private fun createSpawnEgg(cattleId: String): ItemStack? {
        val cattle = config.cattles[cattleId] ?: return null
        val material = materialResolver.fromKeyOrNull("${cattle.type.name}_SPAWN_EGG") ?: Material.COW_SPAWN_EGG
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(cattle.displayName))
        meta.persistentDataContainer.set(key.itemType, PersistentDataType.STRING, CustomItemType.SPAWN.name)
        meta.persistentDataContainer.set(key.itemId, PersistentDataType.STRING, cattle.id)
        val lore = mutableListOf(color(msg("gui.shop.spawn-egg-lore", "&7섬 전용 가축 스폰알")))
        if (cattle.shopPrice > 0.0) {
            lore.add(
                color(
                    msg("gui.shop.item-price", "&e가격: &f{price}")
                        .replace("{price}", cattle.shopPrice.toString())
                )
            )
        }
        meta.lore = lore
        stack.itemMeta = meta
        return stack
    }

    private fun createFeedItem(feedId: String): ItemStack? {
        val def = config.feeds[feedId] ?: return null
        val material = materialResolver.fromKeyOrNull(def.material) ?: return null
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
        val material = materialResolver.fromKeyOrNull(def.material) ?: return null
        val stack = ItemStack(material)
        val meta = stack.itemMeta ?: return stack
        meta.setDisplayName(color(def.displayName))
        val lore = def.lore.map { color(it) }.toMutableList()
        lore.add(
            color(
                msg("gui.breed-item.uses", "&7남은 사용 횟수: &f{uses}")
                    .replace("{uses}", def.maxUses.toString())
            )
        )
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
        val torchflowerSeeds = materialResolver.fromKeyOrNull("TORCHFLOWER_SEEDS")
        return when (animal.type.name) {
            "COW", "SHEEP" -> material == Material.WHEAT
            "PIG" -> material == Material.CARROT || material == Material.POTATO || material == Material.BEETROOT
            "CHICKEN" -> material == Material.WHEAT_SEEDS ||
                material == Material.BEETROOT_SEEDS ||
                material == Material.MELON_SEEDS ||
                material == Material.PUMPKIN_SEEDS ||
                (torchflowerSeeds != null && material == torchflowerSeeds)
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
        queueUpsertState(state)
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

    private fun queueUpsertState(state: CattleState) {
        synchronized(stateOpLock) {
            pendingStateOps[state.entityUuid] = state
        }
    }

    private fun queueDeleteState(entityUuid: String) {
        synchronized(stateOpLock) {
            pendingStateOps[entityUuid] = null
        }
    }

    private fun flushPendingStateOps() {
        synchronized(flushRunLock) {
            val snapshot: Map<String, CattleState?> = synchronized(stateOpLock) {
                if (pendingStateOps.isEmpty()) return
                LinkedHashMap(pendingStateOps).also { pendingStateOps.clear() }
            }

            val deletes = ArrayList<String>()
            val upserts = ArrayList<CattleState>()
            for ((uuid, state) in snapshot) {
                if (state == null) deletes.add(uuid) else upserts.add(state)
            }

            runCatching {
                store.deleteAll(deletes)
                store.upsertAll(upserts)
            }.onFailure { ex ->
                plugin.logger.warning("[SkyblockCattleAddon] 가축 상태 배치 저장 실패: ${ex.message}")
                synchronized(stateOpLock) {
                    for ((uuid, state) in snapshot) {
                        pendingStateOps.putIfAbsent(uuid, state)
                    }
                }
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

    private fun color(text: String): String = org.bukkit.ChatColor.translateAlternateColorCodes('&', text)
    private fun sendInfo(sender: CommandSender, msg: String) = sender.sendMessage(color("${msg("prefix", defaultPrefixRaw)}&7$msg"))
    private fun sendWarn(sender: CommandSender, msg: String) = sender.sendMessage(color("${msg("prefix", defaultPrefixRaw)}&e$msg"))
    private fun sendError(sender: CommandSender, msg: String) = sender.sendMessage(color("${msg("prefix", defaultPrefixRaw)}&c$msg"))
    private fun sendSuccess(sender: CommandSender, msg: String) = sender.sendMessage(color("${msg("prefix", defaultPrefixRaw)}&a$msg"))

    fun uiInfo(sender: CommandSender, msg: String) = sendInfo(sender, msg)
    fun uiError(sender: CommandSender, msg: String) = sendError(sender, msg)
    fun uiSuccess(sender: CommandSender, msg: String) = sendSuccess(sender, msg)
    fun text(path: String, fallback: String): String = msg(path, fallback)

    private fun decorateShopFrame(inv: Inventory) {
        if (inv.size < 9) return
        val border = createPane(Material.GRAY_STAINED_GLASS_PANE)
        val corner = createPane(Material.ORANGE_STAINED_GLASS_PANE)

        for (slot in 0 until inv.size) {
            val row = slot / 9
            val col = slot % 9
            val isEdge = row == 0 || row == (inv.size / 9) - 1 || col == 0 || col == 8
            if (!isEdge) continue
            val isCorner = (row == 0 || row == (inv.size / 9) - 1) && (col == 0 || col == 8)
            inv.setItem(slot, if (isCorner) corner else border)
        }
    }

    private fun collectShopSlots(inv: Inventory): List<Int> {
        val rows = inv.size / 9
        val slots = ArrayList<Int>()
        for (row in 1 until rows - 1) {
            for (col in 1..7) {
                slots.add(row * 9 + col)
            }
        }
        return slots
    }

    private fun setShopInfoItem(inv: Inventory, total: Int, shown: Int) {
        if (inv.size < 9) return
        val slot = inv.size - 5
        val info = ItemStack(Material.WRITABLE_BOOK)
        val meta = info.itemMeta ?: return
        meta.setDisplayName(color(msg("gui.shop.info-title", "&6&l가축 상점 안내")))
        meta.lore = msgList(
            "gui.shop.info-lore",
            listOf(
                "&7좌클릭으로 가축 스폰알을 구매합니다.",
                "&7표시 가축: &f{shown} &7/ 전체: &f{total}",
                "&7구매한 스폰알은 섬에서만 사용 가능합니다."
            )
        ).map {
            color(it.replace("{shown}", shown.toString()).replace("{total}", total.toString()))
        }
        info.itemMeta = meta
        inv.setItem(slot, info)
    }

    private fun createPane(material: Material): ItemStack {
        val pane = ItemStack(material)
        val meta = pane.itemMeta ?: return pane
        meta.setDisplayName(color("&8"))
        addOptionalItemFlag(meta, "HIDE_ADDITIONAL_TOOLTIP")
        pane.itemMeta = meta
        return pane
    }

    private fun addOptionalItemFlag(meta: org.bukkit.inventory.meta.ItemMeta, flagName: String) {
        val flag = runCatching { ItemFlag.valueOf(flagName) }.getOrNull() ?: return
        meta.addItemFlags(flag)
    }

    private fun spawnCompatParticle(
        world: World,
        primaryName: String,
        location: Location,
        count: Int,
        offsetX: Double,
        offsetY: Double,
        offsetZ: Double
    ) {
        val particle = runCatching { Particle.valueOf(primaryName) }.getOrNull()
            ?: runCatching { Particle.valueOf("VILLAGER_HAPPY") }.getOrNull()
            ?: return
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ)
    }

    private fun loadMessageConfig(): FileConfiguration {
        val dir = File(plugin.dataFolder, "addons/SkyblockCattleAddon")
        if (!dir.exists()) dir.mkdirs()
        ensureAddonResource(dir, "message.yml")
        return YamlConfiguration.loadConfiguration(File(dir, "message.yml"))
    }

    private fun ensureAddonResource(dir: File, path: String) {
        val out = File(dir, path)
        if (out.exists()) return
        runCatching {
            val jarFile = File(SkyblockCattleAddon::class.java.protectionDomain.codeSource.location.toURI())
            JarFile(jarFile).use { jar ->
                val entry = jar.getJarEntry(path)
                if (entry != null) {
                    jar.getInputStream(entry).use { input ->
                        out.outputStream().use { output -> input.copyTo(output) }
                    }
                }
            }
        }.onFailure { ex ->
            plugin.logger.warning("[SkyblockCattleAddon] message.yml 생성 실패: ${ex.message}")
        }
    }

    private fun msg(path: String, fallback: String): String =
        messageConfig.getString(path) ?: fallback

    private fun msgList(path: String, fallback: List<String>): List<String> {
        val raw = messageConfig.getStringList(path)
        return if (raw.isNullOrEmpty()) fallback else raw
    }

    private fun epochSecond(): Long = System.currentTimeMillis() / 1000L
    private fun todayEpochDay(): Long = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
}


