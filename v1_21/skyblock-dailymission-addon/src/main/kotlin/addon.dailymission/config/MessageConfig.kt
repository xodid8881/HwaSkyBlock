package addon.dailymission.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.jar.JarFile

object MessageConfig {
    private lateinit var config: FileConfiguration

    var prefix = "&7[&aHwaSkyBlock&7] "
    var guiTitle = "&6일일 미션"
    var diffVeryEasy = "&a매우 쉬움"
    var diffEasy = "&a쉬움"
    var diffNormal = "&e일반"
    var diffHard = "&c어려움"
    var diffVeryHard = "&4매우 어려움"
    var noIsland = "&c섬이 없습니다."
    var disabled = "&c일일 미션 애드온이 비활성화되어 있습니다."
    var reload = "&a일일 미션 설정이 리로드되었습니다."
    var infoTitle = "&e정보"
    var infoDate = "&7오늘 날짜: &f%date%"
    var infoPoint = "&7미션 포인트: &f%point%"
    var loreMission = "&f미션: &e%name%"
    var loreProgress = "&f진행도: &a%current% &7/ &f%goal%"
    var loreReward = "&f보상: &b%reward%"
    var loreMainPoint = "&f메인 포인트: &b%main%"
    var loreStatus = "&f상태: %status%"
    var statusNone = "&7미진행"
    var statusProgress = "&e진행 중"
    var statusDone = "&a완료"
    var statusPrefixNone = "&7[ ] "
    var statusPrefixProgress = "&e[~] "
    var statusPrefixDone = "&a[✔] "
    var actionbarProgress = "&e%name% &7(&a%current%&7/&f%goal%&7)"
    var broadcastComplete = "&a미션 완료: &f%name% &7(+%reward%)"
    var detailTitle = "&6미션 상세"
    var detailBackName = "&a뒤로가기"
    var detailInfoName = "&e%name%"
    var detailLoreHeader = "&7----------------"
    var detailLoreDescription = "&f%description%"
    var detailLoreTarget = "&f목표: &b%goal%"
    var detailLoreProgress = "&f진행: &a%current%&7/&f%goal%"
    var detailLoreReward = "&f보상: &b%reward%"
    var detailLoreGauge = "&f진행률: &a%bar% &7(%percent%%)"
    var detailRewardName = "&b보상"
    var detailStatusName = "&e상태"
    var detailPointName = "&e미션 포인트"
    var detailRewardLore = "&7미션 포인트 +%reward%"
    var detailStatusLore = "&7%status%"
    var detailPointLore = "&7누적: %point%"

    fun load(plugin: JavaPlugin) {
        val dir = File(plugin.dataFolder, "addons/SkyblockDailyMissionAddon/message")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "message.yml")
        if (!file.exists()) extractFromJar(plugin, "message.yml", file)

        config = YamlConfiguration.loadConfiguration(file)
        read()
    }

    private fun read() {
        prefix = config.getString("prefix", prefix)!!
        guiTitle = config.getString("gui.title", guiTitle)!!
        diffVeryEasy = config.getString("gui.diff.very-easy", diffVeryEasy)!!
        diffEasy = config.getString("gui.diff.easy", diffEasy)!!
        diffNormal = config.getString("gui.diff.normal", diffNormal)!!
        diffHard = config.getString("gui.diff.hard", diffHard)!!
        diffVeryHard = config.getString("gui.diff.very-hard", diffVeryHard)!!
        noIsland = config.getString("message.no-island", noIsland)!!
        disabled = config.getString("message.disabled", disabled)!!
        reload = config.getString("message.reload", reload)!!
        infoTitle = config.getString("gui.info-title", infoTitle)!!
        infoDate = config.getString("gui.info-date", infoDate)!!
        infoPoint = config.getString("gui.info-point", infoPoint)!!
        loreMission = config.getString("gui.lore.mission", loreMission)!!
        loreProgress = config.getString("gui.lore.progress", loreProgress)!!
        loreReward = config.getString("gui.lore.reward", loreReward)!!
        loreMainPoint = config.getString("gui.lore.main-point", loreMainPoint)!!
        loreStatus = config.getString("gui.lore.status", loreStatus)!!
        statusNone = config.getString("gui.status.none", statusNone)!!
        statusProgress = config.getString("gui.status.progress", statusProgress)!!
        statusDone = config.getString("gui.status.done", statusDone)!!
        statusPrefixNone = config.getString("gui.status-prefix.none", statusPrefixNone)!!
        statusPrefixProgress = config.getString("gui.status-prefix.progress", statusPrefixProgress)!!
        statusPrefixDone = config.getString("gui.status-prefix.done", statusPrefixDone)!!
        actionbarProgress = config.getString("message.actionbar-progress", actionbarProgress)!!
        broadcastComplete = config.getString("message.broadcast-complete", broadcastComplete)!!
        detailTitle = config.getString("gui.detail.title", detailTitle)!!
        detailBackName = config.getString("gui.detail.back-name", detailBackName)!!
        detailInfoName = config.getString("gui.detail.info-name", detailInfoName)!!
        detailLoreHeader = config.getString("gui.detail.lore.header", detailLoreHeader)!!
        detailLoreDescription = config.getString("gui.detail.lore.description", detailLoreDescription)!!
        detailLoreTarget = config.getString("gui.detail.lore.target", detailLoreTarget)!!
        detailLoreProgress = config.getString("gui.detail.lore.progress", detailLoreProgress)!!
        detailLoreReward = config.getString("gui.detail.lore.reward", detailLoreReward)!!
        detailLoreGauge = config.getString("gui.detail.lore.gauge", detailLoreGauge)!!
        detailRewardName = config.getString("gui.detail.reward-name", detailRewardName)!!
        detailStatusName = config.getString("gui.detail.status-name", detailStatusName)!!
        detailPointName = config.getString("gui.detail.point-name", detailPointName)!!
        detailRewardLore = config.getString("gui.detail.reward-lore", detailRewardLore)!!
        detailStatusLore = config.getString("gui.detail.status-lore", detailStatusLore)!!
        detailPointLore = config.getString("gui.detail.point-lore", detailPointLore)!!
    }

    private fun extractFromJar(plugin: JavaPlugin, path: String, outFile: File) {
        val jarFile = File(
            MessageConfig::class.java
                .protectionDomain
                .codeSource
                .location
                .toURI()
        )

        JarFile(jarFile).use { jar ->
            jar.getJarEntry(path)?.let { entry ->
                jar.getInputStream(entry).use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } ?: plugin.logger.severe(
                "[SkyblockDailyMissionAddon] $path not found in addon jar"
            )
        }
    }
}
