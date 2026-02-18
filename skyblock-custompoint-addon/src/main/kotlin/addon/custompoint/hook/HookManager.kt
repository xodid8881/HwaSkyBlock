package org.hwabaeg.hwaskyblock.addon.custompoint.hook

import org.bukkit.Bukkit
import org.bukkit.block.Block
import java.util.logging.Logger

class HookManager(
    private val log: Logger
) {

    private var itemsAdderHook: ItemsAdderHook? = null
    private var oraxenHook: OraxenHook? = null
    private var nexoHook: NexoHook? = null
    private var customCropsHook: CustomCropsHook? = null

    fun init() {
        val pm = Bukkit.getPluginManager()

        if (pm.isPluginEnabled("ItemsAdder")) {
            val hook = ItemsAdderHook(log)
            if (hook.hook()) {
                itemsAdderHook = hook
                log.info("[SkyblockCustomPointAddon] ItemsAdder Hook 활성화")
            }
        }

        if (pm.isPluginEnabled("Oraxen")) {
            val hook = OraxenHook(log)
            if (hook.hook()) {
                oraxenHook = hook
                log.info("[SkyblockCustomPointAddon] Oraxen Hook 활성화")
            }
        }

        if (pm.isPluginEnabled("Nexo")) {
            val hook = NexoHook(log)
            if (hook.hook()) {
                nexoHook = hook
                log.info("[SkyblockCustomPointAddon] Nexo Hook 활성화")
            }
        }

        if (pm.isPluginEnabled("CustomCrops")) {
            val hook = CustomCropsHook(log)
            if (hook.hook()) {
                customCropsHook = hook
                log.info("[SkyblockCustomPointAddon] CustomCrops Hook 활성화")
            }
        }
    }

    fun resolveBlockId(block: Block): String {

        if (customCropsHook != null) {
            try {
                customCropsHook!!.getCropId(block)?.let {
                    return "customcrops:$it"
                }
            } catch (_: Exception) {
                // CustomCrop이 아닌 블록 → 조용히 무시
            }
        }

        itemsAdderHook?.getBlockId(block)?.let {
            return "itemsadder:$it"
        }

        oraxenHook?.getBlockId(block)?.let {
            return "oraxen:$it"
        }

        nexoHook?.getBlockId(block)?.let {
            return "nexo:$it"
        }

        return "minecraft:${block.type.name.lowercase()}"
    }

    fun debugStatus() {
        log.info("[SkyblockCustomPointAddon] Hook 상태")
        log.info(" - ItemsAdder: ${itemsAdderHook != null}")
        log.info(" - Oraxen: ${oraxenHook != null}")
        log.info(" - Nexo: ${nexoHook != null}")
        log.info(" - CustomCrops: ${customCropsHook != null}")
    }
}
