package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment

class CreateUrlViewModel: ViewModel() {
    private val _fragment = MutableLiveData<CreateUrlFragment>().apply {
        MutableLiveData<CreateUrlFragment>()
    }
    private val _displayFragment = MutableLiveData<DisplayFragment>().apply {
        MutableLiveData<DisplayFragment>()
    }

    fun init(fragment: CreateUrlFragment, displayFragment: DisplayFragment){
        _fragment.value = fragment
        _displayFragment.value = displayFragment
    }
    fun setBundle(url: String){
        // 空文字チェック
        if (url.isNotBlank()){
            // bundleに値をセット
            val bundle = Bundle()
            bundle.putString(IntentBundle.ScanUrl.name, url)
            bundle.putInt(IntentBundle.ScanType.name, 0)
            _displayFragment.value!!.arguments = bundle

            // viewへ処理を渡す
            _fragment.value!!.displayFragment()
        }else{
            _fragment.value!!.blackToast()
        }
    }

    fun displayFragment(): MutableLiveData<DisplayFragment>{
        return _displayFragment
    }
}