package com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentCameraBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.CameraViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CameraFragment: Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by sharedViewModel<CameraViewModel>()

    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
        }

        // バックボタンタップ時の処理
        requireActivity().onBackPressedDispatcher.addCallback(this){
            transaction.setCustomAnimations(R.anim.nav_up_pop_enter_anim, R.anim.nav_up_pop_exit_anim)
            transaction.replace(R.id.container, ScanFragment()).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel.init(it, binding.barcodeView, this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("result", "Permission Result")

        if (requestCode == 1001){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 権限確認後、scanResultの開始
                viewModel.checkPermission()
            }else{
                Log.w("Warning", "PERMISSION REQUEST WAS DENIED FOR USER")
            }
        }
    }

    fun afterScanFragment(){
        activity?.let {
            // afterScanFragmentへの遷移
            transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
            transaction.replace(R.id.container, viewModel.afterScanFragment().value!!).commit()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.barcodeView().value!!.resume()
    }

    override fun onPause() {
        super.onPause()

        viewModel.barcodeView().value!!.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}