buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${sdk.kotlin}")
        classpath("com.android.tools.build:gradle:${ver.plugin.android_build_tools}")
        classpath("com.squareup.sqldelight:gradle-plugin:${ver.sqldelight}")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.16.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

