package com.websarva.wings.android.qrcodereader.viewmodel.scan.photo

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class PhotoViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val _launcher = MutableLiveData<ActivityResultLauncher<Intent>>()
    private val _qrcode = MutableLiveData<Bitmap>()
    private val _url = MutableLiveData<String>()
    private val _bundle = MutableLiveData<Bundle>()

    fun init(fragment: PhotoFragment){
        _launcher.value = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                readQRCodeFromImage(result.data?.data!!)
            }else{
                Log.w("Warning", "ACTIVITY RESULT FAILURE")
            }
        }
    }

    fun createGetDeviceImageIntent() = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
        it.addCategory(Intent.CATEGORY_OPENABLE)
        it.type = "image/*"

        // Activityを起動
        _launcher.value!!.launch(it)
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
        uri != null -> getApplication<Application>().contentResolver
            ?.openFileDescriptor(uri, "r")
            ?.use {
                val beforeBitmap = BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
                Bitmap.createScaledBitmap(beforeBitmap, 625, 625, true)
            }
        else -> null
    }
    fun setBundle(type: Int){
        // bundleに値をセット
        val bundle = Bundle()
        bundle.putString(IntentBundle.ScanUrl.name, _url.value)
        bundle.putInt(IntentBundle.ScanType.name, type)
        _bundle.value = bundle
    }

    fun qrcode(): MutableLiveData<Bitmap>{
        return _qrcode
    }
    fun url(): MutableLiveData<String>{
        return _url
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }

    init {
        Log.d("test0", "aaaaaa")
        _url.value = ""
    }
}