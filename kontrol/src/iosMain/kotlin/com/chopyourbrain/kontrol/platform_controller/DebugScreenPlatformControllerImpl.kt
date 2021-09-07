package com.chopyourbrain.kontrol.platform_controller

import com.chopyourbrain.kontrol.DebugMenuTabViewController
import platform.UIKit.UINavigationController

internal class DebugScreenPlatformControllerImpl(
    private val navigationController: UINavigationController
) : DebugScreenPlatformController {

    override fun show() {
        val debugViewController = DebugMenuTabViewController()
        debugViewController.modalPresentationStyle = 0
        navigationController.pushViewController(debugViewController, true)
    }
}
