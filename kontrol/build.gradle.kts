plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
    id("com.vanniktech.maven.publish") version "0.36.0"
}

version = "1.0.0"
group = "io.github.chopyourbrain"

kotlin {
    androidTarget {
        publishLibraryVariants("release")

        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvmToolchain(21)
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(
        groupId = "io.github.chopyourbrain",
        artifactId = project.name,
        version = project.version.toString()
    )
    pom {
        name.set(project.name)
        description.set("KMP library ${project.name}")
        url.set("https://github.com/chopyourbrain/kontrol")
        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("chopyourbrain")
                name.set("Mikhail Kuznetsov")
                email.set("qtd130@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/chopyourbrain/kontrol")
            connection.set("scm:git:github.com/chopyourbrain/kontrol.git")
            developerConnection.set(
                "scm:git:ssh://git@github.com/chopyourbrain/kontrol.git"
            )
        }
    }
}
