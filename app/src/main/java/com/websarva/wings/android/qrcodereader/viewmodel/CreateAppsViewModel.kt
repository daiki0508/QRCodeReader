package com.websarva.wings.android.qrcodereader.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.App
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.app.CreateAppsFragment

class CreateAppsViewModel(
    application: Application
): AndroidViewModel(application) {
    private val _appList = MutableLiveData<MutableList<MutableMap<String, Any>>>().apply {
        MutableLiveData<MutableMap<String, Any>>()
    }
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun getAppsInfo(){
        // インストールされているアプリケーション情報を取得
        val pm = getApplication<Application>().applicationContext.packageManager
        val installedAppList: List<ApplicationInfo> = pm!!.getInstalledApplications(0)

        // リストに格納
        val appList: MutableList<MutableMap<String, Any>> = mutableListOf()
        var app: MutableMap<String, Any>
        for (installedApp: ApplicationInfo in installedAppList){
            if ((installedApp.flags and ApplicationInfo.FLAG_SYSTEM) == 0){
                app = mutableMapOf(
                    App.Label.name to installedApp.loadLabel(pm).toString(),
                    App.Icon.name to installedApp.loadIcon(pm),
                    App.PName.name to installedApp.packageName
                )
                appList.add(app)
            }
        }

        _appList.value = appList
    }
    fun setBundle(pName: String){
        // bundleに値をセット
        val bundle = Bundle()
        bundle.putString(
            IntentBundle.ScanUrl.name,
            "https://play.google.com/store/apps/details?id=$pName"
        )
        bundle.putInt(IntentBundle.ScanType.name, 2)
        _bundle.value = bundle
    }

    fun appList(): MutableLiveData<MutableList<MutableMap<String, Any>>>{
        return _appList
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }

    init {
        _appList.value = mutableListOf()
    }
}