package io.chopyourbrain.kontrol.network.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.chopyourbrain.kontrol.databinding.FragmentNetworkOverviewBinding

internal class NetworkDetailOverviewFragment : Fragment() {
    lateinit var binding: FragmentNetworkOverviewBinding
    private val url by lazy { requireArguments().url }
    private val method by lazy { requireArguments().method }
    private val status by lazy { requireArguments().status }
    private val requestTime by lazy { requireArguments().requestTime }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNetworkOverviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.url.text = "Url: $url"
        binding.method.text = "Method: $method"
        binding.status.text = "Status: $status"
        binding.requestTime.text = "Request time: $requestTime"
    }

    companion object {
        fun create(url: String, method: String, status: Int, requestTime: Long):
                NetworkDetailOverviewFragment {
            return NetworkDetailOverviewFragment().apply {
                arguments = Bundle().apply {
                    this.url = url
                    this.method = method
                    this.status = if (status == 0) "ERROR" else status.toString()
                    this.requestTime = requestTime.toString()
                }
            }
        }

        var Bundle.url: String
            get() = getString("url") ?: ""
            set(value) = putString("url", value)

        var Bundle.method: String
            get() = getString("method") ?: ""
            set(value) = putString("method", value)

        var Bundle.status: String
            get() = getString("status") ?: ""
            set(value) = putString("status", value)

        var Bundle.requestTime: String
            get() = getString("requestTime") ?: ""
            set(value) = putString("requestTime", value)
    }
}
