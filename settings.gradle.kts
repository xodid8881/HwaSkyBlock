pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "HwaSkyBlock"

include(
    ":v1_21:HwaSkyBlock-Core",
    ":v1_21:skyblock-ranking-addon"
)

include("v1_21:skyblock-custompoint-addon")