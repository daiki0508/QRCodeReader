package com.websarva.wings.android.qrcodereader.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class BottomNavViewModel: ViewModel() {
    private val _fragment = MutableLiveData<BottomNavFragment>().apply {
        MutableLiveData<BottomNavFragment>()
    }
    private val _bottomNavView = MutableLiveData<View>().apply {
        MutableLiveData<View>()
    }
    private val _bottomNavBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }

    fun init(fragment: BottomNavFragment, navView: View){
        _fragment.value = fragment
        _bottomNavView.value = navView

        // balloonの設定
        setBalloon()
    }
    private fun setBalloon(){
        _bottomNavBalloon.value = createBalloon("hogehoge")
    }
    private fun createBalloon(text: String): Balloon{
        return com.skydoves.balloon.createBalloon(_fragment.value!!.requireContext()) {
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
            setLifecycleOwner(_fragment.value!!.activity)
        }
    }

    fun bottomNavView(): MutableLiveData<View>{
        return _bottomNavView
    }
    fun bottomNavBalloon(): MutableLiveData<Balloon>{
        return _bottomNavBalloon
    }
}