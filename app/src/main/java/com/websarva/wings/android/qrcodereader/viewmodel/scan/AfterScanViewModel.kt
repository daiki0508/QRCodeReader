package com.websarva.wings.android.qrcodereader.viewmodel.scan

import android.app.Application
import android.net.Uri
import android.text.format.DateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.qrcodereader.model.SaveData
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.AfterScanFragment
import kotlinx.coroutines.launch
import java.util.*

class AfterScanViewModel(
    private val preferenceHistoryRepository: PreferenceHistoryRepositoryClient,
    application: Application
) : AndroidViewModel(application) {
    private val _scanUri =  MutableLiveData<Uri>()

    fun init(scanUrl: String, fragment: AfterScanFragment){
        // グローバル変数に設定を代入
        _scanUri.value = Uri.parse(scanUrl)

        // ヴァリデーションチェック
        validationCheck(fragment)
    }
    private fun validationCheck(fragment: AfterScanFragment){
        // viewへ処理を渡す
        when(_scanUri.value!!.scheme){
            "http", "https" -> {
                fragment.afterValidationCheck(valFlag = true, 0)
            }
            "geo" -> {
                fragment.afterValidationCheck(valFlag = true, 1)
            }
            else -> {
                fragment.afterValidationCheck(valFlag = false, type = null)
            }
        }
    }
    fun historySave(){
        viewModelScope.launch {
            // 履歴を作成、保存
            val date = DateFormat.format("yyyy/MM/dd kk:mm", Calendar.getInstance())
            _scanUri.value?.let {
                preferenceHistoryRepository.write(
                    getApplication<Application>().applicationContext,
                    keyName = it.toString(),
                    SaveData(
                        title = it.toString(),
                        type = if (it.scheme == "http" || it.scheme == "https"){
                            0
                        }else if (it.scheme == "geo") {
                            1
                        } else {
                            2
                        },
                        time = date.toString()
                    )
                )
            }
        }
    }

    fun scanUri(): MutableLiveData<Uri>{
        return _scanUri
    }
}