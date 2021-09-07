package io.chopyourbrain.kontrol.properties

import io.chopyourbrain.kontrol.ServiceLocator
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.AtomicRef

sealed class Property

class SwitcherProperty(
    val description: String,
    var isEnabled: AtomicBoolean,
    listener: OnCheckedChangeListener?
) : Property() {

    val onCheckedChangeListener = OnCheckedChangeListener {
        isEnabled.value = it
        listener?.invoke(it)
    }

    override fun toString(): String {
        return StringBuilder()
            .append("$description = $isEnabled")
            .appendLine()
            .toString()
    }
}

fun interface OnCheckedChangeListener {
    fun invoke(value: Boolean)
}

class DropDownProperty(
    val description: String,
    val valueList: List<String>,
    var currentValue: AtomicRef<String>,
    listener: OnDropDownStateChangedListener?
) : Property() {

    val onDropDownStateChanged = OnDropDownStateChangedListener {
        currentValue.value = it
        listener?.invoke(it)
    }

    override fun toString(): String {
        return StringBuilder()
            .append("$description = $currentValue")
            .appendLine()
            .toString()
    }
}

fun interface OnDropDownStateChangedListener {
    fun invoke(value: String)
}

class ButtonProperty(
    val text: String,
    val onButtonClickListener: OnButtonClickListener
) : Property() {

    override fun toString(): String {
        return ""
    }
}

fun interface OnButtonClickListener {
    fun invoke()
}

class TextProperty(
    val description: String,
    val value: String
) : Property() {

    override fun toString(): String {
        return StringBuilder()
            .append("$description = $value")
            .appendLine()
            .toString()
    }
}

class TitleProperty(
    val title: String
) : Property() {

    override fun toString(): String {
        return StringBuilder()
            .append("$title :")
            .appendLine()
            .toString()
    }
}

private fun List<Property>?.toString(): String {
    if (this == null) return "null"
    val stringBuilder = StringBuilder()
    forEach {
        stringBuilder.append(it.toString())
    }
    return stringBuilder.toString()
}

internal fun getPropertiesAsString(): String {
    return ServiceLocator.MainDebugScreen.value.propertyList.toString()
}
