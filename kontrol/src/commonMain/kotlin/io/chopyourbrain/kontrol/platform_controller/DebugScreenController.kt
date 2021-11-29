package io.chopyourbrain.kontrol.platform_controller

import io.chopyourbrain.kontrol.DebugScreen
import io.chopyourbrain.kontrol.ServiceLocator

internal class DebugScreenController(private val debugScreenPlatformController: DebugScreenPlatformController) {

    fun show(debugScreen: DebugScreen) {
        ServiceLocator.MainDebugScreen.value = debugScreen
        debugScreenPlatformController.show()
    }
}
