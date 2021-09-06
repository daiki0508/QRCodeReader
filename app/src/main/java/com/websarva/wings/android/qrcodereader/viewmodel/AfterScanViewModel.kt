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
    private val activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val fragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }

    fun init(activity: FragmentActivity, scanUrl: String, fragment: AfterScanFragment){
        // グローバル変数に設定を代入
        _scanUri.value = Uri.parse(scanUrl)
        this.activity.value = activity
        this.fragment.value = fragment

        // 画面回転を固定する
        this.activity.value!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        // ヴァリデーションチェック
        validationCheck()
    }
    private fun validationCheck(){
        if (_scanUri.value!!.scheme == "http" || _scanUri.value!!.scheme == "https"){
            // viewへ処理を渡す
            fragment.value!!.afterValidationCheck(valFlag = true)
        }else{
            // viewへ処理を渡す
            fragment.value!!.afterValidationCheck(valFlag = false)
        }
    }

    fun scanUri(): MutableLiveData<Uri>{
        return _scanUri
    }
}