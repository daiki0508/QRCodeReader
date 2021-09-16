package com.websarva.wings.android.qrcodereader.viewmodel.create

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment

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
        getApplication<Application>().applicationContext?.let {
            _recyclerViewBalloonWeb.value = createBalloon(it.getString(R.string.create_description_balloon0), fragment)
            _recyclerViewBalloonMap.value = createBalloon(it.getString(R.string.create_description_balloon1), fragment)
            _recyclerViewBalloonApp.value = createBalloon(it.getString(R.string.create_description_balloon2), fragment)
        }
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

    fun showBalloonFlag(): Boolean{
        return with(getApplication<Application>().applicationContext) {
            // 初回起動かどうかを判断
            preferenceBalloonRepository.read(this)
        }
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