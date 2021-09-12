package com.websarva.wings.android.qrcodereader.viewmodel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class PhotoViewModel: ViewModel() {
    private val _fragment = MutableLiveData<PhotoFragment>().apply {
        MutableLiveData<PhotoFragment>()
    }
    private val _afterScanFragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }
    private val _qrcode = MutableLiveData<Bitmap>().apply {
        MutableLiveData<Bitmap>()
    }
    private val _url = MutableLiveData<String>().apply {
        MutableLiveData<String>()
    }

    fun init(fragment: PhotoFragment){
        _fragment.value = fragment
    }
    fun createGetDeviceImageIntent() = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
        it.addCategory(Intent.CATEGORY_OPENABLE)
        it.type = "image/*"

        val launcher = _fragment.value!!.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                readQRCodeFromImage(result.data?.data!!)
            }else{
                Log.w("Warning", "ACTIVITY RESULT FAILURE")
            }
        }

        // Activityを起動
        launcher.launch(it)
    }
    fun readQRCodeFromImage(uri: Uri) = with(getBitmapFromUri(uri)) {
        val pixels = IntArray(this!!.width * this.height)
        this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
        val source = RGBLuminanceSource(this.width, this.height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        // MutableLiveDataに値をセット
        _qrcode.value = this
        val url = MultiFormatReader().decode(binaryBitmap).text
        _url.value = url
    }
    private fun getBitmapFromUri(uri: Uri?) = when{
        uri != null -> _fragment.value!!.context?.contentResolver
            ?.openFileDescriptor(uri, "r")
            ?.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }
        else -> null
    }
    fun setBundle(){
        _afterScanFragment.value = AfterScanFragment()

        // bundleに値をセット
        val bundle = Bundle()
        bundle.putString(IntentBundle.ScanUrl.name, _url.value)
        _afterScanFragment.value!!.arguments = bundle

        // viewへ処理を渡す
        _fragment.value!!.afterScanFragment()
    }

    fun qrcode(): MutableLiveData<Bitmap>{
        return _qrcode
    }
    fun url(): MutableLiveData<String>{
        return _url
    }
    fun afterScanFragment(): MutableLiveData<AfterScanFragment>{
        return _afterScanFragment
    }

    init {
        _url.value = ""
    }
}