package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.properties.Property

open class DebugScreen(var propertyList: List<Property>?) {
    fun show() {}
}

fun createDebugScreen(propertyList: List<Property>?, kvStorage: KVStorage? = null): DebugScreen {
    return DebugScreen(propertyList)
}
