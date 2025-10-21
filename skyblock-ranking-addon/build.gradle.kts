import org.gradle.api.file.DuplicatesStrategy

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

    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:1.173.0")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")

    compileOnly(project(":HwaSkyBlock-Core"))
}

tasks {
    shadowJar {
        archiveFileName.set("SkyblockRankingAddon-${version}.jar")

        from("src/main/resources/addon.yml") {
            into("")
        }

        manifest {
            attributes(
                "Addon-Main" to "org.hwabaeg.hwaskyblock.addon.ranking.SkyblockRankingAddon",
                "Addon-Name" to "SkyblockRankingAddon",
                "Addon-Version" to version
            )
        }
    }
}