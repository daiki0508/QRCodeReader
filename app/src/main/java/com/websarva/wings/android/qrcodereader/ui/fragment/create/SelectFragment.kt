package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.showAlignBottom
import com.websarva.wings.android.qrcodereader.databinding.FragmentSelectBinding
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.select.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.SelectViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectFragment: Fragment() {
    private var _binding: FragmentSelectBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: SelectViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val bottomNavViewModel by activityViewModels<BottomNavViewModel>()

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

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.hide()
        }

        // 初期設定
        mainViewModel.setState(2)
        viewModel.init(this)

        // recyclerViewの生成
        activity?.let {
            val selectRecyclerViewAdapter = RecyclerViewAdapter(it, this, viewModel, bottomNavViewModel)
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