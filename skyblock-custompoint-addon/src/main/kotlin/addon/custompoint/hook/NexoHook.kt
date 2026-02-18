package org.hwabaeg.hwaskyblock.addon.custompoint.hook

import org.bukkit.block.Block
import java.util.logging.Logger

class NexoHook(
    private val log: Logger
) {

    private var enabled = false

    fun hook(): Boolean {
        enabled = try {
            Class.forName("com.nexomc.nexo.api.NexoAPI")
            log.info("[CustomPointAddon] Nexo detected")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
        return enabled
    }

    fun isEnabled(): Boolean = enabled

    fun getBlockId(block: Block): String? {
        if (!enabled) return null

        return try {
            val apiClass = Class.forName("com.nexomc.nexo.api.NexoAPI")

            val isNexoBlockMethod = apiClass.getMethod("isNexoBlock", Block::class.java)
            val isNexo = isNexoBlockMethod.invoke(null, block) as Boolean

            if (!isNexo) return null

            val getBlockIdMethod = apiClass.getMethod("getBlockId", Block::class.java)
            getBlockIdMethod.invoke(null, block) as? String

        } catch (e: Exception) {
            log.warning("[CustomPointAddon] Failed to read Nexo block: ${e.message}")
            null
        }
    }
}
