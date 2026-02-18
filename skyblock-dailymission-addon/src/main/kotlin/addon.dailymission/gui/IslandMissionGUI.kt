package addon.dailymission.gui

import addon.dailymission.config.AddonConfig
import addon.dailymission.config.MessageConfig
import addon.dailymission.island.AssignedMission
import addon.dailymission.logic.MissionService
import addon.dailymission.mission.Mission
import addon.dailymission.mission.MissionDifficulty
import addon.dailymission.storage.MissionDataStore
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.enchantments.Enchantment
import org.hwabaeg.hwaskyblock.platform.PlatformFactory
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt

class IslandMissionGUI(
    private val config: AddonConfig,
    private val missionService: MissionService,
    private val store: MissionDataStore
) {
    private val detailPrefix = "[DailyMissionDetail]"
    private val materialResolver = PlatformFactory.createMaterialResolver(Bukkit.getServer())

    fun open(player: Player, islandId: String) {
        val size = (config.guiRows.coerceIn(1, 6)) * 9
        val inv = Bukkit.createInventory(MissionMainHolder(), size, color(MessageConfig.guiTitle))
        val assignments = missionService.getTodayAssignments(islandId)

        if (config.guiCustomEnabled) {
            applyCustomItems(inv, islandId, assignments)
        } else {
            setMissionItem(inv, config.guiSlotVeryEasy, MissionDifficulty.VERY_EASY, assignments[MissionDifficulty.VERY_EASY], islandId)
            setMissionItem(inv, config.guiSlotEasy, MissionDifficulty.EASY, assignments[MissionDifficulty.EASY], islandId)
            setMissionItem(inv, config.guiSlotNormal, MissionDifficulty.NORMAL, assignments[MissionDifficulty.NORMAL], islandId)
            setMissionItem(inv, config.guiSlotHard, MissionDifficulty.HARD, assignments[MissionDifficulty.HARD], islandId)
            setMissionItem(inv, config.guiSlotVeryHard, MissionDifficulty.VERY_HARD, assignments[MissionDifficulty.VERY_HARD], islandId)

            safeSet(inv, config.guiSlotInfo, infoItem(islandId))
        }
        fillEmpty(inv)
        player.openInventory(inv)
    }

    fun openDetail(player: Player, islandId: String, difficulty: MissionDifficulty) {
        val size = (config.guiDetailRows.coerceIn(1, 6)) * 9
        val title = color("${MessageConfig.detailTitle} ${difficultyName(difficulty)} $detailPrefix")
        val inv = Bukkit.createInventory(MissionDetailHolder(difficulty), size, title)

        val assignments = missionService.getTodayAssignments(islandId)
        val assigned = assignments[difficulty] ?: return
        val mission = missionService.getMissionById(assigned.missionId)

        val info = ItemStack(iconMaterial(difficulty, assigned))
        val meta = info.itemMeta
        if (meta != null) {
            meta.setDisplayName(color(applyPlaceholders(MessageConfig.detailInfoName, islandId, mission, assigned)))
            meta.lore = buildDetailLore(islandId, mission, assigned)
            info.itemMeta = meta
        }
        safeSet(inv, config.guiDetailInfoSlot, info)

        val rewardItem = buildSimpleItem(
            Material.EMERALD,
            MessageConfig.detailRewardName,
            MessageConfig.detailRewardLore,
            islandId,
            mission,
            assigned
        )
        safeSet(inv, config.guiDetailRewardSlot, rewardItem)

        val statusText = when {
            assigned.completed -> MessageConfig.statusDone
            assigned.progress > 0 -> MessageConfig.statusProgress
            else -> MessageConfig.statusNone
        }
        val statusItem = buildSimpleItem(
            Material.COMPASS,
            MessageConfig.detailStatusName,
            MessageConfig.detailStatusLore.replace("%status%", statusText),
            islandId,
            mission,
            assigned
        )
        safeSet(inv, config.guiDetailStatusSlot, statusItem)

        val pointItem = buildSimpleItem(
            Material.EXPERIENCE_BOTTLE,
            MessageConfig.detailPointName,
            MessageConfig.detailPointLore,
            islandId,
            mission,
            assigned
        )
        safeSet(inv, config.guiDetailPointSlot, pointItem)

        applyDetailButtons(inv, islandId, assigned, mission)

        val back = ItemStack(materialOrNull(config.guiDetailBackMaterial) ?: Material.ARROW)
        val backMeta = back.itemMeta
        if (backMeta != null) {
            backMeta.setDisplayName(color(MessageConfig.detailBackName))
            back.itemMeta = backMeta
        }
        safeSet(inv, config.guiDetailBackSlot, back)

        fillDetailEmpty(inv)
        player.openInventory(inv)
    }

    private fun setMissionItem(
        inv: Inventory,
        slot: Int,
        difficulty: MissionDifficulty,
        assigned: AssignedMission?,
        islandId: String,
        overrideName: String? = null,
        overrideLore: List<String>? = null
    ) {
        if (assigned == null) return
        val mission = missionService.getMissionById(assigned.missionId)
        val item = ItemStack(iconMaterial(difficulty, assigned))
        val meta = item.itemMeta ?: return

        val namePrefix = when {
            assigned.completed -> MessageConfig.statusPrefixDone
            assigned.progress > 0 -> MessageConfig.statusPrefixProgress
            else -> MessageConfig.statusPrefixNone
        }
        val baseName = overrideName?.ifBlank { null } ?: difficultyName(difficulty)
        val finalName = applyPlaceholders(baseName, islandId, mission, assigned)
        meta.setDisplayName(color(namePrefix + finalName))

        meta.lore = if (!overrideLore.isNullOrEmpty()) {
            overrideLore.map { color(applyPlaceholders(it, islandId, mission, assigned)) }
        } else {
            buildLore(mission?.name ?: assigned.missionId, mission?.amount ?: 0, mission?.reward ?: 0, assigned)
        }

        if (assigned.completed) {
            addCompatGlow(meta)
        }

        item.itemMeta = meta
        safeSet(inv, slot, item)
    }

    private fun buildLore(
        name: String,
        goal: Int,
        reward: Int,
        assigned: AssignedMission
    ): List<String> {
        val list = mutableListOf<String>()
        list.add(color(MessageConfig.loreMission.replace("%name%", name)))
        list.add(
            color(
                MessageConfig.loreProgress
                    .replace("%current%", assigned.progress.toString())
                    .replace("%goal%", goal.toString())
            )
        )
        list.add(color(MessageConfig.loreReward.replace("%reward%", reward.toString())))

        if (config.mainPointEnabled) {
            val mainPoint = (reward * config.mainPointRatio).roundToInt()
            list.add(color(MessageConfig.loreMainPoint.replace("%main%", mainPoint.toString())))
        }

        val status = when {
            assigned.completed -> MessageConfig.statusDone
            assigned.progress > 0 -> MessageConfig.statusProgress
            else -> MessageConfig.statusNone
        }
        list.add(color(MessageConfig.loreStatus.replace("%status%", status)))
        return list
    }

    private fun buildDetailLore(
        islandId: String,
        mission: Mission?,
        assigned: AssignedMission
    ): List<String> {
        val list = mutableListOf<String>()
        list.add(color(MessageConfig.detailLoreHeader))
        list.add(color(applyPlaceholders(MessageConfig.detailLoreDescription, islandId, mission, assigned)))
        list.add(color(applyPlaceholders(MessageConfig.detailLoreTarget, islandId, mission, assigned)))
        list.add(color(applyPlaceholders(MessageConfig.detailLoreProgress, islandId, mission, assigned)))
        list.add(color(buildGaugeLine(islandId, assigned, mission)))
        list.add(color(applyPlaceholders(MessageConfig.detailLoreReward, islandId, mission, assigned)))
        list.add(color(MessageConfig.detailLoreHeader))
        return list
    }

    private fun iconMaterial(diff: MissionDifficulty, assigned: AssignedMission): Material {
        val diffStatusKey = when (diff) {
            MissionDifficulty.VERY_EASY -> when {
                assigned.completed -> config.guiIconSetVeryEasyDone
                assigned.progress > 0 -> config.guiIconSetVeryEasyProgress
                else -> config.guiIconSetVeryEasyNone
            }
            MissionDifficulty.EASY -> when {
                assigned.completed -> config.guiIconSetEasyDone
                assigned.progress > 0 -> config.guiIconSetEasyProgress
                else -> config.guiIconSetEasyNone
            }
            MissionDifficulty.NORMAL -> when {
                assigned.completed -> config.guiIconSetNormalDone
                assigned.progress > 0 -> config.guiIconSetNormalProgress
                else -> config.guiIconSetNormalNone
            }
            MissionDifficulty.HARD -> when {
                assigned.completed -> config.guiIconSetHardDone
                assigned.progress > 0 -> config.guiIconSetHardProgress
                else -> config.guiIconSetHardNone
            }
            MissionDifficulty.VERY_HARD -> when {
                assigned.completed -> config.guiIconSetVeryHardDone
                assigned.progress > 0 -> config.guiIconSetVeryHardProgress
                else -> config.guiIconSetVeryHardNone
            }
        }

        val diffStatusMaterial = materialOrNull(diffStatusKey)
        if (diffStatusMaterial != null) return diffStatusMaterial

        val statusKey = when {
            assigned.completed -> config.guiIconDone
            assigned.progress > 0 -> config.guiIconProgress
            else -> config.guiIconNone
        }
        val baseKey = when (diff) {
            MissionDifficulty.VERY_EASY -> config.guiIconVeryEasy
            MissionDifficulty.EASY -> config.guiIconEasy
            MissionDifficulty.NORMAL -> config.guiIconNormal
            MissionDifficulty.HARD -> config.guiIconHard
            MissionDifficulty.VERY_HARD -> config.guiIconVeryHard
        }
        val status = materialOrNull(statusKey)
        return status ?: (materialOrNull(baseKey) ?: Material.PAPER)
    }

    private fun difficultyName(diff: MissionDifficulty): String {
        return when (diff) {
            MissionDifficulty.VERY_EASY -> MessageConfig.diffVeryEasy
            MissionDifficulty.EASY -> MessageConfig.diffEasy
            MissionDifficulty.NORMAL -> MessageConfig.diffNormal
            MissionDifficulty.HARD -> MessageConfig.diffHard
            MissionDifficulty.VERY_HARD -> MessageConfig.diffVeryHard
        }
    }

    private fun infoItem(islandId: String): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(color(MessageConfig.infoTitle))
        meta.lore = listOf(
            color(applyPlaceholders(MessageConfig.infoDate, islandId, null, null)),
            color(applyPlaceholders(MessageConfig.infoPoint, islandId, null, null))
        )
        item.itemMeta = meta
        return item
    }

    private fun fillEmpty(inv: Inventory) {
        if (!config.guiFillerEnabled) return
        val material = materialOrNull(config.guiFillerMaterial) ?: return
        val filler = ItemStack(material)
        val meta = filler.itemMeta
        if (meta != null) {
            meta.setDisplayName(" ")
            filler.itemMeta = meta
        }
        for (i in 0 until inv.size) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler)
            }
        }
    }

    private fun fillDetailEmpty(inv: Inventory) {
        if (!config.guiDetailFillerEnabled) return
        val material = materialOrNull(config.guiDetailFillerMaterial) ?: return
        val filler = ItemStack(material)
        val meta = filler.itemMeta
        if (meta != null) {
            meta.setDisplayName(" ")
            filler.itemMeta = meta
        }
        for (i in 0 until inv.size) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler)
            }
        }
    }

    private fun applyCustomItems(
        inv: Inventory,
        islandId: String,
        assignments: Map<MissionDifficulty, AssignedMission>
    ) {
        for (item in config.guiCustomItems) {
            val type = item.type?.uppercase()
            when (type) {
                "MISSION_VERY_EASY" -> setMissionItem(inv, item.slot, MissionDifficulty.VERY_EASY, assignments[MissionDifficulty.VERY_EASY], islandId, item.name, item.lore)
                "MISSION_EASY" -> setMissionItem(inv, item.slot, MissionDifficulty.EASY, assignments[MissionDifficulty.EASY], islandId, item.name, item.lore)
                "MISSION_NORMAL" -> setMissionItem(inv, item.slot, MissionDifficulty.NORMAL, assignments[MissionDifficulty.NORMAL], islandId, item.name, item.lore)
                "MISSION_HARD" -> setMissionItem(inv, item.slot, MissionDifficulty.HARD, assignments[MissionDifficulty.HARD], islandId, item.name, item.lore)
                "MISSION_VERY_HARD" -> setMissionItem(inv, item.slot, MissionDifficulty.VERY_HARD, assignments[MissionDifficulty.VERY_HARD], islandId, item.name, item.lore)
                "INFO" -> safeSet(inv, item.slot, infoItem(islandId))
                else -> {
                    val material = materialOrNull(item.material) ?: continue
                    val stack = ItemStack(material)
                    val meta = stack.itemMeta
                    if (meta != null) {
                        if (item.name != null) meta.setDisplayName(color(applyPlaceholders(item.name, islandId, null, null)))
                        if (item.lore.isNotEmpty()) {
                            meta.lore = item.lore.map { color(applyPlaceholders(it, islandId, null, null)) }
                        }
                        if (item.glow) addCompatGlow(meta)
                        stack.itemMeta = meta
                    }
                    safeSet(inv, item.slot, stack)
                }
            }
        }
    }

    fun isDetailTitle(title: String): Boolean {
        return title.contains(detailPrefix)
    }

    fun resolveDifficultyBySlot(slot: Int): MissionDifficulty? {
        if (config.guiCustomEnabled) {
            val type = config.guiCustomItems.firstOrNull { it.slot == slot }?.type?.uppercase() ?: return null
            return when (type) {
                "MISSION_VERY_EASY" -> MissionDifficulty.VERY_EASY
                "MISSION_EASY" -> MissionDifficulty.EASY
                "MISSION_NORMAL" -> MissionDifficulty.NORMAL
                "MISSION_HARD" -> MissionDifficulty.HARD
                "MISSION_VERY_HARD" -> MissionDifficulty.VERY_HARD
                else -> null
            }
        }
        return when (slot) {
            config.guiSlotVeryEasy -> MissionDifficulty.VERY_EASY
            config.guiSlotEasy -> MissionDifficulty.EASY
            config.guiSlotNormal -> MissionDifficulty.NORMAL
            config.guiSlotHard -> MissionDifficulty.HARD
            config.guiSlotVeryHard -> MissionDifficulty.VERY_HARD
            else -> null
        }
    }

    private fun safeSet(inv: Inventory, slot: Int, item: ItemStack) {
        if (slot < 0 || slot >= inv.size) return
        inv.setItem(slot, item)
    }

    private fun materialOrNull(name: String?): Material? {
        if (name.isNullOrBlank()) return null
        return materialResolver.fromKeyOrNull(name.trim())
    }

    private fun buildSimpleItem(
        material: Material,
        name: String,
        loreLine: String,
        islandId: String,
        mission: Mission?,
        assigned: AssignedMission
    ): ItemStack {
        if (material == Material.AIR) return ItemStack(Material.AIR)
        val item = ItemStack(material)
        val meta = item.itemMeta
        if (meta != null) {
            meta.setDisplayName(color(applyPlaceholders(name, islandId, mission, assigned)))
            meta.lore = listOf(color(applyPlaceholders(loreLine, islandId, mission, assigned)))
            item.itemMeta = meta
        }
        return item
    }

    private fun applyDetailButtons(
        inv: Inventory,
        islandId: String,
        assigned: AssignedMission,
        mission: Mission?
    ) {
        for (btn in config.guiDetailButtons) {
            val material = materialOrNull(btn.material) ?: continue
            val stack = ItemStack(material)
            val meta = stack.itemMeta
            if (meta != null) {
                if (btn.name != null) meta.setDisplayName(color(applyPlaceholders(btn.name, islandId, mission, assigned)))
                if (btn.lore.isNotEmpty()) {
                    meta.lore = btn.lore.map { color(applyPlaceholders(it, islandId, mission, assigned)) }
                }
                if (btn.glow) addCompatGlow(meta)
                stack.itemMeta = meta
            }
            safeSet(inv, btn.slot, stack)
        }
    }

    fun handleDetailButtonClick(
        player: Player,
        slot: Int,
        islandId: String,
        assigned: AssignedMission,
        mission: Mission?
    ): Boolean {
        val btn = config.guiDetailButtons.firstOrNull { it.slot == slot } ?: return false
        when (btn.action.uppercase()) {
            "BACK" -> {
                open(player, islandId)
                return true
            }
            "CLOSE" -> {
                player.closeInventory()
                return true
            }
            "MESSAGE" -> {
                val msg = btn.value ?: return true
                player.sendMessage(color(applyPlaceholders(msg, islandId, mission, assigned)))
                return true
            }
            "COMMAND" -> {
                val cmd = btn.value ?: return true
                player.performCommand(applyPlaceholders(cmd, islandId, mission, assigned))
                return true
            }
            else -> return true
        }
    }

    fun getDetailAssigned(islandId: String, difficulty: MissionDifficulty): Pair<AssignedMission, Mission?>? {
        val assignments = missionService.getTodayAssignments(islandId)
        val assigned = assignments[difficulty] ?: return null
        val mission = missionService.getMissionById(assigned.missionId)
        return assigned to mission
    }

    private fun applyPlaceholders(
        text: String,
        islandId: String,
        mission: Mission?,
        assigned: AssignedMission?
    ): String {
        val missionPoint = store.getMissionPoint(islandId)
        val date = LocalDate.now(ZoneId.systemDefault()).toString()
        val name = mission?.name ?: assigned?.missionId ?: ""
        val description = mission?.description ?: ""
        val goal = mission?.amount ?: 0
        val current = assigned?.progress ?: 0
        val reward = mission?.reward ?: 0

        val percent = if (goal <= 0) 0 else ((current * 100.0) / goal).toInt().coerceIn(0, 100)
        return text
            .replace("%date%", date)
            .replace("%point%", missionPoint.toString())
            .replace("%name%", name)
            .replace("%description%", description)
            .replace("%goal%", goal.toString())
            .replace("%current%", current.toString())
            .replace("%reward%", reward.toString())
            .replace("%percent%", percent.toString())
    }

    private fun buildGaugeLine(
        islandId: String,
        assigned: AssignedMission,
        mission: Mission?
    ): String {
        val goal = mission?.amount ?: 0
        val current = assigned.progress
        val percent = if (goal <= 0) 0 else ((current * 100.0) / goal).toInt().coerceIn(0, 100)
        val bar = buildBar(percent, 10, "#", "-")
        return applyPlaceholders(MessageConfig.detailLoreGauge, islandId, mission, assigned)
            .replace("%bar%", bar)
            .replace("%percent%", percent.toString())
    }

    private fun buildBar(percent: Int, length: Int, full: String, empty: String): String {
        val filled = ((percent / 100.0) * length).toInt().coerceIn(0, length)
        val sb = StringBuilder()
        repeat(filled) { sb.append(full) }
        repeat(length - filled) { sb.append(empty) }
        return sb.toString()
    }

    private fun color(text: String): String =
        ChatColor.translateAlternateColorCodes('&', text)

    private fun addCompatGlow(meta: org.bukkit.inventory.meta.ItemMeta) {
        val enchant = Enchantment.getByName("UNBREAKING")
            ?: Enchantment.getByName("DURABILITY")
            ?: return
        meta.addEnchant(enchant, 1, true)
    }
}
