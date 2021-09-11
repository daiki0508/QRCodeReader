package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class ScanViewModel: ViewModel() {
    private val _fragment = MutableLiveData<ScanFragment>().apply {
        MutableLiveData<ScanFragment>()
    }
    private val _photoFragment = MutableLiveData<PhotoFragment>().apply {
        MutableLiveData<PhotoFragment>()
    }

    fun init(fragment: ScanFragment){
        _fragment.value = fragment
    }
    fun setBundle(){
        _photoFragment.value = PhotoFragment()

        // bundleに値をセット
        val bundle = Bundle()
        bundle.putInt(IntentBundle.ScanType.name, 0)
        _photoFragment.value!!.arguments = bundle

        // viewへ処理を渡す
        _fragment.value!!.photoFragment()
    }

    fun photoFragment(): MutableLiveData<PhotoFragment>{
        return _photoFragment
    }
}