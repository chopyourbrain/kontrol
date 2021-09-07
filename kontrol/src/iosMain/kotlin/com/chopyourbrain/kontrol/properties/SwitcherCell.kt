package com.chopyourbrain.kontrol.properties

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectBase
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*

@ExportObjCClass
internal class SwitcherCell : UITableViewCell {
    val title = UILabel()
    val switcher = UISwitch()
    var switcherProperty: SwitcherProperty? = null

    @ObjCObjectBase.OverrideInit
    constructor(style: UITableViewCellStyle, reuseIdentifier: String? = null) : super(
        style,
        reuseIdentifier
    ) {
        contentView.addSubview(title)
        contentView.addSubview(switcher)
        title.setTranslatesAutoresizingMaskIntoConstraints(false)
        title.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        title.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        title.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        switcher.setTranslatesAutoresizingMaskIntoConstraints(false)
        switcher.centerYAnchor.constraintEqualToAnchor(contentView.centerYAnchor).setActive(true)
        switcher.rightAnchor.constraintEqualToAnchor(rightAnchor, -10.0).setActive(true)
        switcher.addTarget(this, NSSelectorFromString(::onSwitchChanged.name), UIControlEventValueChanged)
    }

    @ObjCAction
    fun onSwitchChanged() {
        switcherProperty?.onCheckedChangeListener?.invoke(switcher.on)
    }

    companion object Meta : UITableViewCellMeta()
}
