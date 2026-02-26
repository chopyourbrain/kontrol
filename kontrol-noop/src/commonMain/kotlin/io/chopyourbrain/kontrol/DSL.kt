package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.properties.*

@DslMarker
internal annotation class PropertyTagMarker

@PropertyTagMarker
abstract class PropertyDSL

fun properties(init: Props.() -> Unit): List<Property> = emptyList()

open class DSL : PropertyDSL() {

    fun switcher(description: String, isEnabled: Boolean = false, onCheckedChangeListener: OnCheckedChangeListener? = null) {}

    fun switcher(key: String, description: String, onCheckedChangeListener: OnCheckedChangeListener? = null) {}

    fun text(description: String, value: String) {}

    fun dropDown(
        description: String,
        valueList: List<String>,
        currentValue: String,
        onDropDownStateChanged: OnDropDownStateChangedListener? = null
    ) {}

    fun dropDown(
        key: String,
        description: String,
        valueList: List<String>,
        onDropDownStateChanged: OnDropDownStateChangedListener? = null
    ) {}

    fun button(text: String, onButtonClickListener: OnButtonClickListener) {}

    fun get(): List<Property> = emptyList()
}

class Props : DSL() {
    fun group(title: String, init: DSL.() -> Unit) {}
}
