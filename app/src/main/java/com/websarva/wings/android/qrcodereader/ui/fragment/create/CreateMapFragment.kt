package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentCreateMapBinding
import com.websarva.wings.android.qrcodereader.viewmodel.CreateMapViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentCreateMapBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: CreateMapViewModel by viewModel()

    private lateinit var transaction: FragmentTransaction
    private lateinit var mMap: GoogleMap
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            transaction.setCustomAnimations(R.anim.nav_up_pop_enter_anim, R.anim.nav_up_pop_exit_anim)
            transaction.replace(R.id.container, SelectFragment()).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMapBinding.inflate(inflater, container, false)

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.title = ""
            it.show()
        }
        // オプションメニューの有効化
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        activity?.let {
            viewModel.init(it, this, DisplayFragment())
        }

        // 地図起動
        viewModel.initGoogleMap()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("result", "Permission Result")

        if (requestCode == 1000){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 現在地の取得の有効化
                enabledMyLocation()
            }else{
                Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // デフォルトの設定
        viewModel.getSavaLatLng().let {
            val location = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(location).title("Default Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14F))
            viewModel.setLatLng(it.latitude, it.longitude)
        }

        // ZoomIn ZoomOutの許可
        mMap.uiSettings.isZoomControlsEnabled = true

        // 地図タップ時の処理
        mMap.setOnMapClickListener {
            // map上のマーカを削除
            mMap.clear()
            // 新しいマーカを設定
            mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("${it.latitude} :${it.longitude}"))
            // 状態を保存
            viewModel.setLatLng(it.latitude, it.longitude)
        }

        // 現在地ボタンタップ時の処理
        mMap.setOnMyLocationButtonClickListener {
            mMap.myLocation.let {
                // map上のマーカを削除
                mMap.clear()
                // 新しいマーカを設定
                mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Now Location"))
                // 状態を保存
                viewModel.setLatLng(it.latitude, it.longitude)
            }
            false
        }

        // 権限の確認
        viewModel.checkPermission()
    }

    @SuppressLint("MissingPermission")
    fun enabledMyLocation(){
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        // actionBarのlocationButtonを非表示に
        menu.getItem(0).isVisible = false
    }
    fun displayFragment(){
        // displayFragmentへ遷移
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.container, viewModel.displayFragment().value!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        this.menu = menu

        inflater.inflate(R.menu.options_menu_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var retValue = true

        when(item.itemId){
            R.id.nowLocation ->{
                viewModel.checkPermission()
            }
            R.id.decision -> {
                viewModel.setBundle()
            }
            else -> retValue = super.onOptionsItemSelected(item)
        }

        return retValue
    }
}