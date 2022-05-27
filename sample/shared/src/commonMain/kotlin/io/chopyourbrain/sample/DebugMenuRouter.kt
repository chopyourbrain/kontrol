package io.chopyourbrain.sample

import com.russhwolf.settings.*
import io.chopyourbrain.kontrol.KVStorage
import io.chopyourbrain.kontrol.createDebugScreen
import io.chopyourbrain.kontrol.properties
import io.github.aakira.napier.Napier
import io.ktor.client.request.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object DebugMenuRouter {

    fun routeToDebugMenu(settings: Settings) {
        val httpClient = HttpClientHolder.httpClient.value
        if (httpClient != null) {
            MainScope().launch {
                runCatching { httpClient.post("https://reqres.in/api/users?page=2") }
                runCatching { httpClient.post("https://reqres.in/api/login") }
                runCatching { httpClient.post("foobar") }
                runCatching { httpClient.post("foobar.com/test") }
            }
            val properties = properties {
                group("Network") {
                    switcher("Enable log", true) {
                        Napier.v("Enable log is $it")
                    }
                    dropDown("Server", listOf("google.com", "amazon.com", "reddit.com"), "google.com") {
                        Napier.v("Server is $it")
                    }
                    text("Default error", "Main Exception")
                    button("Send request") {
                        Napier.v("Send request is pressed")
                    }
                }
                group("App") {
                    switcher("darkTheme", "Enable dark theme")
                    dropDown("version", "Version", listOf("1.0", "2.0", "3.0"))
                    text("Version code", "1.0")
                    button("Kill application") {
                        Napier.v("Kill application is pressed")
                    }
                }
                button("Clear cache") {
                    Napier.v("Clear cache is pressed")
                }
            }
            val kvStorage = object : KVStorage {
                override fun getBoolean(key: String): Boolean? {
                    return settings.getBooleanOrNull(key)
                }

                override fun getString(key: String): String? {
                    return settings.getStringOrNull(key)
                }

                override fun setBoolean(key: String, value: Boolean) {
                    settings[key] = value
                }

                override fun setString(key: String, value: String) {
                    settings[key] = value
                }

            }
            val debugScreen = createDebugScreen(properties, kvStorage)
            debugScreen.show()
        }
    }
}
