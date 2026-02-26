package io.chopyourbrain.kontrol.ktor

import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.close
import io.ktor.utils.io.core.copy
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.writePacket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

private const val CHUNK_BUFFER_SIZE = 4096L

/**
 * Split source [ByteReadChannel] into 2 new one.
 * Cancel of one channel in split(input or both outputs) cancels other channels.
 */
internal fun ByteReadChannel.split(coroutineScope: CoroutineScope): Pair<ByteReadChannel, ByteReadChannel> {
    val first = ByteChannel(autoFlush = true)
    val second = ByteChannel(autoFlush = true)

    coroutineScope.launch {
        try {
            while (!isClosedForRead) {
                this@split.readRemaining(CHUNK_BUFFER_SIZE).use { chunk ->
                    listOf(
                        async { first.writePacket(chunk.copy()) },
                        async { second.writePacket(chunk.copy()) }
                    ).awaitAll()
                }
            }

            if (this is ByteChannel) {
                closedCause?.let { throw it }
            }
        } catch (cause: Throwable) {
            this@split.cancel(cause)
            first.cancel(cause)
            second.cancel(cause)
        } finally {
            first.close()
            second.close()
        }
    }.invokeOnCompletion {
        it ?: return@invokeOnCompletion
        first.cancel(it)
        second.cancel(it)
    }

    return first to second
}

/**
 * Copy source channel to both output channels chunk by chunk.
 */

internal fun ByteReadChannel.copyToBoth(first: ByteWriteChannel, second: ByteWriteChannel) {
    GlobalScope.launch(Dispatchers.Unconfined) {
        try {
            while (!isClosedForRead && (!first.isClosedForWrite || !second.isClosedForWrite)) {
                readRemaining(CHUNK_BUFFER_SIZE).use {
                    try {
                        first.writePacket(it.copy())
                        second.writePacket(it.copy())
                    } catch (cause: Throwable) {
                        this@copyToBoth.cancel(cause)
                        first.close(cause)
                        second.close(cause)
                    }
                }
            }

            if (this is ByteChannel) {
                closedCause?.let { throw it }
            }
        } catch (cause: Throwable) {
            first.close(cause)
            second.close(cause)
        } finally {
            first.close()
            second.close()
        }
    }.invokeOnCompletion {
        it ?: return@invokeOnCompletion
        first.close(it)
        second.close(it)
    }
}

/**
 * Read channel to byte array.
 */
internal suspend fun ByteReadChannel.toByteArray(): ByteArray = readRemaining().readBytes()

internal suspend inline fun ByteReadChannel.tryReadText(charset: Charset): String? = try {
    readRemaining().readText(charset = charset)
} catch (cause: Throwable) {
    null
}
