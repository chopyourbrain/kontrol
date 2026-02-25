plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
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
            implementation(dep.sqldelight.common)
            implementation(dep.kotlinx.coroutines.core)
            implementation(dep.kotlinx.atomicfu)
            implementation(dep.klock.core)
            implementation(dep.napier)
        }

        androidMain.dependencies {
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

        iosMain.dependencies {
            implementation(dep.ktor.client.ios)
            implementation(dep.sqldelight.ios)
        }
        tasks.withType<AbstractPublishToMaven>().configureEach {
            val signingTasks = tasks.withType<Sign>()
            mustRunAfter(signingTasks)
        }
    }
}

android {
    namespace = "io.chopyourbrain.kontrol.android"
    compileSdk = sdk.compile
    defaultConfig {
        minSdk = sdk.min
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
    databases {
        linkSqlite.set(true)
        create("AppDatabase") {
            packageName.set("io.chopyourbrain.kontrol.database")
        }
    }
}
