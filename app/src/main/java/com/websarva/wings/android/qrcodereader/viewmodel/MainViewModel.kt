package com.websarva.wings.android.qrcodereader.viewmodel

import androidx.lifecycle.ViewModel
import com.google.zxing.integration.android.IntentIntegrator

class MainViewModel: ViewModel() {
    fun initQRScanIntegrator(qrScanIntegrator: IntentIntegrator){
        // 縦画面に固定
        qrScanIntegrator.setOrientationLocked(false)
        // ヒープ音停止
        qrScanIntegrator.setBeepEnabled(false)
        // スキャン開始
        qrScanIntegrator.initiateScan()
    }
}