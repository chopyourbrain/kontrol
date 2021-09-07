package com.chopyourbrain.kontrol.network

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chopyourbrain.kontrol.R
import com.chopyourbrain.kontrol.databinding.ItemNetworkBinding
import com.chopyourbrain.kontrol.ktor.NetCall
import java.util.*

internal class NetworkViewHolder(
    private val binding: ItemNetworkBinding,
    private val itemClickListener: NetworkFragment.ItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    private val colorDefault = 0xFFFFFFFF.toInt()
    private val colorSuccess = ContextCompat.getColor(binding.root.context, R.color.green)
    private val colorWarn = ContextCompat.getColor(binding.root.context, R.color.yellow)
    private val colorError = ContextCompat.getColor(binding.root.context, R.color.red)

    fun bind(call: NetCall) {
        val status = call.response?.status

        val codeColor = when (status) {
            in 200..299 -> colorSuccess
            in 300..399 -> colorWarn
            in 400..499 -> colorError
            null -> colorError
            else -> colorDefault
        }

        binding.url.text = call.request?.url
        binding.method.text = call.request?.method

        binding.code.text = status?.toString() ?: "ERROR"
        binding.code.setTextColor(codeColor)

        binding.time.text = Date(call.timestamp).toString()

        binding.root.setOnClickListener { itemClickListener.onItemClick(call) }
    }
}
