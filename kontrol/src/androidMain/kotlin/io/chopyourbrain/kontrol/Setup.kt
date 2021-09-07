package io.chopyourbrain.kontrol

import android.content.Context
import io.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController

fun kontrolAndroidInstall(context: Context) {
    if (ServiceLocator.DebugScreenPlatformController.value != null) throw IllegalStateException("Double initialization detected")
    ServiceLocator.DebugScreenPlatformController.value = DebugScreenPlatformController.invoke(context)
}
