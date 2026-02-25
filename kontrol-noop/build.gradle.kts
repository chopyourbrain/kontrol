plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.publication")
}

version = "1.0.0"
group = "io.github.chopyourbrain"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(dep.ktor.core.common)
        }

        androidMain.dependencies {
            implementation(dep.okhttp.core)
        }

        tasks.withType<AbstractPublishToMaven>().configureEach {
            val signingTasks = tasks.withType<Sign>()
            mustRunAfter(signingTasks)
        }
    }
}

android {
    namespace = "io.chopyourbrain.kontrol.noop"
    compileSdk = sdk.compile
    defaultConfig {
        minSdk = sdk.min
    }
    compileOptions {
        sourceCompatibility = sdk.java
        targetCompatibility = sdk.java
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
