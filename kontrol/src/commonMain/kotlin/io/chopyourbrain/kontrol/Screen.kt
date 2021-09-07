package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.platform_controller.DebugScreenController
import io.chopyourbrain.kontrol.properties.Property

open class DebugScreen(
    var propertyList: List<Property>?
) {
    fun show() {
        val platformController = ServiceLocator.DebugScreenPlatformController.value
        if (platformController != null)
            DebugScreenController(platformController).show(this)
        else throw Exception("Use init function")
    }
}

fun createDebugScreen(propertyList: List<Property>?, kvStorage: KVStorage? = null): DebugScreen {
    ServiceLocator.KVStorage.value = kvStorage
    return DebugScreen(propertyList)
}
