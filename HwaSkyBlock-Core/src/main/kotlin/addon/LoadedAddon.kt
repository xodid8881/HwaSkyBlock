package org.hwabaeg.hwaskyblock.addon

import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import java.io.File
import java.net.URLClassLoader

data class LoadedAddon(
    val name: String,
    val version: String?,
    val instance: HwaSkyBlockAddon,
    val loader: URLClassLoader,
    val jarFile: File,
)
