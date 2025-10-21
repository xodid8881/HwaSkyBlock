plugins {
    kotlin("jvm") version "1.9.25" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.purpurmc.org/snapshots")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        maven("https://repo.opencollab.dev/main/")
    }
}

subprojects {
    afterEvaluate {
        tasks.matching { it.name == "shadowJar" || it.name == "jar" }.configureEach {
            doLast {
                val jarFile = outputs.files.singleFile
                val outputDir = File(rootProject.buildDir, "libs")
                outputDir.mkdirs()

                val dest = File(outputDir, jarFile.name)
                jarFile.copyTo(dest, overwrite = true)
                println("✅ Copied ${jarFile.name} → ${dest.path}")
            }
        }

        tasks.named("build") {
            dependsOn("shadowJar")
        }
    }
}

