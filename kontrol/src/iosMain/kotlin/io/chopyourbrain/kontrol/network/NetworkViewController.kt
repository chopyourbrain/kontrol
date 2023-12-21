package io.chopyourbrain.kontrol.network

import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.network.detail.NetworkDetailViewController
import io.chopyourbrain.kontrol.repository.getCallsList
import io.chopyourbrain.kontrol.timestampToString
import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import platform.Foundation.NSBundle
import platform.Foundation.NSIndexPath
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UINavigationController
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellSelectionStyle
import platform.UIKit.UITableViewCellSeparatorStyle
import platform.UIKit.UITableViewDataSourceProtocol
import platform.UIKit.UITableViewDelegateProtocol
import platform.UIKit.UIViewController
import platform.UIKit.navigationController
import platform.UIKit.row
import platform.darwin.NSInteger
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
@ExportObjCClass
internal class NetworkViewController @OverrideInit constructor(
    nibName: String? = null,
    bundle: NSBundle? = null
) : UIViewController(nibName, bundle) {
    private val dataSource = NetworkDataSource()
    private val delegate = NetworkViewDelegate()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        val table = UITableView()
        view.addSubview(table)
        table.apply {
            contentInset = UIEdgeInsetsMake(20.0, 0.0, 0.0, 0.0)
            setTranslatesAutoresizingMaskIntoConstraints(false)
            topAnchor.constraintEqualToAnchor(view.topAnchor).setActive(true)
            bottomAnchor.constraintEqualToAnchor(view.bottomAnchor).setActive(true)
            leftAnchor.constraintEqualToAnchor(view.leftAnchor).setActive(true)
            rightAnchor.constraintEqualToAnchor(view.rightAnchor).setActive(true)
            separatorStyle = UITableViewCellSeparatorStyle.UITableViewCellSeparatorStyleSingleLine
            scope.launch {
                val callList = getCallsList()
                Napier.d(callList.toString())
                this@NetworkViewController.dataSource.responseList = callList
                this@NetworkViewController.delegate.responseList = callList
                this@NetworkViewController.delegate.navigationController = navigationController
                dataSource = this@NetworkViewController.dataSource
                delegate = this@NetworkViewController.delegate
                registerClass(cellClass = NetworkCell, forCellReuseIdentifier = "network_cell")
                reloadData()
            }
        }
    }
}

internal class NetworkDataSource : NSObject(), UITableViewDataSourceProtocol {
    var responseList: List<NetCall>? = null

    override fun numberOfSectionsInTableView(tableView: UITableView): NSInteger {
        return 1
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger {
        return responseList?.size?.toLong() ?: 0
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
        val item = responseList?.get(cellForRowAtIndexPath.row.toInt())
        val cell = tableView.dequeueReusableCellWithIdentifier("network_cell", cellForRowAtIndexPath) as? NetworkCell
        if (cell != null) {
            cell.apply {
                selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                val itemCode = item?.response?.status?.toString() ?: "ERROR"
                method.text = item?.request?.method
                code.text = itemCode

                time.text = item?.timestamp.timestampToString()
                url.text = item?.request?.url
                when (itemCode.first().toString()) {
                    "2" -> code.textColor = UIColor(red = 0.0 / 255, green = 132.0 / 255, blue = 80.0 / 255, alpha = 1.0)
                    "3" -> code.textColor = UIColor(red = 239.0 / 255, green = 184.0 / 255, blue = 0.0 / 255, alpha = 1.0)
                    "4" -> code.textColor = UIColor(red = 184.0 / 255, green = 29.0 / 255, blue = 19.0 / 255, alpha = 1.0)
                    "E" -> code.textColor = UIColor(red = 184.0 / 255, green = 29.0 / 255, blue = 19.0 / 255, alpha = 1.0)
                }
            }
            return cell
        }
        return UITableViewCell()
    }
}

internal class NetworkViewDelegate : NSObject(), UITableViewDelegateProtocol {
    var responseList: List<NetCall>? = null
    var navigationController: UINavigationController? = null

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, didSelectRowAtIndexPath: NSIndexPath) {
        val networkDetailViewController = NetworkDetailViewController()
        networkDetailViewController.modalPresentationStyle = 0
        print(didSelectRowAtIndexPath.row)
        networkDetailViewController.callId = responseList?.get(didSelectRowAtIndexPath.row.toInt())?.id
        navigationController?.pushViewController(networkDetailViewController, true)
    }
}
