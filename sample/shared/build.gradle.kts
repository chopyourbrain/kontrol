import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
    }
    targets.filterIsInstance<KotlinNativeTarget>().forEach{
        it.binaries.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach { lib ->
                lib.isStatic = false
                lib.linkerOpts.add("-lsqlite3")
            }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":kontrol"))
            implementation(dep.ktor.core.common)
            implementation(dep.ktor.network)
            implementation(dep.napier)
            implementation(dep.settings)
            implementation(dep.kotlinx.atomicfu)
            implementation(dep.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(project(":kontrol"))
        }

        iosMain.dependencies {
            implementation(project(":kontrol"))
            implementation(dep.ktor.client.ios)
            implementation(dep.sqldelight.ios)
            implementation(dep.napier)
            implementation(dep.settings)
        }
    }
}

android {
    namespace = "io.chopyourbrain.kontrol.android"
    compileSdk = sdk.compile
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = sdk.java
        targetCompatibility = sdk.java
    }
}
