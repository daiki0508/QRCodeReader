package com.websarva.wings.android.qrcodereader.ui.fragment.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.skydoves.balloon.*
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentScanBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment
import com.websarva.wings.android.qrcodereader.viewmodel.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.ScanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanFragment: Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: ScanViewModel by viewModel()
    private val bottomNavViewModel: BottomNavViewModel by activityViewModels()
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
        }

        // カメラでスキャンボタンをタップ時の処理
        binding.btCamera.setOnClickListener {
            transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
            transaction.replace(R.id.container, CameraFragment()).commit()
        }

        // 画像からスキャンボタンをタップ時の処理
        binding.btPhoto.setOnClickListener {
            viewModel.setBundle()
        }

        // bottomNavのobserver
        bottomNavViewModel.bottomNavView().observe(requireActivity(), {
            // trueなら表示
            if (viewModel.showBalloonFlag()) {
                // balloonの表示順番を設定
                with(viewModel){
                    bottomNavViewModel.let {
                        it.bottomNavBalloonScan(flag = true).value!!
                            .relayShowAlignTop(cameraBalloon().value!!, binding.btCamera)
                            .relayShowAlignBottom(photoBalloon().value!!, binding.btPhoto)

                        // balloonを表示
                        it.bottomNavView().value!!.showAlignTop(it.bottomNavBalloonScan(flag = true).value!!)

                        // photoBalloon、終了時の処理
                        photoBalloon().value!!.setOnBalloonDismissListener {
                            // selectFragmentへ遷移
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, SelectFragment()).commit()
                        }
                    }
                }
            }else{
                bottomNavViewModel.bottomNavBalloonScan(flag = false)
            }
        })

        // bundleのobserver
        viewModel.bundle().observe(this.viewLifecycleOwner, {
            PhotoFragment().apply {
                this.arguments = it
                transaction.replace(R.id.container, this).commit()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}