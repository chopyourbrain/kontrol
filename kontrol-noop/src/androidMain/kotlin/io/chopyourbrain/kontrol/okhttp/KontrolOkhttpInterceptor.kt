package io.chopyourbrain.kontrol.okhttp

import io.chopyourbrain.kontrol.DatabaseDriverFactory
import okhttp3.Interceptor
import okhttp3.Response

class KontrolOkhttpInterceptor(databaseDriverFactory: DatabaseDriverFactory) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}
