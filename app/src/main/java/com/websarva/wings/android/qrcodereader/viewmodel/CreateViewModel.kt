package com.websarva.wings.android.qrcodereader.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateFragment

class CreateViewModel: ViewModel() {
    private val _fragment = MutableLiveData<CreateFragment>().apply {
        MutableLiveData<CreateFragment>()
    }
    private val _qrBitmap = MutableLiveData<Bitmap>().apply {
        MutableLiveData<String>()
    }

    fun init(fragment: CreateFragment){
        _fragment.value = fragment
    }
    fun validationCheck(url: String){
        val uri = Uri.parse(url)

        // 空文字チェック
        if (url.isNotBlank()){
            // ヴァリデーションチェック
            if (uri.scheme == "http" || uri.scheme == "https"){
                // QRコード作成
                createQR(url)
            }else{
                _fragment.value!!.exitError()
            }
        }else{
            _fragment.value!!.blackToast()
        }
    }
    private fun createQR(url: String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            // viewへ通知
            _qrBitmap.value = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 500, 500)
        }catch (e: WriterException){
            Log.w("createQR", e)
        }
    }

    fun qrBitmap(): MutableLiveData<Bitmap>{
        return _qrBitmap
    }
}