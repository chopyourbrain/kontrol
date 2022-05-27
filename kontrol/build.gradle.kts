plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("convention.publication")
}

version = "0.1.0"
group = "io.github.chopyourbrain"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(dep.ktor.core.common)
                implementation(dep.sqldelight.common)
                implementation(dep.kotlinx.coroutines.core)
                implementation(dep.kotlinx.atomicfu)
                implementation(dep.klock.core)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(dep.androidx.appcompat)
                implementation(dep.androidx.core.core)
                implementation(dep.androidx.core.ktx)
                implementation(dep.androidx.layout.constraint)
                implementation(dep.androidx.lifecycle.runtime)
                implementation(dep.androidx.recycler.view)
                implementation(dep.ktor.client.okhttp)
                implementation(dep.sqldelight.android)
                implementation(dep.androidx.pager2)
                implementation(dep.material)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(dep.ktor.client.ios)
                implementation(dep.sqldelight.ios)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    compileSdk = sdk.compile
    defaultConfig {
        minSdk = sdk.min
        targetSdk = sdk.target
    }
    buildFeatures {
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = sdk.java
        targetCompatibility = sdk.java
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

sqldelight {
    database("AppDatabase") {
        packageName = "io.chopyourbrain.kontrol.database"
        sourceFolders = listOf("sqldelight")
        schemaOutputDirectory = file("build/dbs")
        dialect = "sqlite:3.24"
    }
}
