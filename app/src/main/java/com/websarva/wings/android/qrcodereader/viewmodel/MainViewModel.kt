package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.main.MainFragment

class MainViewModel: ViewModel() {
    private val _afterScanFragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }
    private val _mainFragment = MutableLiveData<MainFragment>().apply {
        MutableLiveData<MainFragment>()
    }
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _barcodeView = MutableLiveData<CompoundBarcodeView>().apply {
        MutableLiveData<CompoundBarcodeView>()
    }

    fun init(activity: FragmentActivity, barcodeView: CompoundBarcodeView, mainFragment: MainFragment){
        // activityやfragmentをグローバル変数に代入
        _activity.value = activity
        _afterScanFragment.value  = AfterScanFragment()
        _barcodeView.value = barcodeView
        _mainFragment.value = mainFragment

        // 画面回転を端末の傾きに依存する
        _activity.value!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        // barcodeViewの設定
        initBarcodeView()
    }
    private fun initBarcodeView(){
        _barcodeView.value?.let {
            val formats = listOf(BarcodeFormat.QR_CODE)
            it.decoderFactory = DefaultDecoderFactory(formats)
            it.cameraSettings.isAutoFocusEnabled = true

            // scan開始と結果取得
            scanResult(it)
        }
    }
    private fun scanResult(barcodeView: CompoundBarcodeView){
        barcodeView.decodeSingle {
            Log.d("scanResult", it.text)

            // bundleへのデータセットと値の受け渡し準備
            val bundle = Bundle()
            bundle.putString("scanUrl", it.text)
            _afterScanFragment.value!!.arguments = bundle

            // viewへ処理を渡す
            _mainFragment.value!!.afterScanFragment()
        }
    }

    fun barcodeView(): MutableLiveData<CompoundBarcodeView>{
        return _barcodeView
    }
    fun afterScanFragment(): MutableLiveData<AfterScanFragment>{
        return _afterScanFragment
    }
}