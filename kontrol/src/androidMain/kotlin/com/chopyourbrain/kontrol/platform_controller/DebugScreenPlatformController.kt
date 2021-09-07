package com.chopyourbrain.kontrol.platform_controller

import android.content.Context

internal actual interface DebugScreenPlatformController {

    actual fun show()

    companion object {
        operator fun invoke(applicationContext: Context): com.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController {
            return DebugScreenPlatformControllerImpl(applicationContext)
        }
    }
}