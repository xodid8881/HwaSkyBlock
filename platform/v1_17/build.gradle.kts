plugins {
    kotlin("jvm")
}

group = "org.hwabaeg.hwaskyblock.platform"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common:hsk-platform-api"))
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}
