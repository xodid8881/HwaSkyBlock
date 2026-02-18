import org.gradle.api.file.DuplicatesStrategy
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "org.hwabaeg.hwaskyblock.addon"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("reflect"))


    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:1.173.0")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation(project(":common:hsk-platform-api"))
    implementation(project(":platform:v1_16"))
    implementation(project(":platform:v1_17"))
    implementation(project(":platform:v1_18"))
    implementation(project(":platform:v1_19"))
    implementation(project(":platform:v1_20"))
    implementation(project(":platform:v1_21"))

    compileOnly(project(":common:hsk-core-common"))
    compileOnly(project(":HwaSkyBlock-Core"))
}

kotlin {
    jvmToolchain(17)
}

tasks {
    shadowJar {
        archiveFileName.set("SkyblockCattleAddon-${version}.jar")

        from("src/main/resources/addon.yml") {
            into("")
        }

        manifest {
            attributes(
                    "Addon-Main" to "org.hwabaeg.hwaskyblock.addon.cattle.SkyblockCattleAddon",
                    "Addon-Name" to "SkyblockCattleAddon",
                    "Addon-Version" to version
            )
        }
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
