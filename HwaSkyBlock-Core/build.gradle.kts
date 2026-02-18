import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "org.hwabaeg"
version = "v1.1.0-bukkit"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":common:hsk-core-common"))
    implementation(project(":common:hsk-platform-api"))
    implementation(project(":platform:v1_16"))
    implementation(project(":platform:v1_17"))
    implementation(project(":platform:v1_18"))
    implementation(project(":platform:v1_19"))
    implementation(project(":platform:v1_20"))
    implementation(project(":platform:v1_21"))

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:1.173.0")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    shadowJar {
        archiveFileName.set("HwaSkyBlock-${project.version}.jar")
        mergeServiceFiles()

        dependencies {
            include(project(":common:hsk-core-common"))
            include(project(":common:hsk-platform-api"))
            include(project(":platform:v1_16"))
            include(project(":platform:v1_17"))
            include(project(":platform:v1_18"))
            include(project(":platform:v1_19"))
            include(project(":platform:v1_20"))
            include(project(":platform:v1_21"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect"))
            include(dependency("org.xerial:sqlite-jdbc"))
        }
    }

    build {
        dependsOn(shadowJar)
    }

    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
