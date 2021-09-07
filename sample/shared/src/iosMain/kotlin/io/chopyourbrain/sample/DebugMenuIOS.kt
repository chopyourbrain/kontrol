package io.chopyourbrain.sample

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import io.chopyourbrain.kontrol.DatabaseDriverFactory
import io.chopyourbrain.kontrol.kontrolIOSInstall
import io.chopyourbrain.kontrol.ktor.DetailLevel
import io.chopyourbrain.kontrol.ktor.KontrolKtorInterceptor
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
