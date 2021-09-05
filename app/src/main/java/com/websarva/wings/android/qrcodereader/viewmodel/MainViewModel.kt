package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment

class MainViewModel: ViewModel() {
    private val _flag = MutableLiveData<Boolean>().apply {
        MutableLiveData<Boolean>()
    }

    private lateinit var afterScanFragment: AfterScanFragment

    fun initBarcodeView(barcodeView: CompoundBarcodeView){
        val formats = listOf(BarcodeFormat.QR_CODE)
        barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.cameraSettings.isAutoFocusEnabled = true

        // scan開始と結果取得
        scanResult(barcodeView)
    }

    private fun scanResult(barcodeView: CompoundBarcodeView){
        barcodeView.decodeSingle {
            Log.d("scanResult", it.text)
            val bundle = Bundle()
            bundle.putString("scanUrl", it.text)
            afterScanFragment.arguments = bundle
            // viewに通知
            _flag.value = true
        }
    }

    fun init(afterScanFragment: AfterScanFragment){
        this.afterScanFragment = afterScanFragment
    }
    fun clearBundle(){
        _flag.value = false
    }

    fun flag(): MutableLiveData<Boolean>{
        return _flag
    }

    init {
        _flag.value = false
    }
}