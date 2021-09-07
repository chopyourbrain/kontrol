import UIKit
import shared

class MainViewController: UIViewController {
    
    lazy var buttonRouteToDebugMenu = UIButton()

    override func viewDidLoad() {
        self.view.addSubview(buttonRouteToDebugMenu)
        view.backgroundColor = UIColor.white
        buttonRouteToDebugMenu.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            buttonRouteToDebugMenu.leadingAnchor.constraint(equalTo: view.layoutMarginsGuide.leadingAnchor),
            buttonRouteToDebugMenu.topAnchor.constraint(equalTo: view.layoutMarginsGuide.topAnchor, constant: 10),
            buttonRouteToDebugMenu.trailingAnchor.constraint(equalTo: view.layoutMarginsGuide.trailingAnchor)
        ])
        buttonRouteToDebugMenu.backgroundColor = UIColor.systemBlue
        buttonRouteToDebugMenu.titleLabel?.font = UIFont.boldSystemFont(ofSize: 17)
        buttonRouteToDebugMenu.addTarget(self, action: #selector(didButtonDefaultMenuClick), for: .touchUpInside)
        buttonRouteToDebugMenu.setTitle("Go to debug menu", for: .normal)
        buttonRouteToDebugMenu.layer.cornerRadius = 20
        buttonRouteToDebugMenu.clipsToBounds = true
    }
    
    @objc func didButtonDefaultMenuClick() {
        let settings = DebugMenuIOS.init().createIOSSettings()
        DebugMenuRouter.init().routeToDebugMenu(settings: settings)
    }

}
