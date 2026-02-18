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
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // Optional plugin APIs
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    implementation("io.th0rgal:oraxen:1.205.0")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("net.momirealms:custom-crops:3.6.40")
    implementation(project(":common:hsk-platform-api"))
    implementation(project(":platform:v1_16"))
    implementation(project(":platform:v1_17"))
    implementation(project(":platform:v1_18"))
    implementation(project(":platform:v1_19"))
    implementation(project(":platform:v1_20"))
    implementation(project(":platform:v1_21"))
    // HwaSkyBlock Core
    compileOnly(project(":common:hsk-core-common"))
    compileOnly(project(":HwaSkyBlock-Core"))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    shadowJar {
        archiveFileName.set("SkyblockCustomPointAddon-${version}.jar")

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
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
