package org.hwabaeg.hwaskyblock.api

import org.bukkit.entity.Player

interface HwaSkyBlockAPI {
    fun hasIsland(player: Player): Boolean
    fun hasOwner(player: Player, island_number: Int): Boolean
    fun upgradeIsland(player: Player, island_number: Int, plus_size: Int)
}