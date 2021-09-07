package com.chopyourbrain.kontrol.properties

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chopyourbrain.kontrol.databinding.*

internal abstract class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(property: Property)
}

internal class TitleViewHolder(
    private val binding: ItemTitlePropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val titleDebugProperty = property as? TitleProperty
        if (titleDebugProperty != null)
            binding.title.text = titleDebugProperty.title
    }
}

internal class SwitcherViewHolder(
    private val binding: ItemSwitcherPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val switcherDebugProperty = property as? SwitcherProperty
        if (switcherDebugProperty != null) {
            binding.apply {
                description.text = switcherDebugProperty.description
                switcher.isChecked = switcherDebugProperty.isEnabled.value
                root.setOnClickListener {
                    switcher.isChecked = !switcher.isChecked
                }
                switcher.setOnCheckedChangeListener { _, isChecked ->
                    switcherDebugProperty.onCheckedChangeListener.invoke(isChecked)
                }
            }
        }
    }
}

internal class DropDownViewHolder(
    private val binding: ItemDropdownPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val dropDownProperty = property as? DropDownProperty
        if (dropDownProperty != null) {
            binding.apply {
                description.text = dropDownProperty.description
                spinner.adapter = ArrayAdapter(
                    root.context,
                    android.R.layout.simple_spinner_item,
                    dropDownProperty.valueList).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                val selectedItemPosition = dropDownProperty.valueList.indexOf(dropDownProperty.currentValue.value)
                spinner.setSelection(selectedItemPosition)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        dropDownProperty.onDropDownStateChanged.invoke(dropDownProperty.valueList[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }
        }
    }
}

internal class ButtonViewHolder(
    private val binding: ItemButtonPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val buttonDebugProperty = property as? ButtonProperty
        if (buttonDebugProperty != null) {
            binding.apply {
                button.text = buttonDebugProperty.text
                button.setOnClickListener { buttonDebugProperty.onButtonClickListener.invoke() }
            }
        }
    }
}

internal class TextViewHolder(
    private val binding: ItemTextPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val textDebugProperty = property as? TextProperty
        if (textDebugProperty != null) {
            binding.description.text = textDebugProperty.description
            binding.value.text = textDebugProperty.value
        }
    }
}
