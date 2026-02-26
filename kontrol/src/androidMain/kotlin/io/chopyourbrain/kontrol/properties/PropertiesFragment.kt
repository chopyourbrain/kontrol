package io.chopyourbrain.kontrol.properties

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import io.chopyourbrain.kontrol.ServiceLocator
import io.chopyourbrain.kontrol.android.R
import io.chopyourbrain.kontrol.android.databinding.KntrlFragmentPropertiesBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemButtonPropertyBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemDropdownPropertyBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemSwitcherPropertyBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemTextPropertyBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemTitlePropertyBinding

internal class PropertiesFragment : Fragment() {
    private val diff = object : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property) = oldItem === newItem
        @SuppressLint("DiffUtilEquals")
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
                    val itemBinding = KntrlItemTitlePropertyBinding.inflate(LayoutInflater.from(parent.context))
                    TitleViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Switcher.value -> {
                    val itemBinding = KntrlItemSwitcherPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    SwitcherViewHolder(itemBinding)
                }
                PropertiesViewHolderType.DropDown.value -> {
                    val itemBinding = KntrlItemDropdownPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    DropDownViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Button.value -> {
                    val itemBinding = KntrlItemButtonPropertyBinding.inflate(LayoutInflater.from(parent.context))
                    ButtonViewHolder(itemBinding)
                }
                PropertiesViewHolderType.Text.value -> {
                    val itemBinding = KntrlItemTextPropertyBinding.inflate(LayoutInflater.from(parent.context))
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

    private lateinit var binding: KntrlFragmentPropertiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = KntrlFragmentPropertiesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.kntrlRecyclerProperties.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PropertiesFragment.adapter
        }
        adapter.submitList(ServiceLocator.MainDebugScreen.value.propertyList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.kntrl_menu_properties, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.kntrl_menu_share -> {
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
