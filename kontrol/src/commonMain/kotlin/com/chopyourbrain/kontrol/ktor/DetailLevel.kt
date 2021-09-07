package com.chopyourbrain.kontrol.ktor

enum class DetailLevel(
    val info: Boolean,
    val headers: Boolean,
    val body: Boolean
) {
    ALL(true, true, true),
    HEADERS(true, true, false),
    BODY(true, false, true),
    INFO(true, false, false),
    NONE(false, false, false)
}
