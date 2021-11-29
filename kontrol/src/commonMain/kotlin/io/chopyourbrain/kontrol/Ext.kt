package io.chopyourbrain.kontrol

import com.soywiz.klock.DateTime

fun Long?.timestampToString(): String {
    return if (this == null) "" else DateTime(this).format("dd.MM.yyyy HH:mm:ss z")
}
