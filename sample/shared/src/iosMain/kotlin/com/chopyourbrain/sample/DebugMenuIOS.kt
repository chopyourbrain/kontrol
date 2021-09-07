package com.chopyourbrain.sample

import com.chopyourbrain.kontrol.DatabaseDriverFactory
import com.chopyourbrain.kontrol.kontrolIOSInstall
import com.chopyourbrain.kontrol.ktor.DetailLevel
import com.chopyourbrain.kontrol.ktor.KontrolKtorInterceptor
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.ios.*
import platform.UIKit.UINavigationController

object DebugMenuIOS {

    fun initIOS(navigationController: UINavigationController) {
        val httpClient = HttpClient(Ios.create { }) {
            install(KontrolKtorInterceptor) {
                databaseDriverFactory = DatabaseDriverFactory()
                level = DetailLevel.ALL
            }
        }
        kontrolIOSInstall(navigationController)
        HttpClientHolder.httpClient.value = httpClient
    }

    fun createIOSSettings(): Settings {
        return AppleSettings.Factory().create()
    }
}
