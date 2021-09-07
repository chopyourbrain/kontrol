package io.chopyourbrain.kontrol.ktor

import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*

interface Entry

class NetCallEntry(
    val requestID: Long,
    var request: RequestEntry? = null,
    var response: ResponseEntry? = null
) {
    val isComplete: Boolean; get() = request?.error != null || response?.body != null || response?.error != null
}

class RequestEntry(
    val timestamp: Long,
    var body: RequestBodyEntry,
    var url: Url? = null,
    var method: HttpMethod? = null,
    var headers: Map<String, String>? = null,
    var error: Throwable? = null
) : Entry

class RequestBodyEntry(
    var bodyChannel: ByteChannel? = null,
    var contentType: String? = null,
    var charset: Charset = Charsets.UTF_8
) : Entry

class ResponseEntry(
    var status: HttpStatusCode? = null,
    var method: HttpMethod? = null,
    var url: Url? = null,
    var headers: Map<String, String>? = null,
    var error: Throwable? = null,
    var body: ResponseBodyEntry? = null
) : Entry

class ResponseBodyEntry(
    var content: String? = null,
    var contentType: String? = null,
    var charset: Charset = Charsets.UTF_8
) : Entry

fun Throwable.toError() = NetCallError(toString(), stackTraceToString())
