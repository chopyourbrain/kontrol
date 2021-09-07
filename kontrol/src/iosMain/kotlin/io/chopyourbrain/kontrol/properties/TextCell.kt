package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCObjectBase
import platform.UIKit.*

@ExportObjCClass
internal class TextCell : UITableViewCell {
    val title = UILabel()
    val value = UILabel()

    @ObjCObjectBase.OverrideInit
    constructor(style: UITableViewCellStyle, reuseIdentifier: String? = null) : super(
        style,
        reuseIdentifier
    ) {
        contentView.addSubview(title)
        contentView.addSubview(value)
        title.setTranslatesAutoresizingMaskIntoConstraints(false)
        title.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        title.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        title.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        value.setTranslatesAutoresizingMaskIntoConstraints(false)
        value.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        value.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        value.rightAnchor.constraintEqualToAnchor(rightAnchor, -10.0).setActive(true)
    }

    companion object Meta : UITableViewCellMeta()
}
