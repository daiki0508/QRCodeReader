package com.websarva.wings.android.qrcodereader.viewmodel

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.websarva.wings.android.qrcodereader.model.App
import com.websarva.wings.android.qrcodereader.ui.fragment.create.app.CreateAppsFragment

class CreateAppsViewModel: ViewModel() {
    private val _fragment = MutableLiveData<CreateAppsFragment>().apply {
        MutableLiveData<CreateAppsFragment>()
    }
    private val _appList = MutableLiveData<MutableList<MutableMap<String, Any>>>().apply {
        MutableLiveData<MutableMap<String, Any>>()
    }

    fun init(fragment: CreateAppsFragment){
        _fragment.value = fragment
    }
    fun getAppsInfo(){
        // インストールされているアプリケーション情報を取得
        val pm = _fragment.value!!.context?.packageManager
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

    fun appList(): MutableLiveData<MutableList<MutableMap<String, Any>>>{
        return _appList
    }

    init {
        _appList.value = mutableListOf()
    }
}