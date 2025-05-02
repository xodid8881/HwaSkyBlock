package org.hwabeag.hwaskyblock.api

import org.bukkit.entity.Player

interface HwaSkyBlockAPI {
    fun hasIsland(player: Player): Boolean
    fun hasOwner(player: Player, number: Int): Boolean
    fun upgradeIsland(player: Player, number: Int, plusSize: Int)
}