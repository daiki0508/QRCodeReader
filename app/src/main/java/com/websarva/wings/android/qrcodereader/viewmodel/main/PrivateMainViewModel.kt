package com.websarva.wings.android.qrcodereader.viewmodel.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.repository.AppUpdateRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity
import kotlinx.coroutines.launch

class PrivateMainViewModel(
    private val appUpdateRepository: AppUpdateRepositoryClient,
    application: Application
) : AndroidViewModel(application) {
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun connectingStatus(): NetworkCapabilities? {
        val connectivityManager =
            getApplication<Application>().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // 戻り値がnullでなければ、ネットワークに接続されている
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }
    fun appUpdate(activity: MainActivity){
        viewModelScope.launch {
            appUpdateRepository.appUpdate(activity)
        }
    }
    fun restartUpdate(activity: MainActivity){
        // updateがない、もしくは完了している場合はfalseを返す
        viewModelScope.launch {
            val updateFlag = appUpdateRepository.restartUpdate(activity)
            if (!updateFlag){
                Log.i("update", "complete or noUpdate")
            }
        }
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

    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }
}