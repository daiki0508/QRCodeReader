package com.websarva.wings.android.qrcodereader.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.model.SaveData
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import kotlinx.coroutines.launch
import java.util.*

class CameraViewModel(
    private val preferenceHistoryRepository: PreferenceHistoryRepositoryClient
): ViewModel() {
    private val _afterScanFragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }
    private val _mainFragment = MutableLiveData<CameraFragment>().apply {
        MutableLiveData<CameraFragment>()
    }
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _barcodeView = MutableLiveData<CompoundBarcodeView>().apply {
        MutableLiveData<CompoundBarcodeView>()
    }

    fun init(activity: FragmentActivity, barcodeView: CompoundBarcodeView, cameraFragment: CameraFragment){
        // activityやfragmentをグローバル変数に代入
        _activity.value = activity
        _afterScanFragment.value  = AfterScanFragment()
        _barcodeView.value = barcodeView
        _mainFragment.value = cameraFragment

        // barcodeViewの設定
        initBarcodeView()
    }
    private fun initBarcodeView(){
        _barcodeView.value?.let {
            val formats = listOf(BarcodeFormat.QR_CODE)
            it.decoderFactory = DefaultDecoderFactory(formats)
            it.cameraSettings.isAutoFocusEnabled = true

            // 権限確認
            checkPermission()
        }
    }
    private fun checkPermission(){
        // 権限を既に取得しているかを確認
        if (ActivityCompat.checkSelfPermission(
                _mainFragment.value!!.requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED){
            Log.i("check", "GetPermission")

            // scan開始と結果取得
            scanResult(_barcodeView.value!!)
        }else{
            Log.i("check", "requestPermission")

            val requestPermission = _mainFragment.value!!.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ){
                if (it){
                    // 許可時の処理
                    Log.i("result", "Permission Result")
                    checkPermission()
                }else{
                    // 拒否時の処理
                    Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
                    _mainFragment.value!!.scanFragment()
                }
            }

            // request開始
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }
    private fun scanResult(barcodeView: CompoundBarcodeView){
        barcodeView.decodeSingle {
            Log.d("scanResult", it.text)

            viewModelScope.launch {
                // 履歴を作成、保存
                val date = DateFormat.format("yyyy/MM/dd kk:mm", Calendar.getInstance())
                val uri = Uri.parse(it.text)
                preferenceHistoryRepository.write(
                    _activity.value!!,
                    keyName = it.text,
                    SaveData(
                        title = it.text,
                        type = if (uri.scheme == "http" || uri.scheme == "https"){
                            0
                        }else if (uri.scheme == "geo") {
                            1
                        } else {
                            2
                        },
                        time = date.toString()
                    )
                )

                // bundleへのデータセットと値の受け渡し準備
                val bundle = Bundle()
                bundle.putString(IntentBundle.ScanUrl.name, it.text)
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