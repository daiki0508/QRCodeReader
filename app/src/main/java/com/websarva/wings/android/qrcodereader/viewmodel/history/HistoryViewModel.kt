package com.websarva.wings.android.qrcodereader.viewmodel.history

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.history.RecyclerViewAdapter
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val preferenceHistoryRepository: PreferenceHistoryRepositoryClient,
    private val preferenceBalloonRepository: PreferenceBalloonRepositoryClient,
    application: Application
): AndroidViewModel(application) {
    private val _historyBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _historyBalloon2 = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _historyBalloon3 = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _historyList = MutableLiveData<MutableList<MutableMap<String, Any>>>().apply {
        MutableLiveData<MutableList<MutableMap<String, String>>>()
    }
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun init(fragment: HistoryFragment){
        // balloonの設定
        setBalloon(fragment)
    }
    private fun setBalloon(fragment: HistoryFragment){
        getApplication<Application>().applicationContext?.let {
            _historyBalloon.value = createBalloon(it.getString(R.string.history_description_balloon0), fragment)
            _historyBalloon2.value = createBalloon(it.getString(R.string.history_description_balloon1), fragment)
            _historyBalloon3.value = createBalloon(it.getString(R.string.history_description_balloon2), fragment)
        }
    }
    private fun createBalloon(text: String, fragment: HistoryFragment): Balloon{
        return com.skydoves.balloon.createBalloon(getApplication<Application>().applicationContext) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(65)
            setArrowPosition(0.7f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setText(text)
            setTextColorResource(R.color.white)
            //setTextIsHtml(true)
            //setIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
            setBackgroundColorResource(R.color.dodgerblue)
            //setOnBalloonClickListener(onBalloonClickListener)
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setIsVisibleOverlay(true)
            setOverlayColorResource(R.color.darkgray)
            setOverlayPadding(6f)
            setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
            setDismissWhenOverlayClicked(false)
            setOverlayShape(BalloonOverlayRect)
            setLifecycleOwner(fragment.viewLifecycleOwner)
        }
    }
    fun getHistoryData(){
        viewModelScope.launch {
            val historyList: MutableList<MutableMap<String, Any>> = mutableListOf()
            var history: MutableMap<String, Any>
            // keyListを取得し、分割
            getApplication<Application>().let {
                if (preferenceHistoryRepository.keyList(it).list.isNotBlank()) {
                    for (list in preferenceHistoryRepository.keyList(it).list.split("\n")) {
                        // 対応したdataを取得
                        val data =
                            preferenceHistoryRepository.read(it, keyName = list)
                        // mutableMapに代入
                        history = mutableMapOf(
                            History.Title.name to data.title,
                            History.Type.name to data.type,
                            History.Time.name to data.time
                        )
                        // mutableListに追加
                        historyList.add(history)
                    }
                }
                // livedataに代入
                _historyList.value = historyList
            }
        }
    }
    fun setBundle(url: String){
        // bundleへデータセット
        val bundle = Bundle()
        bundle.putString(IntentBundle.ScanUrl.name, url)
        _bundle.value = bundle
    }
    fun delete(adapter: RecyclerViewAdapter, position: Int){
        preferenceHistoryRepository.delete(getApplication<Application>().applicationContext, adapter.items[position][History.Title.name] as String)
    }

    fun showBalloonFlag(): Boolean{
        return with(getApplication<Application>().applicationContext) {
            // 初回起動かどうかを判断
            preferenceBalloonRepository.read(this)
        }
    }
    fun historyBalloon(): MutableLiveData<Balloon>{
        return _historyBalloon
    }
    fun historyBalloon2(): MutableLiveData<Balloon>{
        return _historyBalloon2
    }
    fun historyBalloon3(): MutableLiveData<Balloon>{
        return _historyBalloon3
    }
    fun historyList(): MutableLiveData<MutableList<MutableMap<String, Any>>>{
        return _historyList
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }
}