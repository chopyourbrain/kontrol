package io.chopyourbrain.kontrol.ktor

data class NetCall(
    val id: Long,
    val timestamp: Long,
    val request: Request? = null,
    val response: Response? = null
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder().apply {
            append("URL: ${request?.url}")
            appendLine()
            append("Method: ${request?.method}")
            appendLine()
            append("Status: ${response?.status}")
            appendLine()
            append("Request time: ${request?.timestamp}")
            appendLine()
            append("Request headers: ")
            appendLine()
            request?.headers?.forEach {
                append("${it.key.removePrefix(" ")} = ${it.value}")
                appendLine()
            }
            appendLine()
            append("Request body: ${request?.body?.content}")
            appendLine()
            if (request?.error != null) {
                append("Request error message: ${request.error.message}")
                appendLine()
                append("Request error trace: ${request.error.trace}")
                appendLine()
            }
            append("Response headers: ")
            appendLine()
            response?.headers?.forEach {
                append("${it.key.removePrefix(" ")} = ${it.value}")
                appendLine()
            }
            appendLine()
            append("Response body: ${response?.body?.content}")
            if (response?.error != null) {
                append("Response error message: ${response.error.message}")
                appendLine()
                append("Response error trace: ${response.error.trace}")
                appendLine()
            }
        }
        return stringBuilder.toString()
    }
}

data class NetCallError(
    val message: String,
    val trace: String
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.apply {
            append("Message: $message")
            appendLine()
            append("Trace: $trace")
        }
        return stringBuilder.toString()
    }
}

data class Request(
    val timestamp: Long,
    val url: String,
    val method: String,
    val headers: Map<String, String>? = null,
    val body: RequestBody? = null,
    val error: NetCallError? = null
)

data class RequestBody(
    val charset: String,
    val contentType: String? = null,
    val content: String? = null,
    val error: NetCallError? = null
)

data class Response(
    val status: Int,
    val url: String,
    val method: String,
    val headers: Map<String, String>? = null,
    val body: ResponseBody? = null,
    val error: NetCallError? = null
)

data class ResponseBody(
    val charset: String,
    val contentType: String? = null,
    val content: String? = null
)
