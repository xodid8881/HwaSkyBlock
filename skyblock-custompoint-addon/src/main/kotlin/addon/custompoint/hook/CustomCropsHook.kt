package org.hwabaeg.hwaskyblock.addon.custompoint.hook

import org.bukkit.block.Block
import java.util.logging.Logger

class CustomCropsHook(
    private val log: Logger
) {

    private var enabled = false

    fun hook(): Boolean {
        enabled = try {
            Class.forName("net.momirealms.customcrops.api.CustomCropsAPI")
            log.info("[CustomPointAddon] CustomCrops detected")
            true
        } catch (_: ClassNotFoundException) {
            false
        }
        return enabled
    }

    fun isEnabled(): Boolean = enabled

    fun getCropId(block: Block): String? {
        if (!enabled) return null

        return try {
            val apiClass =
                Class.forName("net.momirealms.customcrops.api.CustomCropsAPI")

            val getCropMethod =
                apiClass.getMethod("getCrop", Block::class.java)

            val crop = getCropMethod.invoke(null, block) ?: return null

            val idField = crop.javaClass.getDeclaredField("id")
            idField.isAccessible = true
            idField.get(crop) as? String

        } catch (_: Exception) {
            // CustomCrop이 아닌 경우는 정상 흐름 → 로그 찍지 않음
            null
        }
    }
}
