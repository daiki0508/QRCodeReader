package com.websarva.wings.android.qrcodereader.ui.fragment.afterscan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.FragmentAfterscanBinding
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.recyclerview.afterscan.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.AfterScanViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AfterScanFragment: Fragment() {
    private var _binding: FragmentAfterscanBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by sharedViewModel<AfterScanViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfterscanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.setState(1)

        activity?.let {
            // 初期設定
            viewModel.init(it, arguments?.getString(IntentBundle.ScanUrl.name)!!, this)

            // recyclerview
            val actionRecyclerViewAdapter = RecyclerViewAdapter(it, viewModel.scanUri().value!!)
            binding.selectRecyclerView.adapter = actionRecyclerViewAdapter
            binding.selectRecyclerView.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.selectRecyclerView.layoutManager = LinearLayoutManager(it)
        }
    }

    fun afterValidationCheck(valFlag: Boolean){
        // OKはtrue
        if (valFlag){
            binding.scanUrl.text = viewModel.scanUri().value.toString()
        }else{
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            activity?.let {
                val fragmentManager = it.supportFragmentManager
                fragmentManager.beginTransaction().remove(this).commit()
                it.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}