package com.chopyourbrain.kontrol.network.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chopyourbrain.kontrol.databinding.FragmentNetworkDetailBinding
import com.chopyourbrain.kontrol.repository.getCallById
import com.google.android.material.tabs.TabLayoutMediator

internal class NetworkDetailFragment : Fragment() {
    lateinit var binding: FragmentNetworkDetailBinding
    private val callId by lazy { requireArguments().callId }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNetworkDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenStarted {
            val call = getCallById(callId)
            binding.pager.adapter = NetworkDetailPagerAdapter(childFragmentManager, lifecycle, call)
            TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                tab.text = when (position) {
                    0 -> "OVERVIEW"
                    1 -> "REQUEST"
                    2 -> "RESPONSE"
                    else -> ""
                }

            }.attach()
        }
    }

    companion object {
        fun create(callId: Long): NetworkDetailFragment {
            return NetworkDetailFragment().apply {
                arguments = Bundle().apply {
                    this.callId = callId
                }
            }
        }

        var Bundle.callId: Long
            get() = getLong("callId")
            set(value) = putLong("callId", value)
    }
}
