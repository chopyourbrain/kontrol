package io.chopyourbrain.kontrol.properties

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import io.chopyourbrain.kontrol.databinding.*

internal abstract class PropertyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(property: Property)
}

internal class TitleViewHolder(
    private val binding: KntrlItemTitlePropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val titleDebugProperty = property as? TitleProperty
        if (titleDebugProperty != null)
            binding.kntrlTitle.text = titleDebugProperty.title
    }
}

internal class SwitcherViewHolder(
    private val binding: KntrlItemSwitcherPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val switcherDebugProperty = property as? SwitcherProperty
        if (switcherDebugProperty != null) {
            binding.apply {
                kntrlDescription.text = switcherDebugProperty.description
                kntrlSwitcher.isChecked = switcherDebugProperty.isEnabled.value
                root.setOnClickListener {
                    kntrlSwitcher.isChecked = !kntrlSwitcher.isChecked
                }
                kntrlSwitcher.setOnCheckedChangeListener { _, isChecked ->
                    switcherDebugProperty.onCheckedChangeListener.invoke(isChecked)
                }
            }
        }
    }
}

internal class DropDownViewHolder(
    private val binding: KntrlItemDropdownPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val dropDownProperty = property as? DropDownProperty
        if (dropDownProperty != null) {
            binding.apply {
                kntrlDescription.text = dropDownProperty.description
                kntrlSpinner.adapter = ArrayAdapter(
                    root.context,
                    android.R.layout.simple_spinner_item,
                    dropDownProperty.valueList).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                val selectedItemPosition = dropDownProperty.valueList.indexOf(dropDownProperty.currentValue.value)
                kntrlSpinner.setSelection(selectedItemPosition)
                kntrlSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    private val binding: KntrlItemButtonPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val buttonDebugProperty = property as? ButtonProperty
        if (buttonDebugProperty != null) {
            binding.apply {
                kntrlButton.text = buttonDebugProperty.text
                kntrlButton.setOnClickListener { buttonDebugProperty.onButtonClickListener.invoke() }
            }
        }
    }
}

internal class TextViewHolder(
    private val binding: KntrlItemTextPropertyBinding
) : PropertyViewHolder(binding.root) {
    override fun bind(property: Property) {
        val textDebugProperty = property as? TextProperty
        if (textDebugProperty != null) {
            binding.kntrlDescription.text = textDebugProperty.description
            binding.kntrlValue.text = textDebugProperty.value
        }
    }
}
