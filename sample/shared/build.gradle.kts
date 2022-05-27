plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
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
                implementation(dep.kotlinx.atomicfu)
                implementation(dep.kotlinx.coroutines.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project(":kontrol"))
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(project(":kontrol"))
                implementation(dep.ktor.client.ios)
                implementation(dep.sqldelight.ios)
                implementation(dep.napier)
                implementation(dep.settings)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
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
    compileOptions {
        sourceCompatibility = sdk.java
        targetCompatibility = sdk.java
    }
}
