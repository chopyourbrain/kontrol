package com.chopyourbrain.kontrol.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.chopyourbrain.kontrol.DebugMenuActivity
import com.chopyourbrain.kontrol.databinding.FragmentNetworkBinding
import com.chopyourbrain.kontrol.databinding.ItemNetworkBinding
import com.chopyourbrain.kontrol.ktor.NetCall
import com.chopyourbrain.kontrol.network.NetworkFragment.ItemClickListener
import com.chopyourbrain.kontrol.repository.getCallsList

internal class NetworkFragment : Fragment() {
    lateinit var binding: FragmentNetworkBinding
    private val diff = object : DiffUtil.ItemCallback<NetCall>() {
        override fun areItemsTheSame(oldItem: NetCall, newItem: NetCall) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: NetCall, newItem: NetCall): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
    private val itemClickListener = ItemClickListener {
        (activity as DebugMenuActivity).goToNetworkDetail(it)
    }
    private val adapter = object : ListAdapter<NetCall, NetworkViewHolder>(diff) {
        init {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkViewHolder {
            val itemBinding = ItemNetworkBinding.inflate(LayoutInflater.from(parent.context))
            return NetworkViewHolder(itemBinding, itemClickListener)
        }

        override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
            context?.let { holder.bind(getItem(position)) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNetworkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerRequest.adapter = adapter
        binding.recyclerRequest.layoutManager = LinearLayoutManager(context)
        lifecycleScope.launchWhenStarted { adapter.submitList(getCallsList()) }
    }

    companion object {
        fun create(): NetworkFragment {
            return NetworkFragment()
        }
    }

    fun interface ItemClickListener {
        fun onItemClick(item: NetCall)
    }
}
