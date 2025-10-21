import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "org.hwabaeg"
version = "v1.0.9-bukkit"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:1.173.0")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
}

kotlin {
    jvmToolchain(8)
}

tasks {
    shadowJar {
        archiveFileName.set("HwaSkyBlock-${project.version}.jar")
        mergeServiceFiles()

        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect"))
            include(dependency("org.xerial:sqlite-jdbc"))
        }
    }

    build {
        dependsOn(shadowJar)
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }
}
