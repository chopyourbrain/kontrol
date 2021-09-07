package com.chopyourbrain.kontrol.network.detail

import com.chopyourbrain.kontrol.repository.getCallById
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCObjectBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import platform.Foundation.NSBundle
import platform.UIKit.*

@ExportObjCClass
internal class NetworkDetailViewController @ObjCObjectBase.OverrideInit constructor(
    nibName: String? = null,
    bundle: NSBundle? = null
) : UIViewController(nibName, bundle) {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    var callId: Long? = null

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        view.backgroundColor = UIColor.systemBackgroundColor
        val textView = UITextView()
        view.addSubview(textView)
        textView.apply {
            editable = false
            setTranslatesAutoresizingMaskIntoConstraints(false)
            centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).setActive(true)
            widthAnchor.constraintEqualToAnchor(view.widthAnchor).setActive(true)
            topAnchor.constraintEqualToAnchor(view.topAnchor).setActive(true)
            bottomAnchor.constraintEqualToAnchor(view.bottomAnchor).setActive(true)
            textView.font = UIFont.systemFontOfSize(14.0)
            scope.launch {
                callId?.let {
                    val call = getCallById(it)
                    textView.text = call.toString()
                }
            }
        }
    }
}
