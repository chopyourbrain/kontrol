package io.chopyourbrain.kontrol.network

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExportObjCClass
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellMeta
import platform.UIKit.UITableViewCellStyle

@OptIn(BetaInteropApi::class)
@ExportObjCClass
internal class NetworkCell @OverrideInit constructor(
    style: UITableViewCellStyle,
    reuseIdentifier: String? = null
) : UITableViewCell(
    style,
    reuseIdentifier
) {
    val code = UILabel()
    val time = UILabel()
    val method = UILabel()
    val url = UILabel()

    init {
        contentView.addSubview(code)
        contentView.addSubview(time)
        contentView.addSubview(method)
        contentView.addSubview(url)
        code.setTranslatesAutoresizingMaskIntoConstraints(false)
        time.setTranslatesAutoresizingMaskIntoConstraints(false)
        method.setTranslatesAutoresizingMaskIntoConstraints(false)
        url.setTranslatesAutoresizingMaskIntoConstraints(false)
        url.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        url.rightAnchor.constraintEqualToAnchor(rightAnchor, -10.0).setActive(true)
        url.topAnchor.constraintEqualToAnchor(topAnchor, 5.0).setActive(true)
        code.leftAnchor.constraintEqualToAnchor(leftAnchor, 10.0).setActive(true)
        code.topAnchor.constraintEqualToAnchor(url.bottomAnchor, 5.0).setActive(true)
        method.leftAnchor.constraintEqualToAnchor(code.rightAnchor, 10.0).setActive(true)
        method.topAnchor.constraintEqualToAnchor(url.bottomAnchor, 5.0).setActive(true)
        time.leftAnchor.constraintEqualToAnchor(method.rightAnchor, 10.0).setActive(true)
        time.topAnchor.constraintEqualToAnchor(url.bottomAnchor, 5.0).setActive(true)
        code.font = UIFont.systemFontOfSize(14.0)
        time.font = UIFont.systemFontOfSize(14.0)
        method.font = UIFont.systemFontOfSize(14.0)
        url.font = UIFont.systemFontOfSize(14.0)
    }

    companion object Meta : UITableViewCellMeta()
}
