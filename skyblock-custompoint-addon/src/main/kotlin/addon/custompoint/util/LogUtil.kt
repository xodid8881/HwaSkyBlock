package org.hwabaeg.hwaskyblock.addon.custompoint.util

import java.util.logging.Logger

object LogUtil {

    private const val PREFIX = "[SkyblockCustomPointAddon]"

    private var debugEnabled = false
    private lateinit var logger: Logger

    fun init(pluginLogger: Logger, debug: Boolean) {
        logger = pluginLogger
        debugEnabled = debug
    }

    fun info(message: String) {
        logger.info("$PREFIX $message")
    }

    fun warn(message: String) {
        logger.warning("$PREFIX $message")
    }

    fun error(message: String, throwable: Throwable? = null) {
        logger.severe("$PREFIX $message")
        throwable?.printStackTrace()
    }

    fun debug(message: String) {
        if (!debugEnabled) return
        logger.info("$PREFIX [DEBUG] $message")
    }

    fun island(islandId: String, message: String) {
        debug("Island[$islandId] $message")
    }

    fun player(playerName: String, message: String) {
        debug("Player[$playerName] $message")
    }

    fun hook(hookName: String, message: String) {
        debug("Hook[$hookName] $message")
    }

    fun event(eventName: String, message: String) {
        debug("Event[$eventName] $message")
    }
}
