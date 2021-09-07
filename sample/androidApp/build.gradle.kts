plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "io.chopyourbrain.kontrol.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        dataBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":kontrol"))
    implementation(project(":sample:shared"))

    implementation(dep.material)
    implementation(dep.androidx.appcompat)
    implementation(dep.androidx.layout.constraint)
    implementation(dep.androidx.lifecycle.runtime)
    implementation(dep.ktor.client.okhttp)
    implementation(dep.kotlinx.coroutines.android)
    implementation(dep.napier)
    implementation(dep.settings)
    implementation(dep.kotlinx.atomicfu)
    implementation(dep.okhttp.core)
}
