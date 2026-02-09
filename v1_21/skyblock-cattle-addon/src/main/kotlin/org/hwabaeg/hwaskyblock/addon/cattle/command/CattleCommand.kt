package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.addon.cattle.SkyblockCattleAddon

class CattleCommand(private val addon: SkyblockCattleAddon) : Command("cattle") {
    init {
        description = "스카이블럭 가축 애드온 명령어"
        usage = "/cattle <상점|리로드|사료지급|번식지급>"
        aliases = listOf("isca", "가축")
    }

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            addon.help(sender)
            return true
        }

        when (args[0].lowercase()) {
            "shop", "상점" -> {
                val player = sender as? Player ?: return true
                addon.openShop(player)
            }

            "reload", "리로드" -> {
                if (!sender.hasPermission("hwaskyblock.cattle.reload")) return true
                addon.reload()
                sender.sendMessage("§a가축 애드온 설정을 다시 불러왔습니다.")
            }

            "givefeed", "사료지급" -> {
                if (!sender.hasPermission("hwaskyblock.cattle.give")) return true
                if (args.size < 3) return true
                val target = Bukkit.getPlayerExact(args[1]) ?: return true
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                if (addon.giveFeed(target, args[2], amount)) {
                    sender.sendMessage("§a지급 완료")
                } else {
                    sender.sendMessage("§c사료 ID를 찾을 수 없습니다.")
                }
            }

            "givebreed", "번식지급" -> {
                if (!sender.hasPermission("hwaskyblock.cattle.give")) return true
                if (args.size < 3) return true
                val target = Bukkit.getPlayerExact(args[1]) ?: return true
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                if (addon.giveBreed(target, args[2], amount)) {
                    sender.sendMessage("§a지급 완료")
                } else {
                    sender.sendMessage("§c번식 토큰 ID를 찾을 수 없습니다.")
                }
            }

            else -> addon.help(sender)
        }

        return true
    }
}
