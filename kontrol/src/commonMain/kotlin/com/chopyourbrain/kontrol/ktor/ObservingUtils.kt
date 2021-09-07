package com.chopyourbrain.kontrol.ktor

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

suspend fun OutgoingContent.observe(log: ByteWriteChannel): OutgoingContent = when (this) {
    is OutgoingContent.ByteArrayContent -> {
        log.writeFully(bytes())
        log.close()
        this
    }
    is OutgoingContent.ReadChannelContent -> {
        val responseChannel = ByteChannel()
        val content = readFrom()

        content.copyToBoth(log, responseChannel)
        LoggedContent(this, responseChannel)
    }
    is OutgoingContent.WriteChannelContent -> {
        val responseChannel = ByteChannel()
        val content = toReadChannel()
        content.copyToBoth(log, responseChannel)
        LoggedContent(this, responseChannel)
    }
    else -> {
        log.close()
        this
    }
}

private class LoggedContent(
    private val originalContent: OutgoingContent,
    private val channel: ByteReadChannel
) : OutgoingContent.ReadChannelContent() {
    override val contentType: ContentType? = originalContent.contentType
    override val contentLength: Long? = originalContent.contentLength
    override val status: HttpStatusCode? = originalContent.status
    override val headers: Headers = originalContent.headers

    override fun <T : Any> getProperty(key: AttributeKey<T>): T? = originalContent.getProperty(key)

    override fun <T : Any> setProperty(key: AttributeKey<T>, value: T?) = originalContent.setProperty(key, value)

    override fun readFrom(): ByteReadChannel = channel
}

private fun OutgoingContent.WriteChannelContent.toReadChannel(): ByteReadChannel = GlobalScope.writer(Dispatchers.Unconfined) {
    writeTo(channel)
}.channel
