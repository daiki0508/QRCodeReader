package com.websarva.wings.android.qrcodereader.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.websarva.wings.android.qrcodereader.model.CacheName
import com.websarva.wings.android.qrcodereader.repository.SaveBitmapClientRepository
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment
import kotlinx.coroutines.launch
import java.io.File
import java.lang.IllegalArgumentException

class DisplayViewModel(
    private val saveBitmapClient: SaveBitmapClientRepository
): ViewModel() {
    private val _fragment = MutableLiveData<DisplayFragment>().apply {
        MutableLiveData<CreateUrlFragment>()
    }
    private val _qrBitmap = MutableLiveData<Bitmap>().apply {
        MutableLiveData<String>()
    }
    private val _qrCodeUri = MutableLiveData<Uri?>().apply {
        MutableLiveData<Uri?>()
    }

    fun init(fragment: DisplayFragment){
        _fragment.value = fragment
    }
    fun validationCheck(url: String, type: Int){
        val uri = Uri.parse(url)

        // 空文字チェック
        if (url.isNotBlank()){
            // ヴァリデーションチェック
            when(type){
                // 0がwebページ, 1が地図
                0 -> {
                    if (uri.scheme == "http" || uri.scheme == "https"){
                        // QRコード作成
                        createQR(url)
                    }else{
                        _fragment.value!!.exitError()
                    }
                }
                1 -> {
                    if (uri.scheme == "geo"){
                        // QRコード作成
                        createQR(url)
                    }else{
                        _fragment.value!!.exitError()
                    }
                }
                else -> {
                    _fragment.value!!.exitError()
                }
            }
        }else{
            _fragment.value!!.blackToast()
        }
    }
    private fun createQR(url: String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            val qrcode = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 600, 600)

            viewModelScope.launch {
                // cacheにqrcodeを保存
                saveBitmap(qrcode)
                // viewへ通知
                _qrBitmap.value = qrcode
            }
        }catch (e: WriterException){
            Log.w("createQR", e)
        }
    }
    private suspend fun saveBitmap(qrcode: Bitmap) {
        _fragment.value!!.activity?.let {
            saveBitmapClient.save(it, qrcode)
        }
    }
    fun getQRCodeUri(): MutableLiveData<Uri?> {
        _qrCodeUri.apply {
            // 画面回転時にuriを再取得しないようにする
            if (this.value == null){
                this.value = try {
                    _fragment.value!!.activity?.let {
                        val file = File(it.cacheDir, CacheName().cache)
                        // fileからuriを取得
                        FileProvider.getUriForFile(it, "${it.packageName}.fileprovider", file)
                    }
                }catch (e: IllegalArgumentException){
                    Log.wtf("ERROR", "CANNOT GET URL FOR SHARE")
                    null
                }
            }
        }
        return _qrCodeUri
    }
    fun deleteChaChe(){
        _fragment.value!!.activity?.let {
            saveBitmapClient.delete(it)
        }
    }

    fun qrBitmap(): MutableLiveData<Bitmap> {
        return _qrBitmap
    }

    init {
        _qrCodeUri.value = null
    }
}