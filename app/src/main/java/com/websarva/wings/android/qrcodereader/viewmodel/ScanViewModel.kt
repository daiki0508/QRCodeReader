package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.model.SaveData
import com.websarva.wings.android.qrcodereader.repository.PreferenceRepository
import com.websarva.wings.android.qrcodereader.repository.PreferenceRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import kotlinx.coroutines.launch
import java.util.*

class ScanViewModel(
    private val preferenceRepository: PreferenceRepositoryClient
): ViewModel() {
    private val _afterScanFragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }
    private val _mainFragment = MutableLiveData<ScanFragment>().apply {
        MutableLiveData<ScanFragment>()
    }
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _barcodeView = MutableLiveData<CompoundBarcodeView>().apply {
        MutableLiveData<CompoundBarcodeView>()
    }

    fun init(activity: FragmentActivity, barcodeView: CompoundBarcodeView, scanFragment: ScanFragment){
        // activityやfragmentをグローバル変数に代入
        _activity.value = activity
        _afterScanFragment.value  = AfterScanFragment()
        _barcodeView.value = barcodeView
        _mainFragment.value = scanFragment

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

            viewModelScope.launch {
                // 履歴を作成、保存
                val date = DateFormat.format("yyyy/MM/dd kk:mm", Calendar.getInstance())
                preferenceRepository.write(_activity.value!!, keyName = it.text, SaveData(title = it.text, type = 0, time = date.toString()))

                // bundleへのデータセットと値の受け渡し準備
                val bundle = Bundle()
                bundle.putString("scanUrl", it.text)
                _afterScanFragment.value!!.arguments = bundle

                // viewへ処理を渡す
                _mainFragment.value!!.afterScanFragment()
            }
        }
    }

    fun barcodeView(): MutableLiveData<CompoundBarcodeView>{
        return _barcodeView
    }
    fun afterScanFragment(): MutableLiveData<AfterScanFragment>{
        return _afterScanFragment
    }
}