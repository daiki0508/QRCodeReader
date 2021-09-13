package com.websarva.wings.android.qrcodereader.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class BottomNavViewModel: ViewModel() {
    private val _fragment = MutableLiveData<BottomNavFragment>().apply {
        MutableLiveData<BottomNavFragment>()
    }
    private val _bottomNavView = MutableLiveData<View>().apply {
        MutableLiveData<View>()
    }
    private val _bottomNavBalloonScan = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _bottomNavBalloonCreate = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }

    fun init(fragment: BottomNavFragment, navView: View){
        _fragment.value = fragment
        _bottomNavView.value = navView

        // balloonの設定
        setBalloon()
    }
    private fun setBalloon(){
        // navigationのScanの説明
        _bottomNavBalloonScan.value = createBalloon().apply{
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_photo_camera_24)
            this.getContentView().findViewById<TextView>(R.id.title).text = "コード読み取り"
            this.getContentView().findViewById<TextView>(R.id.description).text = "主にQRコードのスキャンに関することが行えます"
        }

        // navigationのCreateの説明
        _bottomNavBalloonCreate.value = createBalloon().apply {
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_qr_code_24)
            this.getContentView().findViewById<TextView>(R.id.title).text = "コード生成"
            this.getContentView().findViewById<TextView>(R.id.description).text = "様々な種類のQRコードを作成できます"
        }
    }
    private fun createBalloon(): Balloon{
        return com.skydoves.balloon.createBalloon(_fragment.value!!.requireContext()) {
            setLayout(R.layout.nav_balloon)
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.7f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            //setText(text)
            //setTextColorResource(R.color.white)
            //setTextIsHtml(true)
            //setIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
            setBackgroundColorResource(R.color.darkorange)
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
    fun bottomNavBalloonScan(): MutableLiveData<Balloon>{
        return _bottomNavBalloonScan
    }
    fun bottomNavBalloonCreate(): MutableLiveData<Balloon>{
        return _bottomNavBalloonCreate
    }
}