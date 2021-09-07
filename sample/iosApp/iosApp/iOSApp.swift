import UIKit
import shared

@UIApplicationMain
class iOSApp: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        window = UIWindow(frame: UIScreen.main.bounds)
        let viewController = UINavigationController(rootViewController: MainViewController())
        DebugMenuIOS.init().doInitIOS(navigationController: viewController)
        NapierProxyKt.debugBuild()
        window?.rootViewController = viewController
        window?.makeKeyAndVisible()
        return true
    }
}
