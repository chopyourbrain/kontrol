pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "kontrol"
include(":kontrol")
include(":kontrol-noop")
include(":sample:androidApp")
include(":sample:shared")
