package com.chopyourbrain.kontrol.platform_controller

import android.content.Context
import com.chopyourbrain.kontrol.DebugMenuActivity

internal class DebugScreenPlatformControllerImpl(
    private val applicationContext: Context
) : com.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController {

    override fun show() {
        DebugMenuActivity.startActivity(applicationContext)
    }
}
