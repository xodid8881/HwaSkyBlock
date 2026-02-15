package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.hwabaeg.hwaskyblock.addon.cattle.SkyblockCattleAddon

class CattleAdminCommand(private val addon: SkyblockCattleAddon) : Command("가축관리") {
    init {
        description = "스카이블럭 가축 관리자 명령어"
        usage = "/가축관리 <리로드|급식지급|번식지급>"
        aliases = listOf("cattleadmin", "iscaadmin")
    }

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("hwaskyblock.cattle.admin")) {
            addon.uiError(sender, addon.text("command.common.no-permission", "권한이 없습니다."))
            return true
        }

        if (args.isEmpty()) {
            addon.helpAdmin(sender)
            return true
        }

        when (args[0].lowercase()) {
            "reload", "리로드" -> {
                addon.reload()
                addon.uiSuccess(
                    sender,
                    addon.text("command.reload.success", "가축 애드온 설정을 다시 불러왔습니다.")
                )
            }

            "givefeed", "급식지급", "사료지급" -> {
                if (args.size < 3) {
                    addon.uiError(sender, addon.text("command.common.usage.givefeed", "사용법: /가축관리 급식지급 <플레이어> <사료ID> [수량]"))
                    return true
                }
                val target = Bukkit.getPlayerExact(args[1])
                if (target == null) {
                    addon.uiError(sender, addon.text("command.common.player-not-found", "대상 플레이어를 찾을 수 없습니다."))
                    return true
                }
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                if (addon.giveFeed(target, args[2], amount)) {
                    addon.uiSuccess(sender, addon.text("command.givefeed.success", "급식 아이템을 지급했습니다."))
                } else {
                    addon.uiError(sender, addon.text("command.givefeed.invalid-id", "급식 ID를 찾을 수 없습니다."))
                }
            }

            "givebreed", "번식지급", "토큰지급" -> {
                if (args.size < 3) {
                    addon.uiError(sender, addon.text("command.common.usage.givebreed", "사용법: /가축관리 번식지급 <플레이어> <토큰ID> [수량]"))
                    return true
                }
                val target = Bukkit.getPlayerExact(args[1])
                if (target == null) {
                    addon.uiError(sender, addon.text("command.common.player-not-found", "대상 플레이어를 찾을 수 없습니다."))
                    return true
                }
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                if (addon.giveBreed(target, args[2], amount)) {
                    addon.uiSuccess(sender, addon.text("command.givebreed.success", "번식 토큰을 지급했습니다."))
                } else {
                    addon.uiError(sender, addon.text("command.givebreed.invalid-id", "번식 토큰 ID를 찾을 수 없습니다."))
                }
            }

            else -> addon.helpAdmin(sender)
        }

        return true
    }
}
