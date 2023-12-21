package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import platform.UIKit.UILabel
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellMeta
import platform.UIKit.UITableViewCellStyle

@OptIn(BetaInteropApi::class)
@ExportObjCClass
internal class TextCell @OverrideInit constructor(
    style: UITableViewCellStyle,
    reuseIdentifier: String? = null
) : UITableViewCell(
    style,
    reuseIdentifier
) {
    val title = UILabel()
    val value = UILabel()

    init {
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
