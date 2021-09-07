package io.chopyourbrain.kontrol.platform_controller

import android.content.Context
import io.chopyourbrain.kontrol.DebugMenuActivity

internal class DebugScreenPlatformControllerImpl(
    private val applicationContext: Context
) : DebugScreenPlatformController {

    override fun show() {
        DebugMenuActivity.startActivity(applicationContext)
    }
}
