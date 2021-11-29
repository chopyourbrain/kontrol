package io.chopyourbrain.kontrol.properties

import io.chopyourbrain.kontrol.*
import io.chopyourbrain.kontrol.properties.*
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCObjectBase
import platform.Foundation.NSBundle
import platform.Foundation.NSIndexPath
import platform.UIKit.*
import platform.darwin.NSInteger
import platform.darwin.NSObject

@ExportObjCClass
internal class PropertiesViewController @ObjCObjectBase.OverrideInit constructor(
    nibName: String? = null,
    bundle: NSBundle? = null
) : UIViewController(nibName, bundle) {
    var table = UITableView()
    var properties: List<Property>? = null
    private val dataSource = PropertiesDataSource()
    private val delegate = TableViewDelegate()

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(table)
        table.apply {
            contentInset = UIEdgeInsetsMake(20.0, 0.0, 0.0, 0.0)
            setTranslatesAutoresizingMaskIntoConstraints(false)
            topAnchor.constraintEqualToAnchor(view.topAnchor).setActive(true)
            bottomAnchor.constraintEqualToAnchor(view.bottomAnchor).setActive(true)
            leftAnchor.constraintEqualToAnchor(view.leftAnchor).setActive(true)
            rightAnchor.constraintEqualToAnchor(view.rightAnchor).setActive(true)
            separatorStyle = UITableViewCellSeparatorStyle.UITableViewCellSeparatorStyleNone
            properties = ServiceLocator.MainDebugScreen.value.propertyList
            this@PropertiesViewController.dataSource.properties = properties
            dataSource = this@PropertiesViewController.dataSource
            delegate = this@PropertiesViewController.delegate
            registerClass(cellClass = SwitcherCell, forCellReuseIdentifier = "switcher_cell")
            registerClass(cellClass = TitleCell, forCellReuseIdentifier = "title_cell")
            registerClass(cellClass = TextCell, forCellReuseIdentifier = "text_cell")
            registerClass(cellClass = ButtonCell, forCellReuseIdentifier = "button_cell")
            registerClass(cellClass = PickerCell, forCellReuseIdentifier = "picker_cell")
        }
    }

}

internal class PropertiesDataSource : NSObject(), UITableViewDataSourceProtocol {
    var properties: List<Property>? = null

    override fun numberOfSectionsInTableView(tableView: UITableView): NSInteger {
        return 1
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger {
        return properties?.size?.toLong() ?: 0
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
        val item = properties?.get(cellForRowAtIndexPath.row.toInt())
        if (item is TitleProperty) {
            val cell = tableView.dequeueReusableCellWithIdentifier("title_cell", cellForRowAtIndexPath) as? TitleCell
            if (cell != null) {
                cell.apply {
                    selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                    title.text = item.title
                }
                return cell
            }
            return UITableViewCell()
        }
        if (item is SwitcherProperty) {
            val cell = tableView.dequeueReusableCellWithIdentifier("switcher_cell", cellForRowAtIndexPath) as? SwitcherCell
            if (cell != null) {
                cell.apply {
                    selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                    title.text = item.description
                    switcher.setOn(item.isEnabled.value)
                    switcherProperty = item
                }
                return cell
            }
            return UITableViewCell()
        }
        if (item is TextProperty) {
            val cell = tableView.dequeueReusableCellWithIdentifier("text_cell", cellForRowAtIndexPath) as? TextCell
            if (cell != null) {
                cell.apply {
                    selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                    title.text = item.description
                    value.text = item.value
                }
                return cell
            }
            return UITableViewCell()
        }
        if (item is ButtonProperty) {
            val cell = tableView.dequeueReusableCellWithIdentifier("button_cell", cellForRowAtIndexPath) as? ButtonCell
            if (cell != null) {
                cell.apply {
                    buttonProperty = item
                    selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                    button.setTitle(item.text, UIControlStateNormal)
                }
                return cell
            }
            return UITableViewCell()
        }
        if (item is DropDownProperty) {
            val cell = tableView.dequeueReusableCellWithIdentifier("picker_cell", cellForRowAtIndexPath) as? PickerCell
            if (cell != null) {
                cell.apply {
                    selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleNone
                    title.text = item.description
                    textField.text = item.currentValue.value
                    dropDownProperty = item
                    picker.reloadAllComponents()
                    picker.selectRow(
                        dropDownProperty?.valueList?.indexOf(dropDownProperty?.currentValue?.value)?.toLong() ?: 0,
                        0,
                        false
                    )
                }
                return cell
            }
            return UITableViewCell()
        }
        return UITableViewCell()
    }
}

internal class TableViewDelegate : NSObject(), UITableViewDelegateProtocol
