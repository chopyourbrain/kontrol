package com.chopyourbrain.kontrol.properties

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCObjectBase
import platform.UIKit.*

@ExportObjCClass
internal class TitleCell : UITableViewCell {
    val title = UILabel()

    @ObjCObjectBase.OverrideInit
    constructor(style: UITableViewCellStyle, reuseIdentifier: String? = null) : super(
        style,
        reuseIdentifier
    ) {
        contentView.addSubview(title)
        title.setTranslatesAutoresizingMaskIntoConstraints(false)
        title.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        title.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        title.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        title.font = UIFont.systemFontOfSize(28.0)
    }

    companion object Meta : UITableViewCellMeta()
}
