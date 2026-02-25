package io.chopyourbrain.kontrol.ktor

import io.chopyourbrain.kontrol.DatabaseDriverFactory
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.util.*

class KontrolKtorInterceptor {

    companion object : HttpClientPlugin<Config, KontrolKtorInterceptor> {

        override val key: AttributeKey<KontrolKtorInterceptor> = AttributeKey("HttpLogging")

        override fun prepare(block: Config.() -> Unit): KontrolKtorInterceptor = KontrolKtorInterceptor()

        override fun install(plugin: KontrolKtorInterceptor, scope: HttpClient) {}
    }

    class Config {
        var level: DetailLevel = DetailLevel.ALL
        var databaseDriverFactory: DatabaseDriverFactory? = null
    }
}
