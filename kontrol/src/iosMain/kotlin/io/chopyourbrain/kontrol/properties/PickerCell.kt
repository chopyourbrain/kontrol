package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIBarButtonSystemItem
import platform.UIKit.UIBarStyleDefault
import platform.UIKit.UIColor
import platform.UIKit.UILabel
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellMeta
import platform.UIKit.UITableViewCellStyle
import platform.UIKit.UITextField
import platform.UIKit.UIToolbar
import platform.UIKit.endEditing
import platform.UIKit.systemBlueColor
import platform.darwin.NSInteger

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
@ExportObjCClass
internal class PickerCell @OverrideInit constructor(
    style: UITableViewCellStyle,
    reuseIdentifier: String? = null
) : UITableViewCell(
    style,
    reuseIdentifier
), UIPickerViewDataSourceProtocol, UIPickerViewDelegateProtocol {
    val title = UILabel()
    val picker = UIPickerView()
    val textField = UITextField()
    var dropDownProperty: DropDownProperty? = null

    init {
        contentView.addSubview(title)
        contentView.addSubview(textField)
        title.setTranslatesAutoresizingMaskIntoConstraints(false)
        title.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        title.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        title.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        textField.setTranslatesAutoresizingMaskIntoConstraints(false)
        textField.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        textField.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        textField.rightAnchor.constraintEqualToAnchor(rightAnchor, -10.0).setActive(true)
        picker.dataSource = this
        picker.delegate = this
        val toolBar = UIToolbar()
        toolBar.barStyle = UIBarStyleDefault
        toolBar.translucent = true
        toolBar.tintColor = UIColor.systemBlueColor
        toolBar.sizeToFit()
        val doneButton = UIBarButtonItem(
            title = "Done",
            style = UIBarButtonItemStyle.UIBarButtonItemStyleDone,
            target = this,
            action = NSSelectorFromString(::onDoneTapped.name)
        )
        val spaceButton = UIBarButtonItem(
            barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemFlexibleSpace,
            target = null,
            action = null
        )
        val cancelButton = UIBarButtonItem(
            title = "Cancel",
            style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
            target = this,
            action = NSSelectorFromString(::onCancelTapped.name)
        )
        toolBar.setItems(listOf(cancelButton, spaceButton, doneButton), false)
        toolBar.userInteractionEnabled = true
        textField.inputView = picker
        textField.inputAccessoryView = toolBar
        picker.showsSelectionIndicator = true
        textField.tintColor = UIColor.clearColor
    }

    @ObjCAction
    fun onDoneTapped() {
        val pickedValue = dropDownProperty?.valueList?.get(picker.selectedRowInComponent(0).toInt())
        if (pickedValue != null) {
            textField.text = pickedValue
            dropDownProperty?.onDropDownStateChanged?.invoke(pickedValue)
        }
        textField.endEditing(true)
    }

    @ObjCAction
    fun onCancelTapped() {
        textField.endEditing(true)
    }

    override fun numberOfComponentsInPickerView(pickerView: UIPickerView): NSInteger {
        return 1
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun pickerView(pickerView: UIPickerView, numberOfRowsInComponent: NSInteger): NSInteger {
        return dropDownProperty?.valueList?.size?.toLong() ?: 0
    }

    @Suppress("CONFLICTING_OVERLOADS","RETURN_TYPE_MISMATCH_ON_OVERRIDE")
    override fun pickerView(pickerView: UIPickerView, titleForRow: NSInteger, forComponent: NSInteger): String? {
        return dropDownProperty?.valueList?.get(titleForRow.toInt())
    }

    companion object Meta : UITableViewCellMeta()
}
