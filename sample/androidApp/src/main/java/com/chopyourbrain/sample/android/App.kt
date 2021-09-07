package com.chopyourbrain.sample.android

import android.app.Application
import com.chopyourbrain.kontrol.DatabaseDriverFactory
import com.chopyourbrain.kontrol.kontrolAndroidInstall
import com.chopyourbrain.kontrol.ktor.DetailLevel
import com.chopyourbrain.kontrol.ktor.KontrolKtorInterceptor
import com.chopyourbrain.sample.HttpClientHolder
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
