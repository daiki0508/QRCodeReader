package com.websarva.wings.android.qrcodereader.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.model.SaveData
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.BundleEvent
import kotlinx.coroutines.launch
import java.util.*

class CameraViewModel(
    application: Application
): AndroidViewModel(application) {
    private val _barcodeView = MutableLiveData<CompoundBarcodeView>().apply {
        MutableLiveData<CompoundBarcodeView>()
    }
    private val _bundle = MutableLiveData<BundleEvent<Bundle>>().apply {
        MutableLiveData<BundleEvent<Bundle>>()
    }
    val bundle: LiveData<BundleEvent<Bundle>> = _bundle

    fun init(barcodeView: CompoundBarcodeView, fragment: CameraFragment){
        _barcodeView.value = barcodeView

        // barcodeViewの設定
        initBarcodeView(fragment)
    }
    private fun initBarcodeView(fragment: CameraFragment){
        _barcodeView.value?.let {
            val formats = listOf(BarcodeFormat.QR_CODE)
            it.decoderFactory = DefaultDecoderFactory(formats)
            it.cameraSettings.isAutoFocusEnabled = true

            // 権限確認
            checkPermission(fragment)
        }
    }
    private fun checkPermission(fragment: CameraFragment){
        // 権限を既に取得しているかを確認
        if (ActivityCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED){
            Log.i("check", "GetPermission")

            // scan開始と結果取得
            scanResult(_barcodeView.value!!)
        }else{
            Log.i("check", "requestPermission")

            val requestPermission = fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ){
                if (it){
                    // 許可時の処理
                    Log.i("result", "Permission Result")
                    checkPermission(fragment)
                }else{
                    // 拒否時の処理
                    Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
                    fragment.scanFragment()
                }
            }

            // request開始
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }
    private fun scanResult(barcodeView: CompoundBarcodeView){
        barcodeView.decodeSingle {
            Log.d("scanResult", it.text)
            // bundleへのデータセットと値の受け渡し準備
            val bundle = Bundle()
            bundle.putString(IntentBundle.ScanUrl.name, it.text)
            bundle.putInt(IntentBundle.ScanType.name, 0)
            _bundle.value = BundleEvent(bundle)
        }
    }

    fun barcodeView(): MutableLiveData<CompoundBarcodeView>{
        return _barcodeView
    }
}