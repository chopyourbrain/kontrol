package io.chopyourbrain.kontrol.repository

import io.chopyourbrain.kontrol.ServiceLocator
import io.chopyourbrain.kontrol.database.AppDatabase
import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.ktor.NetCallEntry
import io.chopyourbrain.kontrol.ktor.NetCallError
import io.chopyourbrain.kontrol.ktor.Request
import io.chopyourbrain.kontrol.ktor.RequestBody
import io.chopyourbrain.kontrol.ktor.Response
import io.chopyourbrain.kontrol.ktor.ResponseBody
import io.chopyourbrain.kontrol.ktor.toError
import io.chopyourbrain.kontrol.ktor.tryReadText
import io.chopyourbrain.kontrol.okhttp.OkhttpNetCallEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DBRepository(private val database: AppDatabase) {

    suspend fun getCallList(): List<NetCall> {
        return withContext(Dispatchers.Default) {
            val calls = database.netCallQueries.selectAllCalls().executeAsList()
            return@withContext calls.map {
                val request = if (it.request == null) null
                else database.requestQueries.selectRequest(it.request).executeAsOneOrNull()

                val requestError = if (request?.error == null) null
                else database.requestErrorQueries.selectRequestError(request.error).executeAsOneOrNull()

                val response = if (it.response == null) null
                else database.responseQueries.selectResponse(it.response).executeAsOneOrNull()

                val responseError = if (response?.error == null) null
                else database.responseErrorQueries.selectResponseError(response.error).executeAsOneOrNull()

                val parsedRequest = if (request != null) Request(
                    request.timestamp,
                    request.url,
                    request.method,
                    request.headers.parseToStringMap(),
                    RequestBody(request.charset, request.contentType, request.body),
                    if (requestError != null) NetCallError(requestError.message, requestError.trace) else null
                ) else null

                val parsedResponse = if (response != null) Response(
                    response.status.toInt(),
                    response.url,
                    response.method,
                    response.headers.parseToStringMap(),
                    ResponseBody(response.charset, response.contentType, response.body),
                    if (responseError != null) NetCallError(responseError.message, responseError.trace) else null
                ) else null

                NetCall(it.request_id, it.timestamp, parsedRequest, parsedResponse)
            }.apply {  }
        }
    }

    suspend fun getCallById(id: Long): NetCall {
        return withContext(Dispatchers.Default) {
            val call = database.netCallQueries.selectCallById(id).executeAsOne()

            val request = if (call.request == null) null
            else database.requestQueries.selectRequest(call.request).executeAsOneOrNull()

            val requestError = if (request?.error == null) null
            else database.requestErrorQueries.selectRequestError(request.error).executeAsOneOrNull()

            val response = if (call.response == null) null
            else database.responseQueries.selectResponse(call.response).executeAsOneOrNull()

            val responseError = if (response?.error == null) null
            else database.responseErrorQueries.selectResponseError(response.error).executeAsOneOrNull()

            val parsedRequest = if (request != null) Request(
                request.timestamp,
                request.url,
                request.method,
                request.headers.parseToStringMap(),
                RequestBody(request.charset, request.contentType, request.body),
                if (requestError != null) NetCallError(requestError.message, requestError.trace) else null
            ) else null

            val parsedResponse = if (response != null) Response(
                response.status.toInt(),
                response.url,
                response.method,
                response.headers.parseToStringMap(),
                ResponseBody(response.charset, response.contentType, response.body),
                if (responseError != null) NetCallError(responseError.message, responseError.trace) else null
            ) else null

            return@withContext NetCall(call.request_id, call.timestamp, parsedRequest, parsedResponse)

        }
    }

    suspend fun save(call: NetCallEntry) {
        withContext(Dispatchers.Default) {
            val netCallQueries = database.netCallQueries
            val requestQueries = database.requestQueries
            val responseQueries = database.responseQueries
            val requestErrorQueries = database.requestErrorQueries
            val responseErrorQueries = database.responseErrorQueries

            netCallQueries.insertCall(
                requestID = call.requestID,
                timestamp = call.request?.timestamp ?: 0L,
                request = null,
                response = null
            )

            val callId = netCallQueries.selectIdByRequestId(call.requestID).executeAsOne()

            val requestData = call.request
            if (requestData != null) {
                val request = Request(
                    requestData.timestamp,
                    requestData.url.toString(),
                    requestData.method?.value ?: "",
                    requestData.headers,
                    RequestBody(
                        requestData.body.charset.toString(),
                        requestData.body.contentType,
                        requestData.body.bodyChannel?.tryReadText(requestData.body.charset)
                    )
                )

                requestQueries.insertRequest(
                    requestID = call.requestID,
                    timestamp = request.timestamp,
                    method = request.method,
                    url = request.url,
                    headers = request.headers.toString(),
                    charset = request.body?.charset ?: "",
                    contentType = request.body?.contentType ?: "",
                    body = request.body?.content ?: ""
                )

                val netCallError = requestData.error?.toError()
                if (netCallError != null) {
                    requestErrorQueries.insertRequestError(call.requestID, netCallError.message, netCallError.trace)
                    val requestErrorId = requestErrorQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    val requestId = requestQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    requestQueries.updateRequest(requestErrorId, requestId)
                }
            }

            val requestId = requestQueries.selectIdByRequestId(call.requestID).executeAsOne()
            netCallQueries.updateCallRequest(requestID = requestId, callID = callId)

            val responseData = call.response
            if (responseData != null) {
                val response = Response(
                    responseData.status?.value ?: 0,
                    responseData.url.toString(),
                    responseData.method?.value ?: "",
                    responseData.headers,
                    ResponseBody(
                        responseData.body?.charset.toString(),
                        responseData.body?.contentType,
                        responseData.body?.content
                    )
                )

                responseQueries.insertResponse(
                    requestID = call.requestID,
                    status = response.status.toLong(),
                    url = response.url,
                    method = response.method,
                    headers = response.headers.toString(),
                    charset = response.body?.charset ?: "",
                    contentType = response.body?.contentType ?: "",
                    body = response.body?.content ?: ""
                )

                val netCallError = responseData.error?.toError()
                if (netCallError != null) {
                    responseErrorQueries.insertRequestError(call.requestID, netCallError.message, netCallError.trace)
                    val responseErrorId = responseErrorQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    val responseId = responseQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    responseQueries.updateResponse(responseErrorId, responseId)
                }

                val responseId = responseQueries.selectIdByRequestId(call.requestID).executeAsOne()

                netCallQueries.updateCallResponse(responseId, callId)
            }
        }
    }

    suspend fun save(call: OkhttpNetCallEntry) {
        withContext(Dispatchers.Default) {
            val netCallQueries = database.netCallQueries
            val requestQueries = database.requestQueries
            val responseQueries = database.responseQueries
            val responseErrorQueries = database.responseErrorQueries

            netCallQueries.insertCall(
                requestID = call.requestID,
                timestamp = call.request?.timestamp ?: 0L,
                request = null,
                response = null
            )

            val callId = netCallQueries.selectIdByRequestId(call.requestID).executeAsOne()

            val requestData = call.request
            if (requestData != null) {
                val request = Request(
                    requestData.timestamp,
                    requestData.url.toString(),
                    requestData.method ?: "",
                    requestData.headers,
                    RequestBody(
                        requestData.body.charset.toString(),
                        requestData.body.contentType,
                        requestData.body.body
                    )
                )

                requestQueries.insertRequest(
                    requestID = call.requestID,
                    timestamp = request.timestamp,
                    method = request.method,
                    url = request.url,
                    headers = request.headers.toString(),
                    charset = request.body?.charset ?: "",
                    contentType = request.body?.contentType ?: "",
                    body = request.body?.content ?: ""
                )
            }

            val requestId = requestQueries.selectIdByRequestId(call.requestID).executeAsOne()
            netCallQueries.updateCallRequest(requestID = requestId, callID = callId)

            val responseData = call.response
            if (responseData != null) {
                val response = Response(
                    responseData.status ?: 0,
                    responseData.url.toString(),
                    responseData.method ?: "",
                    responseData.headers,
                    ResponseBody(
                        responseData.body?.charset.toString(),
                        responseData.body?.contentType,
                        responseData.body?.content
                    )
                )

                responseQueries.insertResponse(
                    requestID = call.requestID,
                    status = response.status.toLong(),
                    url = response.url,
                    method = response.method,
                    headers = response.headers.toString(),
                    charset = response.body?.charset ?: "",
                    contentType = response.body?.contentType ?: "",
                    body = response.body?.content ?: "",
                )

                val netCallError = responseData.error?.toError()
                if (netCallError != null) {
                    responseErrorQueries.insertRequestError(call.requestID, netCallError.message, netCallError.trace)
                    val responseErrorId = responseErrorQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    val responseId = responseQueries.selectIdByRequestId(call.requestID).executeAsOne()
                    responseQueries.updateResponse(responseErrorId, responseId)
                }

                val responseId = responseQueries.selectIdByRequestId(call.requestID).executeAsOne()

                netCallQueries.updateCallResponse(responseId, callId)
            }
        }
    }

    fun getLastRequestId(): Long? {
        return database.netCallQueries.selectLastRequestId().executeAsOneOrNull()
    }
}

internal suspend fun getCallsList(): List<NetCall> {
    val repository = requireNotNull(ServiceLocator.DBRepository.value)
    return repository.getCallList()
}

internal suspend fun getCallById(id: Long): NetCall {
    val repository = requireNotNull(ServiceLocator.DBRepository.value)
    return repository.getCallById(id)
}

fun String.parseToStringMap(): Map<String, String> {
    return this.replace("{", "")
        .replace("}", "")
        .split(",")
        .map { it.split("=") }
        .map { it.first().removePrefix(" ") to it.last() }
        .filter { it.first.isNotBlank() }
        .toMap()
}
