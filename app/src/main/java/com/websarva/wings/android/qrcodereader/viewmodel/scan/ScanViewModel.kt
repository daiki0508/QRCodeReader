package com.websarva.wings.android.qrcodereader.viewmodel.scan

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment

class ScanViewModel(
    private val preferenceBalloonRepository: PreferenceBalloonRepositoryClient,
    application: Application
): AndroidViewModel(application) {
    private val _photoBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _cameraBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _bundle = MutableLiveData<Bundle>().apply {
        MutableLiveData<Bundle>()
    }

    fun init(fragment: ScanFragment){
        // balloonの設定
        setBalloon(fragment)
    }
    private fun setBalloon(fragment: ScanFragment){
        // scanFragment内のボタンの説明
        getApplication<Application>().applicationContext?.let {
            _cameraBalloon.value = createBalloon(it.getString(R.string.scan_description_balloon0), fragment)
            _photoBalloon.value = createBalloon(it.getString(R.string.scan_description_balloon1), fragment)
        }
    }
    private fun createBalloon(text: String, fragment: ScanFragment): Balloon{
        return createBalloon(getApplication<Application>().applicationContext) {
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
            setLifecycleOwner(fragment.viewLifecycleOwner)
        }
    }
    fun setBundle(){
        // bundleに値をセット
        val bundle = Bundle()
        bundle.putInt(IntentBundle.ScanType.name, 1)
        _bundle.value = bundle
    }

    fun showBalloonFlag(): Boolean{
        return with(getApplication<Application>().applicationContext) {
            // 初回起動かどうかを判断
            preferenceBalloonRepository.read(this)
        }
    }
    fun photoBalloon(): MutableLiveData<Balloon>{
        return _photoBalloon
    }
    fun cameraBalloon(): MutableLiveData<Balloon>{
        return _cameraBalloon
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }
}