package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class MainViewModel: ViewModel() {
    private val _activity = MutableLiveData<MainActivity>().apply {
        MutableLiveData<MainActivity>()
    }
    private val _photoFragment = MutableLiveData<PhotoFragment>().apply {
        MutableLiveData<PhotoFragment>()
    }
    private val _state = MutableLiveData<Int?>().apply {
        MutableLiveData<Int>()
    }

    fun init(activity: MainActivity){
        _activity.value = activity
    }
    fun validationCheck(url: String){
        _activity.value?.let {
            // ヴァリデーションチェック
            if (
                (it.intent.action == Intent.ACTION_SEND)
                && (it.intent.type?.startsWith("image/") == true)
            ){
                _photoFragment.value = PhotoFragment()

                // bundleに値をセット
                val bundle = Bundle()
                bundle.putString(IntentBundle.ScanUrl.name, url)
                bundle.putInt(IntentBundle.ScanType.name, 1)
                _photoFragment.value!!.arguments = bundle

                // viewへ処理を渡す
                _activity.value!!.afterScanFragment()
            }else{
                _activity.value!!.exitError()
            }
        }
    }

    fun setState(state: Int){
        _state.value = state
    }

    fun state(): MutableLiveData<Int?>{
        return _state
    }
    fun photoFragment(): MutableLiveData<PhotoFragment>{
        return _photoFragment
    }

    init {
        _state.value = null
    }
}