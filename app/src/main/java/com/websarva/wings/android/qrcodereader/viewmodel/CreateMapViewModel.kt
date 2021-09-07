package com.websarva.wings.android.qrcodereader.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.SupportMapFragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateMapFragment

class CreateMapViewModel: ViewModel() {
    private val _activity = MutableLiveData<FragmentActivity>().apply {
        MutableLiveData<FragmentActivity>()
    }
    private val _fragment = MutableLiveData<CreateMapFragment>().apply {
        MutableLiveData<CreateMapFragment>()
    }

    fun init(activity: FragmentActivity, fragment: CreateMapFragment){
        _activity.value = activity
        _fragment.value = fragment
    }
    fun initGoogleMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = _fragment.value!!.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(_fragment.value!!)
    }
}