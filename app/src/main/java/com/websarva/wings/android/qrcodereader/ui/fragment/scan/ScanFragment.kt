package com.websarva.wings.android.qrcodereader.ui.fragment.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentScanBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.ScanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanFragment: Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: ScanViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.hide()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 現在のfragmentをmainViewModelに通知
        mainViewModel.setState(0)

        // 初期設定
        viewModel.init(this)
        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
        }

        // カメラでスキャンボタンをタップ時の処理
        binding.btCamera.setOnClickListener {
            transaction.replace(R.id.container, CameraFragment()).commit()
        }

        // 画像からスキャンボタンをタップ時の処理
        binding.btPhoto.setOnClickListener {
            viewModel.setBundle()
        }
    }

    fun photoFragment(){
        transaction.replace(R.id.container, viewModel.photoFragment().value!!).commit()
    }
}