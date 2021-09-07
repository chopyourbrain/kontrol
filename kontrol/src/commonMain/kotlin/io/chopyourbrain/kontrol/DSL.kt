package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.properties.*
import kotlinx.atomicfu.atomic

@DslMarker
internal annotation class PropertyTagMarker

@PropertyTagMarker
abstract class PropertyDSL

fun properties(init: Props.() -> Unit): List<Property> = Props().apply(init).get()

open class DSL : PropertyDSL() {
    protected val props = mutableListOf<Property>()

    fun switcher(description: String, isEnabled: Boolean = false, onCheckedChangeListener: OnCheckedChangeListener? = null) {
        props.add(SwitcherProperty(description, atomic(isEnabled), onCheckedChangeListener))
    }

    fun switcher(key: String, description: String, onCheckedChangeListener: OnCheckedChangeListener? = null) {
        val kvStorage = ServiceLocator.KVStorage.value
        val cachedListener = OnCheckedChangeListener {
            kvStorage?.setBoolean(key, it)
            onCheckedChangeListener?.invoke(it)
        }
        props.add(SwitcherProperty(description, atomic(kvStorage?.getBoolean(key) ?: false), cachedListener))
    }

    fun text(description: String, value: String) {
        props.add(TextProperty(description, value))
    }

    fun dropDown(
        description: String,
        valueList: List<String>,
        currentValue: String,
        onDropDownStateChanged: OnDropDownStateChangedListener? = null
    ) {
        props.add(DropDownProperty(description, valueList, atomic(currentValue), onDropDownStateChanged))
    }

    fun dropDown(
        key: String,
        description: String,
        valueList: List<String>,
        onDropDownStateChanged: OnDropDownStateChangedListener? = null
    ) {
        val kvStorage = ServiceLocator.KVStorage.value
        val cachedListener = OnDropDownStateChangedListener {
            kvStorage?.setString(key, it)
            onDropDownStateChanged?.invoke(it)
        }
        props.add(
            DropDownProperty(
                description, valueList, atomic(kvStorage?.getString(key) ?: valueList.firstOrNull() ?: ""),
                cachedListener
            )
        )
    }

    fun button(text: String, onButtonClickListener: OnButtonClickListener) {
        props.add(ButtonProperty(text, onButtonClickListener))
    }

    fun get(): List<Property> {
        return props
    }
}

class Props : DSL() {

    fun group(title: String, init: DSL.() -> Unit) {
        props.add(TitleProperty(title))
        apply(init)
    }
}