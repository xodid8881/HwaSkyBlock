package org.hwabaeg.hwaskyblock.addon

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.hwabaeg.hwaskyblock.api.HwaSkyBlockAddon
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

class AddonLoader(private val plugin: JavaPlugin) {

    private val loaded = mutableMapOf<String, Pair<HwaSkyBlockAddon, ClassLoader>>()

    fun loadAll() {
        val libsDir = File(plugin.dataFolder, "Libs")
        if (!libsDir.exists()) libsDir.mkdirs()

        libsDir.listFiles { f -> f.isFile && f.name.endsWith(".jar", ignoreCase = true) }
            ?.forEach { jar ->
                try {
                    loadOne(jar)
                } catch (t: Throwable) {
                    Bukkit.getLogger().warning("[HwaSkyBlock] ❌ 애드온 로드 실패 (${jar.name}): ${t::class.java.name}: ${t.message}")
                    t.printStackTrace()
                }
            }
    }

    fun disableAll() {
        loaded.values.forEach { (addon, _) ->
            try {
                addon.onDisable()
            } catch (t: Throwable) {
                Bukkit.getLogger().warning("[HwaSkyBlock] ⚠ 애드온 onDisable 중 오류: ${t::class.java.name}: ${t.message}")
                t.printStackTrace()
            }
        }
        loaded.clear()
    }

    private fun loadOne(jar: File) {
        val manifest = JarFile(jar).use { it.manifest } ?: error("MANIFEST.MF 없음")
        val attrs = manifest.mainAttributes
        val mainClass = attrs.getValue("Addon-Main") ?: error("Addon-Main 미지정")
        val name = attrs.getValue("Addon-Name") ?: jar.nameWithoutExtension
        val ver = attrs.getValue("Addon-Version") ?: "unknown"

        val parent: ClassLoader = plugin.javaClass.classLoader
        val cl: URLClassLoader = URLClassLoader(arrayOf<URL>(jar.toURI().toURL()), parent)

        val clazz = Class.forName(mainClass, true, cl)
        if (!HwaSkyBlockAddon::class.java.isAssignableFrom(clazz)) {
            error("메인 클래스가 HwaSkyBlockAddon을 구현하지 않음: $mainClass")
        }

        val instance = clazz.getDeclaredConstructor().newInstance() as HwaSkyBlockAddon
        instance.onEnable(plugin)

        loaded[name] = instance to cl
        Bukkit.getLogger().info("[HwaSkyBlock] ✅ 애드온 로드 완료: $name v$ver ($mainClass)")
    }
}
