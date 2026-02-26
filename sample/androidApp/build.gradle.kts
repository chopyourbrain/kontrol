plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.chopyourbrain.sample.android"
    compileSdk = sdk.compile
    defaultConfig {
        applicationId = "io.chopyourbrain.kontrol.android"
        minSdk = sdk.min
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
