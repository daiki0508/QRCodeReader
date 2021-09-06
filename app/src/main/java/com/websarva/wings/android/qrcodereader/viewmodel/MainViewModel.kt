package com.websarva.wings.android.qrcodereader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _state = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }

    fun setState(state: Int){
        _state.value = state
    }

    fun state(): MutableLiveData<Int>{
        return _state
    }
}