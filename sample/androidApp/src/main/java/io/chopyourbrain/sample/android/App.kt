package io.chopyourbrain.sample.android

import android.app.Application
import io.chopyourbrain.kontrol.DatabaseDriverFactory
import io.chopyourbrain.kontrol.kontrolAndroidInstall
import io.chopyourbrain.kontrol.ktor.DetailLevel
import io.chopyourbrain.kontrol.ktor.KontrolKtorInterceptor
import io.chopyourbrain.sample.HttpClientHolder
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val okhttp = OkHttp.create {}
        val httpClient = HttpClient(okhttp) {
            install(KontrolKtorInterceptor) {
                databaseDriverFactory = DatabaseDriverFactory(applicationContext)
                level = DetailLevel.ALL
            }
        }
        kontrolAndroidInstall(applicationContext)
        HttpClientHolder.httpClient.value = httpClient
    }
}
