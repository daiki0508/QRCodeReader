package com.websarva.wings.android.qrcodereader.viewmodel.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class MainViewModel: ViewModel() {
    private val _state = MutableLiveData<Int?>().apply {
        MutableLiveData<Int>()
    }
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun validationCheck(url: String, activity: MainActivity){
        activity.let {
            // ヴァリデーションチェック
            var bundle: Bundle? = null
            if (
                (it.intent.action == Intent.ACTION_SEND)
                && (it.intent.type?.startsWith("image/") == true)
            ){
                // bundleに値をセット
                bundle = Bundle().apply {
                    this.putString(IntentBundle.ScanUrl.name, url)
                    this.putInt(IntentBundle.ScanType.name, 2)
                }
            }
            _bundle.value = bundle
        }
    }

    fun setState(state: Int){
        _state.value = state
    }

    fun state(): MutableLiveData<Int?>{
        return _state
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }

    init {
        _state.value = null
    }
}