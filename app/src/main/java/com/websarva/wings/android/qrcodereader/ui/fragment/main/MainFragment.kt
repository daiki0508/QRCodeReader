package com.websarva.wings.android.qrcodereader.ui.fragment.main

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

    private lateinit var barcodeView: CompoundBarcodeView

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

        // qrコードライブラリの設定
        val formats = listOf(BarcodeFormat.QR_CODE)
        barcodeView = binding.barcodeView
        barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.cameraSettings.isAutoFocusEnabled = true
        barcodeView.decodeSingle { result ->
            Log.d("scanResult", result.text)
            val bundle = Bundle()
            bundle.putString("scanUrl", result.text)
            val afterScanFragment = AfterScanFragment()
            afterScanFragment.arguments = bundle

            activity?.let {
                val fragmentManager = it.supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                transaction.replace(R.id.container, afterScanFragment).commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()

        barcodeView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}