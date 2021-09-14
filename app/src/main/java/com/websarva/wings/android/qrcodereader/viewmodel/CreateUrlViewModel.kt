package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.create.web.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment

class CreateUrlViewModel: ViewModel() {
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun setBundle(url: String){
        // 空文字チェック
        var bundle: Bundle? = null
        if (url.isNotBlank()){
            // bundleに値をセット
            bundle = Bundle().apply {
                this.putString(IntentBundle.ScanUrl.name, url)
                this.putInt(IntentBundle.ScanType.name, 0)
            }
        }
        _bundle.value = bundle
    }

    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }
}