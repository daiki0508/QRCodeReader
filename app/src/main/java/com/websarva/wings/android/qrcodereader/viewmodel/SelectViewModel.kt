package com.websarva.wings.android.qrcodereader.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment

class SelectViewModel(
    private val preferenceBalloonRepository: PreferenceBalloonRepositoryClient,
    application: Application
): AndroidViewModel(application) {
    private val _recyclerViewBalloonWeb = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _recyclerViewBalloonMap = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _recyclerViewBalloonApp = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _holderView0 = MutableLiveData<View>().apply {
        MutableLiveData<View>()
    }
    private val _holderView1 = MutableLiveData<View>().apply {
        MutableLiveData<View>()
    }

    fun init(fragment: SelectFragment){
        // balloonの設定
        setBalloon(fragment)
    }
    private fun setBalloon(fragment: SelectFragment){
        _recyclerViewBalloonWeb.value = createBalloon("http,httpsから始まるウェブページのQRコードを作成できます。", fragment)
        _recyclerViewBalloonMap.value = createBalloon("現在地などの好きな場所の座標をQRコード化できます", fragment)
        _recyclerViewBalloonApp.value = createBalloon("端末内にインストールされているアプリのダウンロードQRコードを作成できます", fragment)
    }
    private fun createBalloon(text: String, fragment: SelectFragment): Balloon{
        return com.skydoves.balloon.createBalloon(getApplication<Application>().applicationContext) {
            //setLayout(R.layout.nav_balloon)
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

    fun setHolderView0(holderView: View){
        _holderView0.value = holderView
    }
    fun setHolderView1(holderView: View){
        _holderView1.value = holderView
    }

    fun showBalloonFlag(): Boolean?{
        var flag: Boolean? = null
        getApplication<Application>().applicationContext.let {
            // 初回起動かどうかを判断
            flag = preferenceBalloonRepository.read(it)
        }
        return flag
    }
    fun recyclerViewBalloonWeb(): MutableLiveData<Balloon>{
        return _recyclerViewBalloonWeb
    }
    fun recyclerViewBalloonMap(): MutableLiveData<Balloon>{
        return _recyclerViewBalloonMap
    }
    fun recyclerViewBalloonApp(): MutableLiveData<Balloon>{
        return _recyclerViewBalloonApp
    }
    fun holderView0(): MutableLiveData<View>{
        return _holderView0
    }
    fun holderView1(): MutableLiveData<View>{
        return _holderView1
    }
}