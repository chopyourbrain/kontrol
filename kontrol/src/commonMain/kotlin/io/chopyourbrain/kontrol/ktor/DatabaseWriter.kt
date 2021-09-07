package io.chopyourbrain.kontrol.ktor

import io.chopyourbrain.kontrol.ServiceLocator
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val consumer = DatabaseWriter()

class DatabaseWriter {
    private val poolMutex = Mutex()
    private val callPool = HashMap<Long, NetCallEntry>()

    suspend fun saveRequest(id: Long, request: RequestEntry) {
        poolMutex.withLock {
            val call = callPool.getOrPut(id) { NetCallEntry(id) }
            call.request = request

            if (call.isComplete) {
                callPool.remove(id)
                save(call)
            }
        }
    }

    suspend fun saveResponse(id: Long, response: ResponseEntry) {
        poolMutex.withLock {
            val call = callPool.getOrPut(id) { NetCallEntry(id) }
            call.response = response

            if (call.isComplete) {
                callPool.remove(id)
                save(call)
            }
        }
    }

    suspend fun saveResponse(id: Long, body: ResponseBodyEntry) {
        poolMutex.withLock {
            val call = callPool.getOrPut(id) { NetCallEntry(id) }
            val response = requireNotNull(call.response) { "Request $id response is lost!" }
            response.body = body

            if (call.isComplete) {
                callPool.remove(id)
                save(call)
            }
        }
    }

    suspend fun saveResponse(id: Long, error: Throwable) {
        poolMutex.withLock {
            val call = callPool.getOrPut(id) { NetCallEntry(id) }
            val response = requireNotNull(call.response) { "Request $id response is lost!" }
            response.error = error

            if (call.isComplete) {
                callPool.remove(id)
                save(call)
            }
        }
    }

    private suspend fun save(call: NetCallEntry) {
        ServiceLocator.DBRepository.value?.save(call)
    }
}
