package com.chopyourbrain.kontrol.network.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chopyourbrain.kontrol.databinding.FragmentNetworkRequestBinding
import com.chopyourbrain.kontrol.repository.parseToStringMap

internal class NetworkDetailRequestFragment : Fragment() {
    lateinit var binding: FragmentNetworkRequestBinding
    private val headers by lazy { requireArguments().headers }
    private val body by lazy { requireArguments().body }
    private val error by lazy { requireArguments().error }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNetworkRequestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val headersString = StringBuilder()
        headers.parseToStringMap().forEach {
            headersString.append(it.key.removePrefix(" ") + ": " + it.value)
            headersString.appendLine()
        }
        binding.headers.text = headersString.toString()
        binding.body.text = body
        binding.error.text = error
    }

    companion object {
        fun create(headers: Map<String, String>, body: String, error: String): NetworkDetailRequestFragment {
            return NetworkDetailRequestFragment().apply {
                arguments = Bundle().apply {
                    this.headers = headers.toString()
                    this.body = body
                    this.error = error
                }
            }
        }

        var Bundle.headers: String
            get() = getString("headers") ?: ""
            set(value) = putString("headers", value)

        var Bundle.body: String
            get() = getString("body") ?: ""
            set(value) = putString("body", value)

        var Bundle.error: String
            get() = getString("error") ?: ""
            set(value) = putString("error", value)
    }
}
