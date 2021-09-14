package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment

class AfterScanViewModel: ViewModel() {
    private val _scanUri =  MutableLiveData<Uri>().apply {
        MutableLiveData<Uri>()
    }

    fun init(scanUrl: String, fragment: AfterScanFragment){
        // グローバル変数に設定を代入
        _scanUri.value = Uri.parse(scanUrl)

        // ヴァリデーションチェック
        validationCheck(fragment)
    }
    private fun validationCheck(fragment: AfterScanFragment){
        // viewへ処理を渡す
        when(_scanUri.value!!.scheme){
            "http", "https" -> {
                fragment.afterValidationCheck(valFlag = true, 0)
            }
            "geo" -> {
                fragment.afterValidationCheck(valFlag = true, 1)
            }
            else -> {
                fragment.afterValidationCheck(valFlag = false, type = null)
            }
        }
    }

    fun scanUri(): MutableLiveData<Uri>{
        return _scanUri
    }
}