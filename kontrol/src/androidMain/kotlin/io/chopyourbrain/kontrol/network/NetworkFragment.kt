package io.chopyourbrain.kontrol.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import io.chopyourbrain.kontrol.DebugMenuActivity
import io.chopyourbrain.kontrol.android.databinding.KntrlFragmentNetworkBinding
import io.chopyourbrain.kontrol.android.databinding.KntrlItemNetworkBinding
import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.network.NetworkFragment.ItemClickListener
import io.chopyourbrain.kontrol.repository.getCallsList
import kotlinx.coroutines.launch

internal class NetworkFragment : Fragment() {
    lateinit var binding: KntrlFragmentNetworkBinding
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
            val itemBinding = KntrlItemNetworkBinding.inflate(LayoutInflater.from(parent.context))
            return NetworkViewHolder(itemBinding, itemClickListener)
        }

        override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
            context?.let { holder.bind(getItem(position)) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KntrlFragmentNetworkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.kntrlRecyclerRequest.adapter = adapter
        binding.kntrlRecyclerRequest.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.submitList(getCallsList())
            }
        }
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
