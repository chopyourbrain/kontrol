plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-atomicfu")
}

version = "1.0"

kotlin {
    android()

    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kontrol"))
                implementation(dep.ktor.core.common)
                implementation(dep.ktor.network)
                implementation(dep.napier)
                implementation(dep.settings)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":kontrol"))
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(project(":kontrol"))
                implementation(dep.ktor.client.ios)
                implementation(dep.sqldelight.ios)
                implementation(dep.napier)
                implementation(dep.settings)
            }
        }
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 30
    }
}