plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("maven-publish")
    id("kotlinx-atomicfu")
}

version = "0.0.1"
group = "com.chopyourbrain"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(dep.ktor.core.common)
                implementation(dep.sqldelight.common)
                implementation(dep.kotlinx.coroutines.core)
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
        val iosMain by getting {
            dependencies {
                implementation(dep.ktor.client.ios)
                implementation(dep.sqldelight.ios)
            }
        }
    }
}

android {
    compileSdkVersion(sdk.compile)
    defaultConfig {
        minSdkVersion(sdk.min)
        targetSdkVersion(sdk.target)
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
        packageName = "com.chopyourbrain.kontrol.database"
        sourceFolders = listOf("sqldelight")
        schemaOutputDirectory = file("build/dbs")
        dialect = "sqlite:3.24"
    }
}
