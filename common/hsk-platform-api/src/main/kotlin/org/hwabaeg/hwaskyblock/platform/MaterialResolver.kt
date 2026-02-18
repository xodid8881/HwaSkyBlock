package org.hwabaeg.hwaskyblock.platform

import org.bukkit.Material

interface MaterialResolver {
    fun fromKeyOrNull(key: String): Material?
}
