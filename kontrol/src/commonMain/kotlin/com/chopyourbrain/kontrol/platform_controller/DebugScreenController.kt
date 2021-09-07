package com.chopyourbrain.kontrol.platform_controller

import com.chopyourbrain.kontrol.DebugScreen
import com.chopyourbrain.kontrol.ServiceLocator

internal class DebugScreenController(private val debugScreenPlatformController: com.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController) {

    fun show(debugScreen: DebugScreen) {
        ServiceLocator.MainDebugScreen.value = debugScreen
        debugScreenPlatformController.show()
    }
}
