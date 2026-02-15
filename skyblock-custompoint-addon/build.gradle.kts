import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "org.hwabaeg.hwaskyblock.addon"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.oraxen.com/releases")
    maven("https://repo.nexomc.com/releases")
    maven("https://repo.momirealms.net/releases/")
}

dependencies {
    // Kotlin (애드온은 직접 포함 권장)
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // Paper API
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    // Optional plugin APIs
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    implementation("io.th0rgal:oraxen:1.205.0")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("com.nexomc:nexo:1.15.0")
    compileOnly("net.momirealms:custom-crops:3.6.40")
    // HwaSkyBlock Core
    compileOnly(project(":v1_21:HwaSkyBlock-Core"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        archiveFileName.set("SkyblockCustomPointAddon-${version}-v1.21.jar")

        from("src/main/resources/addon.yml") {
            into("")
        }

        mergeServiceFiles()

        manifest {
            attributes(
                "Addon-Main" to "org.hwabaeg.hwaskyblock.addon.custompoint.SkyblockCustomPointAddon",
                "Addon-Name" to "SkyblockCustomPointAddon",
                "Addon-Version" to version
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
}
