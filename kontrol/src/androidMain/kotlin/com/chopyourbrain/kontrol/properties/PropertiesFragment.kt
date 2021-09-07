package com.chopyourbrain.kontrol.properties

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.chopyourbrain.kontrol.R
import com.chopyourbrain.kontrol.ServiceLocator
import com.chopyourbrain.kontrol.databinding.*
import com.chopyourbrain.kontrol.properties.*

internal class PropertiesFragment : Fragment() {
    private val diff = object : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property) = oldItem === newItem
        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val adapter = object : ListAdapter<Property, PropertyViewHolder>(diff) {
        init {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
            return when (viewType) {
                PropertiesViewHolderType.Title.value -> {
                    val itemBinding = ItemTitlePropertyBinding.inflate(LayoutInflater.from(parent.context))
                    TitleViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Switcher.value -> {
                    val itemBinding = ItemSwitcherPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    SwitcherViewHolder(itemBinding)
                }
                PropertiesViewHolderType.DropDown.value -> {
                    val itemBinding = ItemDropdownPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    DropDownViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Button.value -> {
                    val itemBinding = ItemButtonPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    ButtonViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Text.value -> {
                    val itemBinding = ItemTextPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    TextViewHolder(itemBinding)
                }
                else -> throw NotImplementedError()
            }
        }

        override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        override fun getItemViewType(position: Int): Int {
            return when (currentList[position]) {
                is TitleProperty -> PropertiesViewHolderType.Title.value
                is SwitcherProperty -> PropertiesViewHolderType.Switcher.value
                is DropDownProperty -> PropertiesViewHolderType.DropDown.value
                is ButtonProperty -> PropertiesViewHolderType.Button.value
                is TextProperty -> PropertiesViewHolderType.Text.value
                else -> -1
            }
        }
    }

    private lateinit var binding: FragmentPropertiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPropertiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerProperties.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PropertiesFragment.adapter
        }
        adapter.submitList(ServiceLocator.MainDebugScreen.value.propertyList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_properties, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getPropertiesAsString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun create(): PropertiesFragment {
            return PropertiesFragment()
        }
    }

    private enum class PropertiesViewHolderType(val value: Int) {
        Title(0), Switcher(1), DropDown(2), Button(3), Text(4)
    }
}
