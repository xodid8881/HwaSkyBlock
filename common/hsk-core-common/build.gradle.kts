plugins {
    kotlin("jvm")
}

group = "org.hwabaeg.hwaskyblock"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common:hsk-platform-api"))
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(17)
}
