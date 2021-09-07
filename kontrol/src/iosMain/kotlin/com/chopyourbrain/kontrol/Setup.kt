package com.chopyourbrain.kontrol

import com.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformControllerImpl
import platform.UIKit.UINavigationController

fun kontrolIOSInstall(navigationController: UINavigationController) {
    if (ServiceLocator.DebugScreenPlatformController.value != null) throw IllegalStateException("Double initialization detected")
    ServiceLocator.DebugScreenPlatformController.value = DebugScreenPlatformControllerImpl(navigationController)
}
