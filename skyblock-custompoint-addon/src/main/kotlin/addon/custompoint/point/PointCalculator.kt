package org.hwabaeg.hwaskyblock.addon.custompoint.point

import kotlin.random.Random

object PointCalculator {

    fun fixed(point: Int): Int {
        return if (point > 0) point else 0
    }

    fun random(min: Int, max: Int): Int {
        if (min <= 0 || max <= 0) return 0
        if (min > max) return 0

        return Random.nextInt(min, max + 1)
    }
}
