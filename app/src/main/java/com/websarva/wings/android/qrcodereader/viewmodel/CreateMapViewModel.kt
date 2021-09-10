package com.websarva.wings.android.qrcodereader.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.websarva.wings.android.qrcodereader.BuildConfig
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.SaveLatLng
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepository
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateMapFragment
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class CreateMapViewModel(
    private val preferenceMapRepository: PreferenceMapRepositoryClient
): ViewModel() {
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _fragment = MutableLiveData<CreateMapFragment>().apply {
        MutableLiveData<CreateMapFragment>()
    }
    private val _likelyPlaceLatLngs = MutableLiveData<LatLng>().apply {
        MutableLiveData<LatLng>()
    }

    fun init(activity: FragmentActivity, fragment: CreateMapFragment){
        _activity.value = activity
        _fragment.value = fragment
    }
    fun checkPermission(){
        // 権限を既に取得しているかを確認
        if (ActivityCompat.checkSelfPermission(
                _fragment.value!!.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            Log.d("check", "GetPermission")

            // 現在地を取得ボタンの有効化
            _fragment.value!!.enabledMyLocation()
        }else{
            Log.d("check", "requestPermission")
            _fragment.value!!.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        }
    }
    fun initGoogleMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = _fragment.value!!.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(_fragment.value!!)
    }

    fun setLatLng(latitude: Double, longitude: Double){
        _likelyPlaceLatLngs.value = LatLng(latitude, longitude)
        preferenceMapRepository.write(_activity.value!!, SaveLatLng(latitude, longitude))
    }

    fun getSavaLatLng(): SaveLatLng{
        return preferenceMapRepository.read(_activity.value!!)
    }
    fun likelyPlaceLatLngs(): MutableLiveData<LatLng>{
        return _likelyPlaceLatLngs
    }
}