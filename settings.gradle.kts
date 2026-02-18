pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "HwaSkyBlock"

include(":common:hsk-core-common")
include(":common:hsk-platform-api")

include(":platform:v1_16")
include(":platform:v1_17")
include(":platform:v1_18")
include(":platform:v1_19")
include(":platform:v1_20")
include(":platform:v1_21")

include(":HwaSkyBlock-Core")
include(":skyblock-ranking-addon")
include(":skyblock-custompoint-addon")
include(":skyblock-cattle-addon")
include(":skyblock-dailymission-addon")
