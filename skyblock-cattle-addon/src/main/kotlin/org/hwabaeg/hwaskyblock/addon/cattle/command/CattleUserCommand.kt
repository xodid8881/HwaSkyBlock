package org.hwabaeg.hwaskyblock.addon.cattle.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hwabaeg.hwaskyblock.addon.cattle.SkyblockCattleAddon

class CattleUserCommand(private val addon: SkyblockCattleAddon) : Command("가축") {
    init {
        description = "스카이블럭 가축 유저 명령어"
        usage = "/가축 <상점>"
        aliases = listOf("cattle", "isca")
    }

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            addon.helpUser(sender)
            return true
        }

        when (args[0].lowercase()) {
            "shop", "상점" -> {
                val player = sender as? Player ?: return true
                addon.openShop(player)
            }

            else -> addon.helpUser(sender)
        }

        return true
    }
}
