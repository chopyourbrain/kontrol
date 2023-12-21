package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellMeta
import platform.UIKit.UITableViewCellStyle

@OptIn(BetaInteropApi::class)
@ExportObjCClass
internal class TitleCell @OverrideInit constructor(
    style: UITableViewCellStyle,
    reuseIdentifier: String? = null
) : UITableViewCell(
    style,
    reuseIdentifier
) {
    val title = UILabel()

    init {
        contentView.addSubview(title)
        title.setTranslatesAutoresizingMaskIntoConstraints(false)
        title.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        title.topAnchor.constraintEqualToAnchor(topAnchor).setActive(true)
        title.bottomAnchor.constraintEqualToAnchor(bottomAnchor).setActive(true)
        title.font = UIFont.systemFontOfSize(28.0)
    }

    companion object Meta : UITableViewCellMeta()
}
