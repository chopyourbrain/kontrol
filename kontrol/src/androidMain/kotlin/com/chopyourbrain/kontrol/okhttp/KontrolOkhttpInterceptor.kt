package com.chopyourbrain.kontrol.okhttp

import com.chopyourbrain.kontrol.DatabaseDriverFactory
import com.chopyourbrain.kontrol.ServiceLocator
import com.chopyourbrain.kontrol.database.AppDatabase
import com.chopyourbrain.kontrol.repository.DBRepository
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8

class KontrolOkhttpInterceptor(databaseDriverFactory: DatabaseDriverFactory) : Interceptor {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val requestId = atomic(2000L)

    init {
        ServiceLocator.DBRepository.value = DBRepository(AppDatabase(databaseDriverFactory.createDriver()))
        requestId.value = ServiceLocator.DBRepository.value?.getLastRequestId()?.plus(1L) ?: 1000
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestId = requestId.getAndIncrement()
        val request = chain.request()
        val requestBody = request.body
        val okhttpRequestBodyEntry = OkhttpRequestBodyEntry()

        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val contentType = requestBody.contentType()
            val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
            val body = buffer.readString(charset)
            okhttpRequestBodyEntry.apply {
                this.body = body
                this.charset = charset
                this.contentType = contentType.toString()
            }

        }

        val requestTimestamp = System.currentTimeMillis()

        val okhttpRequestEntry = OkhttpRequestEntry(
            timestamp = requestTimestamp,
            url = request.url.toString(),
            method = request.method,
            headers = request.headers.toMap(),
            body = okhttpRequestBodyEntry
        )

        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            val okhttpResponseEntry = OkhttpResponseEntry(
                error = e
            )

            saveCall(requestId, okhttpRequestEntry, okhttpResponseEntry)
            throw e
        }

        val responseBody = response.body
        val headers = response.headers
        val okhttpResponseBodyEntry = OkhttpResponseBodyEntry()

        if (response.promisesBody() && responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer

            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
            val body = buffer.clone().readString(charset)

            okhttpResponseBodyEntry.apply {
                this.charset = charset
                this.contentType = contentType.toString()
                this.content = body
            }
        }

        val okhttpResponseEntry = OkhttpResponseEntry(
            status = response.code,
            method = response.request.method,
            url = response.request.url.toString(),
            headers = response.headers.toMap(),
            body = okhttpResponseBodyEntry,
        )

        saveCall(requestId, okhttpRequestEntry, okhttpResponseEntry)

        return response
    }

    private fun saveCall(requestId: Long, okhttpRequestEntry: OkhttpRequestEntry, okhttpResponseEntry: OkhttpResponseEntry) {
        val okhttpNetCallEntry = OkhttpNetCallEntry(
            requestId,
            okhttpRequestEntry,
            okhttpResponseEntry
        )
        scope.launch {
            ServiceLocator.DBRepository.value?.save(okhttpNetCallEntry)
        }
    }
}
