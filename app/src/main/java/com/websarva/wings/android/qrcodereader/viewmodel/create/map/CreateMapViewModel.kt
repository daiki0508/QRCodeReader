package com.websarva.wings.android.qrcodereader.viewmodel.create.map

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.model.SaveLatLng
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.create.map.CreateMapFragment

class CreateMapViewModel(
    private val preferenceMapRepository: PreferenceMapRepositoryClient, application: Application
): AndroidViewModel(application) {
    private val _likelyPlaceLatLngs = MutableLiveData<LatLng>()
    private val _requestPermission = MutableLiveData<ActivityResultLauncher<String>>()
    private val _bundle = MutableLiveData<Bundle>()

    fun init(fragment: CreateMapFragment){
        // requestPermissionの設定
        _requestPermission.value = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            if (it){
                // 許可時の処理
                Log.i("result", "Permission Result")
                checkPermission(fragment)
            }else{
                // 拒否時の処理
                Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
            }
        }
    }
    fun checkPermission(fragment: CreateMapFragment){
        // 権限を既に取得しているかを確認
        if (ActivityCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            Log.i("check", "GetPermission")

            // 現在地を取得ボタンの有効化
            fragment.enabledMyLocation()
        }else{
            Log.i("check", "requestPermission")

            // requestPermissionの開始
            _requestPermission.value!!.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    fun initGoogleMap(fragment: CreateMapFragment){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = fragment.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(fragment)
    }
    fun setBundle(){
        val bundle = Bundle()
        _likelyPlaceLatLngs.value?.let {
            // bundleに値をセット
            val url = "geo:0,0?q=${it.latitude},${it.longitude}"
            bundle.putString(IntentBundle.ScanUrl.name, url)
            bundle.putInt(IntentBundle.ScanType.name, 1)
            _bundle.value = bundle
        }
    }

    fun setLatLng(latitude: Double, longitude: Double){
        _likelyPlaceLatLngs.value = LatLng(latitude, longitude)
        preferenceMapRepository.write(
            getApplication<Application>().applicationContext,
            SaveLatLng(latitude, longitude)
        )
    }

    fun getSavaLatLng(): SaveLatLng{
        return preferenceMapRepository.read(getApplication<Application>().applicationContext)
    }
    fun bundle(): MutableLiveData<Bundle>{
        return _bundle
    }
}