package br.com.market.storage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.market.storage.databinding.FragmentListaProdutosBinding
import br.com.market.storage.ui.fragment.base.AbstractSessionedFragment

class ListaProdutosFragment : AbstractSessionedFragment() {

    private var _binding: FragmentListaProdutosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaProdutosBinding.inflate(inflater, container, false)
        return binding.root
    }

}