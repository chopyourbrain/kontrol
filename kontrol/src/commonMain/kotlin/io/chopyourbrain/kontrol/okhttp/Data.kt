package io.chopyourbrain.kontrol.okhttp

import io.chopyourbrain.kontrol.ktor.NetCallError
import io.ktor.utils.io.charsets.*

interface Entry

class OkhttpNetCallEntry(
    val requestID: Long,
    var request: OkhttpRequestEntry? = null,
    var response: OkhttpResponseEntry? = null
) {
    val isComplete: Boolean; get() = request?.error != null || response?.body != null || response?.error != null
}

class OkhttpRequestEntry(
    val timestamp: Long,
    var body: OkhttpRequestBodyEntry,
    var url: String? = null,
    var method: String? = null,
    var headers: Map<String, String>? = null,
    var error: Throwable? = null
) : Entry

class OkhttpRequestBodyEntry(
    var body: String? = null,
    var contentType: String? = null,
    var charset: Charset = Charsets.UTF_8
) : Entry

class OkhttpResponseEntry(
    var status: Int? = null,
    var method: String? = null,
    var url: String? = null,
    var headers: Map<String, String>? = null,
    var error: Throwable? = null,
    var body: OkhttpResponseBodyEntry? = null
) : Entry

class OkhttpResponseBodyEntry(
    var content: String? = null,
    var contentType: String? = null,
    var charset: Charset = Charsets.UTF_8
) : Entry

fun Throwable.toError() = NetCallError(toString(), stackTraceToString())
