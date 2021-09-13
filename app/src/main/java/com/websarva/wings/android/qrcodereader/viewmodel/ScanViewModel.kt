package com.websarva.wings.android.qrcodereader.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment

class ScanViewModel(
    private val preferenceBalloonRepository: PreferenceBalloonRepositoryClient
): ViewModel() {
    private val _fragment = MutableLiveData<ScanFragment>().apply {
        MutableLiveData<ScanFragment>()
    }
    private val _photoFragment = MutableLiveData<PhotoFragment>().apply {
        MutableLiveData<PhotoFragment>()
    }
    private val _photoBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }
    private val _cameraBalloon = MutableLiveData<Balloon>().apply {
        MutableLiveData<Balloon>()
    }

    fun init(fragment: ScanFragment){
        _fragment.value = fragment

        // balloonの設定
        setBalloon()
    }
    private fun setBalloon(){
        // scanFragment内のボタンの説明
        _cameraBalloon.value = createBalloon("スマホのカメラ機能を用いてQRコードをスキャン出来ます", flag = false)
        _photoBalloon.value = createBalloon("端末内に保存されている画像や、ドライブからQRコードをインポートできます", flag = true)
    }
    private fun createBalloon(text: String, flag: Boolean): Balloon{
        return createBalloon(_fragment.value!!.requireContext()) {
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
            // trueならこのページでのballoonは終了
            if (flag){
                setOnBalloonDismissListener {
                    val transaction = _fragment.value!!.activity?.supportFragmentManager?.beginTransaction()
                    transaction!!.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                    transaction.replace(R.id.container, SelectFragment()).commit()
                }
            }
            setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            setIsVisibleOverlay(true)
            setOverlayColorResource(R.color.darkgray)
            setOverlayPadding(6f)
            setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
            setDismissWhenOverlayClicked(false)
            setLifecycleOwner(_fragment.value!!.activity)
        }
    }
    fun setBundle(){
        _photoFragment.value = PhotoFragment()

        // bundleに値をセット
        val bundle = Bundle()
        bundle.putInt(IntentBundle.ScanType.name, 0)
        _photoFragment.value!!.arguments = bundle

        // viewへ処理を渡す
        _fragment.value!!.photoFragment()
    }

    fun showBalloonFlag(): Boolean?{
        var flag: Boolean? = null
        _fragment.value!!.activity?.let {
            // 初回起動かどうかを判断
            flag = preferenceBalloonRepository.read(it)
            preferenceBalloonRepository.save(it)
        }
        return flag
    }
    fun photoFragment(): MutableLiveData<PhotoFragment>{
        return _photoFragment
    }
    fun photoBalloon(): MutableLiveData<Balloon>{
        return _photoBalloon
    }
    fun cameraBalloon(): MutableLiveData<Balloon>{
        return _cameraBalloon
    }
}