package io.chopyourbrain.kontrol.ktor

import io.chopyourbrain.kontrol.ServiceLocator
import io.chopyourbrain.kontrol.database.AppDatabase
import io.chopyourbrain.kontrol.repository.DBRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.observer.ResponseHandler
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.date.getTimeMillis
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.charsets.Charsets
import kotlinx.atomicfu.atomic

class KontrolKtorInterceptor(val level: DetailLevel) {

    private suspend fun processRequest(pipeline: PipelineContext<Any, HttpRequestBuilder>) {
        val id = pipeline.context.attributes[requestIdKey]

        val entryBody = RequestBodyEntry()
        val entry = RequestEntry(timestamp = getTimeMillis(), body = entryBody)

        val content = pipeline.context.body as? OutgoingContent?

        if (level.info) {
            entry.url = Url(pipeline.context.url)
            entry.method = pipeline.context.method
        }

        if (level.headers) {
            val headers = mutableMapOf<String, String>()

            val requestHeaders = joinHeaders(pipeline.context.headers.entries())

            val lengthHeader =
                content?.contentLength?.let { HttpHeaders.ContentLength to it.toString() }
            val typeHeader = content?.contentType?.let { HttpHeaders.ContentType to it.toString() }

            val contentHeaders = content?.headers?.entries()?.let { joinHeaders(it) }

            headers += requestHeaders
            if (lengthHeader != null) headers += lengthHeader
            if (typeHeader != null) headers += typeHeader
            if (contentHeaders != null) headers += contentHeaders

            entry.headers = headers
        }

        val observedContent = if (level.body) {
            val charset = content?.contentType?.charset() ?: Charsets.UTF_8
            val contentType = content?.contentType?.toString()
            val bodyChannel = ByteChannel()

            entryBody.charset = charset
            entryBody.contentType = contentType
            entryBody.bodyChannel = bodyChannel

            content?.observe(bodyChannel)
        } else null

        runCatching {
            pipeline.proceedWith(observedContent ?: pipeline.subject)
        }.onFailure {
            entry.error = it
            consumer.saveRequest(id, entry)
            //throw?
        }.onSuccess {
            consumer.saveRequest(id, entry)
        }
    }

    private suspend fun processResponse(pipeline: PipelineContext<HttpResponseContainer, HttpClientCall>) {
        val id = pipeline.context.attributes[requestIdKey]

        val entry = ResponseEntry()

        if (level.info) {
            entry.status = pipeline.context.response.status
            entry.method = pipeline.context.response.call.request.method
            entry.url = pipeline.context.response.call.request.url
        }

        if (level.headers) {
            entry.headers = joinHeaders(pipeline.context.response.headers.entries())
        }

        consumer.saveResponse(id, entry)
        runCatching {
            pipeline.proceed()
        }.onFailure {
            consumer.saveResponse(id, it)
            throw it
        }
    }

    private suspend fun processResponse(response: HttpResponse) {
        val id = response.call.attributes[requestIdKey]

        val charset = response.contentType()?.charset() ?: Charsets.UTF_8
        val contentType = response.contentType()?.toString()

        val entry = ResponseBodyEntry(
            response.bodyAsChannel().tryReadText(charset),
            contentType,
            charset
        )

        consumer.saveResponse(id, entry)
    }

    companion object : HttpClientPlugin<Config, KontrolKtorInterceptor> {
        private val requestIdKey = AttributeKey<Long>("RequestID")
        private val requestId = atomic(1000L)

        override val key: AttributeKey<KontrolKtorInterceptor> = AttributeKey("HttpLogging")

        override fun prepare(block: Config.() -> Unit): KontrolKtorInterceptor {
            val config = Config().apply(block)
            val databaseDriverFactory = config.databaseDriverFactory
                ?: throw IllegalStateException("Init databaseDriverFactory")
            ServiceLocator.DBRepository.value =
                DBRepository(AppDatabase(databaseDriverFactory.createDriver()))
            requestId.value =
                ServiceLocator.DBRepository.value?.getLastRequestId()?.plus(1L) ?: 1000
            return KontrolKtorInterceptor(config.level)
        }

        @OptIn(InternalAPI::class)
        override fun install(plugin: KontrolKtorInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                context.attributes.put(requestIdKey, requestId.getAndIncrement())
            }

            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                plugin.processRequest(this)
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                plugin.processResponse(this)
            }

            if (plugin.level.body) {
                val observer: ResponseHandler = { response -> plugin.processResponse(response) }
                ResponseObserver.prepare { onResponse(observer) }.install(scope)
            }
        }
    }

    class Config {
        var level: DetailLevel = DetailLevel.ALL
        var databaseDriverFactory: io.chopyourbrain.kontrol.DatabaseDriverFactory? = null
    }
}

private fun joinHeaders(entries: Set<Map.Entry<String, List<String>>>): Map<String, String> {
    return entries.sortedBy { it.key }.map {
        it.key to it.value.joinToString("; ")
    }.toMap()
}
