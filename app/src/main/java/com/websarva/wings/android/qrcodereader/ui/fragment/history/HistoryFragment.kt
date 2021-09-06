package com.websarva.wings.android.qrcodereader.ui.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.FragmentHistoryBinding
import com.websarva.wings.android.qrcodereader.ui.recyclerview.history.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.HistoryViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment: Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: HistoryViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        mainViewModel.setState(3)
        activity?.let {
            viewModel.init(it, this)
        }

        // recyclerViewの作成
        viewModel.getHistoryData()
    }

    fun recyclerView(items: MutableList<MutableMap<String, Any>>){
        // NoContentsを非表示
        binding.tvNoContents.visibility = View.GONE

        activity?.let {
            val historyRecyclerViewAdapter = RecyclerViewAdapter(items)
            binding.rvHistory.adapter = historyRecyclerViewAdapter
            binding.rvHistory.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.rvHistory.layoutManager = LinearLayoutManager(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}