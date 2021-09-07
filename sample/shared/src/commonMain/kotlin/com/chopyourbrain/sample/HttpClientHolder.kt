package com.chopyourbrain.sample

import io.ktor.client.*
import kotlinx.atomicfu.atomic

object HttpClientHolder {
    val httpClient = atomic<HttpClient?>(null)
}
