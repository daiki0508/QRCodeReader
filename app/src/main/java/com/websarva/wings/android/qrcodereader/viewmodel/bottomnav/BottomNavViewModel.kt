package com.websarva.wings.android.qrcodereader.viewmodel.bottomnav

import android.app.Application
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRect
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment

class BottomNavViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val _bottomNavView = MutableLiveData<View>()
    private val _bottomNavBalloonScan = MutableLiveData<Balloon>()
    private val _bottomNavBalloonCreate = MutableLiveData<Balloon>()
    private val _bottomNavBalloonHistory = MutableLiveData<Balloon>()
    private val _bottomNavBalloonSettings = MutableLiveData<Balloon>()
    private val _bottomNavScanCalled = MutableLiveData<Boolean>()
    private val _bottomNavCreateCalled = MutableLiveData<Boolean>()
    private val _bottomNavHistoryCalled = MutableLiveData<Boolean>()
    private val _bottomNavSettingsCalled = MutableLiveData<Boolean>()

    fun init(fragment: BottomNavFragment, navView: View){
        _bottomNavView.value = navView

        // balloonの設定
        setBalloon(fragment)
    }
    private fun setBalloon(fragment: BottomNavFragment){
        // navigationのScanの説明
        _bottomNavBalloonScan.value = createBalloon(fragment).apply{
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_photo_camera_24)
            getApplication<Application>().applicationContext?.let {
                this.getContentView().findViewById<TextView>(R.id.title).text = it.getString(R.string.nav_title_balloon_scan)
                this.getContentView().findViewById<TextView>(R.id.description).text = it.getString(R.string.nav_description_balloon_scan)
            }
        }

        // navigationのCreateの説明
        _bottomNavBalloonCreate.value = createBalloon(fragment).apply {
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_qr_code_24)
            getApplication<Application>().applicationContext?.let {
                this.getContentView().findViewById<TextView>(R.id.title).text = it.getString(R.string.nav_title_balloon_create)
                this.getContentView().findViewById<TextView>(R.id.description).text = it.getString(R.string.nav_description_balloon_create)
            }
        }

        // navigationのHistoryの説明
        _bottomNavBalloonHistory.value = createBalloon(fragment).apply {
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_history_24)
            getApplication<Application>().applicationContext?.let {
                this.getContentView().findViewById<TextView>(R.id.title).text = it.getString(R.string.nav_title_balloon_history)
                this.getContentView().findViewById<TextView>(R.id.description).text = it.getString(R.string.nav_description_balloon_history)
            }
        }

        // navigationのSettingsの説明
        _bottomNavBalloonSettings.value = createBalloon(fragment).apply {
            this.getContentView().findViewById<ImageView>(R.id.icon).setImageResource(R.drawable.ic_baseline_settings_24)
            getApplication<Application>().applicationContext?.let {
                this.getContentView().findViewById<TextView>(R.id.title).text = it.getString(R.string.nav_title_balloon_settings)
                this.getContentView().findViewById<TextView>(R.id.description).text = it.getString(R.string.nav_description_balloon_settings)
            }
        }
    }
    private fun createBalloon(fragment: BottomNavFragment): Balloon{
        return com.skydoves.balloon.createBalloon(getApplication<Application>().applicationContext) {
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
            setOverlayShape(BalloonOverlayRect)
            setLifecycleOwner(fragment.viewLifecycleOwner)
        }
    }

    fun setBottomNavScanCalled(){
        _bottomNavScanCalled.value = true
    }

    fun bottomNavView(): MutableLiveData<View>{
        return _bottomNavView
    }
    fun bottomNavBalloonScan(flag: Boolean): MutableLiveData<Balloon>{
        // bottomNavigationの状態を更新
        _bottomNavScanCalled.value = flag

        return _bottomNavBalloonScan
    }
    fun bottomNavBalloonCreate(): MutableLiveData<Balloon>{
        // bottomNavigationの状態を更新
        _bottomNavCreateCalled.value = true

        return _bottomNavBalloonCreate
    }
    fun bottomNavBalloonHistory(): MutableLiveData<Balloon>{
        // bottomNavigationの状態を更新
        _bottomNavHistoryCalled.value = true

        return _bottomNavBalloonHistory
    }
    fun bottomNavBalloonSettings(): MutableLiveData<Balloon>{
        // bottomNavigationの状態を更新
        _bottomNavSettingsCalled.value = true

        return _bottomNavBalloonSettings
    }
    fun bottomNavScanCalled(): MutableLiveData<Boolean>{
        return _bottomNavScanCalled
    }
    fun bottomNavCreateCalled(): MutableLiveData<Boolean>{
        return _bottomNavCreateCalled
    }
    fun bottomNavHistoryCalled(): MutableLiveData<Boolean>{
        return _bottomNavHistoryCalled
    }
    fun bottomNavSettingsCalled(): MutableLiveData<Boolean>{
        return _bottomNavSettingsCalled
    }
}