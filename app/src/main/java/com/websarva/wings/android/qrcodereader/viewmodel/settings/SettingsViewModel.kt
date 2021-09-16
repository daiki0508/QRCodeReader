package com.websarva.wings.android.qrcodereader.viewmodel.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient

class SettingsViewModel(
    private val preferenceBalloonRepository: PreferenceBalloonRepositoryClient,
    application: Application
) : AndroidViewModel(application) {

    fun showBalloonFlag(): Boolean{
        return with(getApplication<Application>().applicationContext) {
            // 初回起動かどうかを判断
            preferenceBalloonRepository.read(this)
        }
    }
    fun write(){
        // チュートリアルの終了
        preferenceBalloonRepository.save(getApplication<Application>().applicationContext)
    }
}