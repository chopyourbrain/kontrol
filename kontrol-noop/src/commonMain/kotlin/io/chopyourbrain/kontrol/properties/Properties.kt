package io.chopyourbrain.kontrol.properties

sealed class Property

class SwitcherProperty(val description: String) : Property()
class DropDownProperty(val description: String) : Property()
class ButtonProperty(val text: String, val onButtonClickListener: OnButtonClickListener) : Property()
class TextProperty(val description: String, val value: String) : Property()
class TitleProperty(val title: String) : Property()

fun interface OnCheckedChangeListener {
    fun invoke(value: Boolean)
}

fun interface OnDropDownStateChangedListener {
    fun invoke(value: String)
}

fun interface OnButtonClickListener {
    fun invoke()
}
