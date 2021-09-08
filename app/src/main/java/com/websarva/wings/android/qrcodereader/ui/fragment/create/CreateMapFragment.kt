package com.websarva.wings.android.qrcodereader.ui.fragment.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        //mainViewModel.setState(6)
        activity?.let {
            viewModel.init(it, this)
        }

        // mapを起動
        viewModel.initGoogleMap()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //TODO("未実装")
        var location = LatLng(0.0, 0.0)
        mMap.addMarker(MarkerOptions().position(location).title("Now Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10F))

        // 地図タップ時の処理
        mMap.setOnMapClickListener {
            location = LatLng(it.latitude, it.longitude)
        }
    }
}