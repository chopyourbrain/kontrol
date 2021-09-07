package com.chopyourbrain.kontrol.properties

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectBase
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*
import platform.darwin.NSInteger

@ExportObjCClass
internal class PickerCell : UITableViewCell, UIPickerViewDataSourceProtocol, UIPickerViewDelegateProtocol {
    val title = UILabel()
    val picker = UIPickerView()
    val textField = UITextField()
    var dropDownProperty: DropDownProperty? = null

    @ObjCObjectBase.OverrideInit
    constructor(style: UITableViewCellStyle, reuseIdentifier: String? = null) : super(
        style,
        reuseIdentifier
    ) {
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

    override fun pickerView(pickerView: UIPickerView, numberOfRowsInComponent: NSInteger): NSInteger {
        return dropDownProperty?.valueList?.size?.toLong() ?: 0
    }

    override fun pickerView(pickerView: UIPickerView, titleForRow: NSInteger, forComponent: NSInteger): String? {
        return dropDownProperty?.valueList?.get(titleForRow.toInt())
    }

    companion object Meta : UITableViewCellMeta()
}
