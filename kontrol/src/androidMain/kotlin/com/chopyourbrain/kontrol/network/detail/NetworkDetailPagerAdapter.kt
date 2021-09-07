package com.chopyourbrain.kontrol.network.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chopyourbrain.kontrol.ktor.NetCall

internal class NetworkDetailPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val call: NetCall
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NetworkDetailOverviewFragment.create(
                call.request?.url ?: "",
                call.request?.method ?: "",
                call.response?.status ?: 0,
                call.request?.timestamp ?: 0,
            )
            1 -> NetworkDetailRequestFragment.create(
                call.request?.headers ?: hashMapOf(),
                call.request?.body?.content ?: "",
                call.request?.error?.toString() ?: ""
            )
            2 -> NetworkDetailResponseFragment.create(
                call.response?.headers ?: hashMapOf(),
                call.response?.body?.content ?: "",
                call.response?.error?.toString() ?: ""
            )
            else -> throw NotImplementedError()
        }
    }
}
