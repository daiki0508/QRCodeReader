package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class PhotoViewModel: ViewModel() {
    private val _fragment = MutableLiveData<PhotoFragment>().apply {
        MutableLiveData<PhotoFragment>()
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

        _fragment.value!!.startActivityForResult(it, 1)
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

        // viewへ処理を渡す
        //_fragment.value!!.setQRCode(this)
    }
    private fun getBitmapFromUri(uri: Uri?) = when{
        uri != null -> _fragment.value!!.context?.contentResolver
            ?.openFileDescriptor(uri, "r")
            ?.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }
        else -> null
    }

    fun qrcode(): MutableLiveData<Bitmap>{
        return _qrcode
    }
    fun url(): MutableLiveData<String>{
        return _url
    }

    init {
        _url.value = ""
    }
}