package io.chopyourbrain.kontrol.network

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.chopyourbrain.kontrol.android.R
import io.chopyourbrain.kontrol.android.databinding.KntrlItemNetworkBinding
import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.timestampToString

internal class NetworkViewHolder(
    private val binding: KntrlItemNetworkBinding,
    private val itemClickListener: NetworkFragment.ItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    private val colorDefault = 0xFFFFFFFF.toInt()
    private val colorSuccess = ContextCompat.getColor(binding.root.context, R.color.kntrl_green)
    private val colorWarn = ContextCompat.getColor(binding.root.context, R.color.kntrl_yellow)
    private val colorError = ContextCompat.getColor(binding.root.context, R.color.kntrl_red)

    fun bind(call: NetCall) {
        val status = call.response?.status

        val codeColor = when (status) {
            in 200..299 -> colorSuccess
            in 300..399 -> colorWarn
            in 400..499 -> colorError
            null -> colorError
            else -> colorDefault
        }

        binding.kntrlUrl.text = call.request?.url
        binding.kntrlMethod.text = call.request?.method

        binding.kntrlCode.text = status?.toString() ?: "ERROR"
        binding.kntrlCode.setTextColor(codeColor)

        binding.kntrlTime.text = call.timestamp.timestampToString()

        binding.root.setOnClickListener { itemClickListener.onItemClick(call) }
    }
}
