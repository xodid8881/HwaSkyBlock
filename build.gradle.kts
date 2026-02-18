import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.20" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.purpurmc.org/snapshots")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        maven("https://repo.opencollab.dev/main/")
    }
}

subprojects {
    plugins.withId("com.github.johnrengelman.shadow") {
        val shadowJarTask = tasks.named<ShadowJar>("shadowJar")

        val copyShadowJarToRoot = tasks.register<Copy>("copyShadowJarToRoot") {
            dependsOn(shadowJarTask)
            from(shadowJarTask.flatMap { it.archiveFile })
            into(rootProject.layout.buildDirectory.dir("libs"))
        }

        tasks.named("build") {
            dependsOn(copyShadowJarToRoot)
        }
    }
}
