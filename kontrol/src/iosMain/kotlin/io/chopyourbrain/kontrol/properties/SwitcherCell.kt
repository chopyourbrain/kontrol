package io.chopyourbrain.kontrol.properties

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UILabel
import platform.UIKit.UISwitch
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellMeta
import platform.UIKit.UITableViewCellStyle

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
@ExportObjCClass
internal class SwitcherCell @OverrideInit constructor(
    style: UITableViewCellStyle,
    reuseIdentifier: String? = null
) : UITableViewCell(
    style,
    reuseIdentifier
) {
    val title = UILabel()
    val switcher = UISwitch()
    var switcherProperty: SwitcherProperty? = null

    init {
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
