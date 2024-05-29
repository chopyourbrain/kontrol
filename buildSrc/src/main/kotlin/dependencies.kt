@file:Suppress("ClassName", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

import org.gradle.api.JavaVersion

object sdk {
    const val min = 21
    const val target = 34
    const val compile = target

    val java = JavaVersion.VERSION_17
    const val kotlin = "1.9.21"

    const val ios = "10.0"
}

object ver {
    const val androidx_appcompat = "1.6.1"
    const val androidx_constraint = "2.1.4"
    const val androidx_core = "1.12.0"
    const val androidx_lifecycle = "2.6.2"
    const val androidx_pager2 = "1.0.0"
    const val androidx_recycler = "1.3.2"
    const val atomicfu = "0.23.1"
    const val coroutines = "1.7.3"
    const val coroutines_android = "1.7.3"
    const val klock = "4.0.10"
    const val kotlin = sdk.kotlin
    const val ktor = "3.0.0-beta-1"
    const val material = "1.9.0"
    const val napier = "2.6.1"
    const val okhttp = "4.12.0"
    const val settings = "1.1.1"
    const val sqldelight = "2.0.0"

    object plugin {
        const val android_build_tools = "8.2.0"
        const val versions = "0.39.0"
    }
}

object dep {
    const val material = "com.google.android.material:material:${ver.material}"
    const val napier = "io.github.aakira:napier:${ver.napier}"
    const val settings = "com.russhwolf:multiplatform-settings:${ver.settings}"

    object androidx {
        const val appcompat = "androidx.appcompat:appcompat:${ver.androidx_appcompat}"

        const val pager2 = "androidx.viewpager2:viewpager2:${ver.androidx_pager2}"

        object core {
            const val core = "androidx.core:core:${ver.androidx_core}"
            const val ktx = "androidx.core:core-ktx:${ver.androidx_core}"
        }

        object layout {
            const val constraint = "androidx.constraintlayout:constraintlayout:${ver.androidx_constraint}"
        }

        object lifecycle {
            const val common = "androidx.lifecycle:lifecycle-common-java8:${ver.androidx_lifecycle}"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${ver.androidx_lifecycle}"
        }

        object recycler {
            const val view = "androidx.recyclerview:recyclerview:${ver.androidx_recycler}"
        }
    }

    object klock {
        const val android = "com.soywiz.korlibs.klock:klock-android:${ver.klock}"
        const val core = "com.soywiz.korlibs.klock:klock:${ver.klock}"
    }

    object kotlinx {
        private const val group = "org.jetbrains.kotlinx"

        object coroutines {
            const val android = "$group:kotlinx-coroutines-android:${ver.coroutines_android}"
            const val core = "$group:kotlinx-coroutines-core:${ver.coroutines}"
            const val common = "$group:kotlinx-coroutines-core-common:${ver.coroutines}"
        }

        const val atomicfu = "$group:atomicfu:${ver.atomicfu}"
    }

    object ktor {
        private const val group = "io.ktor"

        const val network = "$group:ktor-network:${ver.ktor}"

        object client {
            const val ios = "$group:ktor-client-darwin:${ver.ktor}"
            const val okhttp = "$group:ktor-client-okhttp:${ver.ktor}"
        }

        object core {
            const val common = "$group:ktor-client-core:${ver.ktor}"
            const val jvm = "$group:ktor-client-core-jvm:${ver.ktor}"
        }
    }

    object okhttp {
        private const val group = "com.squareup.okhttp3"

        const val core = "$group:okhttp:${ver.okhttp}"
    }

    object sqldelight {
        private const val group = "app.cash.sqldelight"

        const val common = "$group:runtime:${ver.sqldelight}"
        const val android = "$group:android-driver:${ver.sqldelight}"
        const val ios = "$group:native-driver:${ver.sqldelight}"
    }
}
