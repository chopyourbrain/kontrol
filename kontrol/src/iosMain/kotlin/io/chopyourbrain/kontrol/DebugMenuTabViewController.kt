package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.network.NetworkViewController
import io.chopyourbrain.kontrol.properties.PropertiesViewController
import io.chopyourbrain.kontrol.properties.getPropertiesAsString
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectBase
import platform.Foundation.NSBundle
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*

@ExportObjCClass
internal class DebugMenuTabViewController @ObjCObjectBase.OverrideInit constructor(
    nibName: String? = null,
    bundle: NSBundle? = null
) : UITabBarController(nibName, bundle), UITabBarControllerDelegateProtocol {

    override fun viewDidLoad() {
        super.viewDidLoad()
        val controllerList = mutableListOf<UIViewController>()
        val debugViewController = PropertiesViewController()
        debugViewController.tabBarItem = UITabBarItem("Properties", UIImage.systemImageNamed("slider.horizontal.3"), 0L)
        controllerList.add(debugViewController)
        if (ServiceLocator.DBRepository.value != null) {
            val networkViewController = NetworkViewController()
            networkViewController.tabBarItem =
                UITabBarItem("Network", UIImage.systemImageNamed("antenna.radiowaves.left.and.right"), 1L)
            controllerList.add(networkViewController)
        }
        this.viewControllers = controllerList.toList()
        title = "Debug menu"
        navigationItem.setRightBarButtonItem(
            UIBarButtonItem(
                UIImage.systemImageNamed("square.and.arrow.up"),
                UIBarButtonItemStyle.UIBarButtonItemStylePlain,
                this,
                NSSelectorFromString(::onShareTapped.name)
            ), true
        )
    }

    @ObjCAction
    fun onShareTapped() {
        val text = getPropertiesAsString()
        val activityViewController = UIActivityViewController(listOf(text), null)
        activityViewController.popoverPresentationController?.sourceView = this.view
        this.presentViewController(activityViewController, true, null)
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tabBarController(tabBarController: UITabBarController, shouldSelectViewController: UIViewController): Boolean {
        return true
    }
}
