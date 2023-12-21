package io.chopyourbrain.kontrol.network.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.chopyourbrain.kontrol.android.databinding.KntrlFragmentNetworkResponseBinding
import io.chopyourbrain.kontrol.repository.parseToStringMap

internal class NetworkDetailResponseFragment : Fragment() {
    lateinit var binding: KntrlFragmentNetworkResponseBinding
    private val headers by lazy { requireArguments().headers }
    private val body by lazy { requireArguments().body }
    private val error by lazy { requireArguments().error }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = KntrlFragmentNetworkResponseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val headersString = StringBuilder()
        headers.parseToStringMap().forEach {
            headersString.append(it.key.removePrefix(" ") + ": " + it.value)
            headersString.appendLine()
        }
        binding.kntrlHeaders.text = headersString.toString()
        binding.kntrlBody.text = body
        binding.kntrlError.text = error
    }

    companion object {
        fun create(headers: Map<String, String>, body: String, error: String): NetworkDetailResponseFragment {
            return NetworkDetailResponseFragment().apply {
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
