package com.websarva.wings.android.qrcodereader.viewmodel.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class MainViewModel: ViewModel() {
    private val _state = MutableLiveData<Int?>().apply {
        MutableLiveData<Int>()
    }

    fun setState(state: Int){
        _state.value = state
    }

    fun state(): MutableLiveData<Int?>{
        return _state
    }

    init {
        _state.value = null
    }
}