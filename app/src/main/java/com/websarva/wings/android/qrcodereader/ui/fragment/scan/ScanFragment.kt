package com.websarva.wings.android.qrcodereader.ui.fragment.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentMainBinding
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.ScanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScanFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by sharedViewModel<ScanViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 現在のfragmentをmainViewModelに通知
        mainViewModel.setState(0)

        activity?.let {
            viewModel.init(it, binding.barcodeView, this)
        }
    }

    fun afterScanFragment(){
        activity?.let {
            // afterScanFragmentへの遷移
            val fragmentManager = it.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
            transaction.replace(R.id.container, viewModel.afterScanFragment().value!!).commit()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.barcodeView().value!!.resume()
    }

    override fun onPause() {
        super.onPause()

        viewModel.barcodeView().value!!.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}