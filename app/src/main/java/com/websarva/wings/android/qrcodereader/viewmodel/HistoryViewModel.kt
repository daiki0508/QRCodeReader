package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.model.SaveData
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.history.RecyclerViewAdapter
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(
    private val preferenceHistoryRepository: PreferenceHistoryRepositoryClient
): ViewModel() {
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _fragment = MutableLiveData<HistoryFragment>().apply {
        MutableLiveData<HistoryFragment>()
    }
    private val _afterScanFragment = MutableLiveData<AfterScanFragment>().apply {
        MutableLiveData<AfterScanFragment>()
    }
    private val _historyList = MutableLiveData<MutableList<MutableMap<String, Any>>>().apply {
        MutableLiveData<MutableList<MutableMap<String, String>>>()
    }

    fun init(activity: FragmentActivity, fragment: HistoryFragment){
        _activity.value = activity
        _fragment.value = fragment
        _afterScanFragment.value = AfterScanFragment()
    }
    fun getHistoryData(){
        viewModelScope.launch {
            val historyList: MutableList<MutableMap<String, Any>> = mutableListOf()
            var history: MutableMap<String, Any>
            // keyListを取得し、分割
            if (preferenceHistoryRepository.keyList(_activity.value!!).list.isNotBlank()){
                for (list in preferenceHistoryRepository.keyList(_activity.value!!).list.split("\n")){
                    // 対応したdataを取得
                    val data = preferenceHistoryRepository.read(_activity.value!!, keyName = list)
                    // mutableMapに代入
                    history = mutableMapOf(
                        History.Title.name to data.title,
                        History.Type.name to data.type,
                        History.Time.name to data.time
                    )
                    // mutableListに追加
                    historyList.add(history)
                }
                // livedataに代入
                _historyList.value = historyList

                // viewへ処理を渡す
                _fragment.value!!.recyclerView(_historyList.value!!)
            }
        }
    }
    fun setBundle(url: String){
        // bundleへデータセット
        val bundle = Bundle()
        bundle.putString(IntentBundle.ScanUrl.name, url)
        _afterScanFragment.value!!.arguments = bundle

        // viewへ処理を渡す
        _fragment.value!!.afterScanFragment()
    }
    fun delete(adapter: RecyclerViewAdapter, position: Int){
        preferenceHistoryRepository.delete(_activity.value!!, adapter.items[position][History.Title.name] as String)
    }

    fun afterScanFragment(): MutableLiveData<AfterScanFragment>{
        return _afterScanFragment
    }

    init {
        _historyList.value = mutableListOf()
    }
}