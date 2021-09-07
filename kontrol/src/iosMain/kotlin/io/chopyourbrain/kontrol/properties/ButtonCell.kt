package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectBase
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*

@ExportObjCClass
internal class ButtonCell : UITableViewCell {
    val button = UIButton()
    var buttonProperty: ButtonProperty? = null

    @ObjCObjectBase.OverrideInit
    constructor(style: UITableViewCellStyle, reuseIdentifier: String? = null) : super(
        style,
        reuseIdentifier
    ) {
        contentView.addSubview(button)
        button.setTranslatesAutoresizingMaskIntoConstraints(false)
        button.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        button.topAnchor.constraintEqualToAnchor(topAnchor, 2.0).setActive(true)
        button.bottomAnchor.constraintEqualToAnchor(bottomAnchor, -2.0).setActive(true)
        button.rightAnchor.constraintEqualToAnchor(rightAnchor, -10.0).setActive(true)
        button.layer.cornerRadius = 20.0
        button.clipsToBounds = true
        button.titleLabel?.font = UIFont.boldSystemFontOfSize(17.0)
        button.backgroundColor = UIColor.systemBlueColor
        button.addTarget(this, NSSelectorFromString(::onButtonTapped.name), UIControlEventTouchUpInside)
    }

    @ObjCAction
    fun onButtonTapped() {
        buttonProperty?.onButtonClickListener?.invoke()
    }

    companion object Meta : UITableViewCellMeta()
}
