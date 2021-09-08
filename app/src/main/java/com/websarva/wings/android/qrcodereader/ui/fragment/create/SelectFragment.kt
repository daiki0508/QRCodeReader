package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.FragmentSelectBinding
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.select.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectFragment: Fragment() {
    private var _binding: FragmentSelectBinding? = null
    private val binding
    get() = _binding!!

    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        mainViewModel.setState(2)

        // recyclerViewの生成
        activity?.let {
            val selectRecyclerViewAdapter = RecyclerViewAdapter(it, this)
            binding.rvSelect.adapter = selectRecyclerViewAdapter
            binding.rvSelect.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.rvSelect.layoutManager = LinearLayoutManager(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}