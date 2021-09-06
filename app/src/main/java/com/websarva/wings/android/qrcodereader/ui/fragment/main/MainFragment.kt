package com.websarva.wings.android.qrcodereader.ui.fragment.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentMainBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding
    get() = _binding!!
    private val viewModel by sharedViewModel<MainViewModel>()

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

        activity?.let {
            viewModel.init(it, binding.barcodeView, this)
        }
    }

    fun afterScanFragment(){
        activity?.let {
            val fragmentManager = it.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.addToBackStack(null)
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