@file:Suppress("ClassName", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

import org.gradle.api.JavaVersion

object sdk {
    const val min = 21
    const val target = 30
    const val compile = target

    val java = JavaVersion.VERSION_1_8
    const val kotlin = "1.6.21"

    const val ios = "10.0"
}

object ver {
    const val androidx_appcompat = "1.3.0"
    const val androidx_constraint = "2.0.4"
    const val androidx_core = "1.6.0-beta02"
    const val androidx_lifecycle = "2.3.1"
    const val androidx_pager2 = "1.0.0"
    const val androidx_recycler = "1.2.1"
    const val atomicfu = "0.17.2"
    const val coroutines = "1.6.0-native-mt"
    const val coroutines_android = "1.6.1"
    const val klock = "2.7.0"
    const val kotlin = sdk.kotlin
    const val ktor = "2.0.1"
    const val material = "1.4.0-rc01"
    const val napier = "2.6.1"
    const val okhttp = "4.9.1"
    const val settings = "0.9"
    const val stately = "1.1.1"
    const val sqldelight = "1.5.3"

    object plugin {
        const val android_build_tools = "7.2.0"
        const val versions = "0.39.0"
    }
}

object dep {
    const val material = "com.google.android.material:material:${ver.material}"
    const val napier = "io.github.aakira:napier:${ver.napier}"
    const val settings = "com.russhwolf:multiplatform-settings:${ver.settings}"
    const val stately = "co.touchlab:stately-common:${ver.stately}"

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
        private const val group = "com.squareup.sqldelight"

        const val common = "$group:runtime:${ver.sqldelight}"
        const val android = "$group:android-driver:${ver.sqldelight}"
        const val ios = "$group:native-driver:${ver.sqldelight}"
    }
}
