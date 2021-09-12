package com.websarva.wings.android.qrcodereader.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.model.SaveLatLng
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.create.map.CreateMapFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment

class CreateMapViewModel(
    private val preferenceMapRepository: PreferenceMapRepositoryClient
): ViewModel() {
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _fragment = MutableLiveData<CreateMapFragment>().apply {
        MutableLiveData<CreateMapFragment>()
    }
    private val _displayFragment = MutableLiveData<DisplayFragment>().apply {
        MutableLiveData<DisplayFragment>()
    }
    private val _likelyPlaceLatLngs = MutableLiveData<LatLng>().apply {
        MutableLiveData<LatLng>()
    }
    private val _requestPermission = MutableLiveData<ActivityResultLauncher<String>>().apply {
        MutableLiveData<ActivityResultLauncher<String>>()
    }

    fun init(activity: FragmentActivity, fragment: CreateMapFragment, displayFragment: DisplayFragment){
        _activity.value = activity
        _fragment.value = fragment
        _displayFragment.value = displayFragment

        // requestPermissionの設定
        _requestPermission.value = _fragment.value!!.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            if (it){
                // 許可時の処理
                Log.i("result", "Permission Result")
                checkPermission()
            }else{
                // 拒否時の処理
                Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
            }
        }
    }
    fun checkPermission(){
        // 権限を既に取得しているかを確認
        if (ActivityCompat.checkSelfPermission(
                _fragment.value!!.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            Log.i("check", "GetPermission")

            // 現在地を取得ボタンの有効化
            _fragment.value!!.enabledMyLocation()
        }else{
            Log.i("check", "requestPermission")

            // requestPermissionの開始
            _requestPermission.value!!.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    fun initGoogleMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = _fragment.value!!.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(_fragment.value!!)
    }
    fun setBundle(){
        val bundle = Bundle()
        _likelyPlaceLatLngs.value?.let {
            // bundleに値をセット
            val url = "geo:0,0?q=${it.latitude},${it.longitude}"
            bundle.putString(IntentBundle.ScanUrl.name, url)
            bundle.putInt(IntentBundle.ScanType.name, 1)
            _displayFragment.value?.arguments = bundle

            // viewに処理を渡す
            _fragment.value!!.displayFragment()
        }
    }

    fun setLatLng(latitude: Double, longitude: Double){
        _likelyPlaceLatLngs.value = LatLng(latitude, longitude)
        preferenceMapRepository.write(_activity.value!!, SaveLatLng(latitude, longitude))
    }

    fun getSavaLatLng(): SaveLatLng{
        return preferenceMapRepository.read(_activity.value!!)
    }
    fun displayFragment(): MutableLiveData<DisplayFragment>{
        return _displayFragment
    }
}