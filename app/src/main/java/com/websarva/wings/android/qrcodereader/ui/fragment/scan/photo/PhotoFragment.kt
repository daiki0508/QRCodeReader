package com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentPhotoBinding
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.PhotoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment: Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: PhotoViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var transaction: FragmentTransaction
    private var type: Int = 1

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
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.hide()
        setHasOptionsMenu(true)

        // 初期設定
        viewModel.init(this)

        //qrcodeObserver
        viewModel.url().observe(this.viewLifecycleOwner, { url ->
            if (!url.isNullOrBlank()) {
                setQRCode(viewModel.qrcode().value!!, url)
                Log.d("test", "called")
            } else {
                arguments?.getInt(IntentBundle.ScanType.name, 1)?.let { type ->
                    this.type = type
                    Log.d("test2", type.toString())

                    when (type) {
                        1 -> {
                            // フォトライブラリを開く
                            viewModel.createGetDeviceImageIntent()
                        }
                        2 -> {
                            // 現在のfragmentをmainViewModelに通知
                            mainViewModel.setState(0)
                            viewModel.readQRCodeFromImage(
                                Uri.parse(
                                    arguments?.getString(
                                        IntentBundle.ScanUrl.name,
                                        ""
                                    )
                                )
                            )
                        }
                        else -> {
                        }
                    }
                }
            }
        })

        // bundleのobserver
        viewModel.bundle().observe(this.viewLifecycleOwner, {
            AfterScanFragment().apply {
                this.arguments = it

                // AfterFragmentへの遷移
                transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
                transaction.replace(R.id.container, this).commit()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.options_menu_map, menu)

        // actionBarのlocationButtonを非表示に
        menu.getItem(0).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var retValue = true

        when(item.itemId){
            android.R.id.home -> {
                if (type == 1){
                    // フォトライブラリを開く
                    viewModel.createGetDeviceImageIntent()
                }else{
                    transaction.replace(R.id.container, ScanFragment()).commit()
                }
            }
            R.id.decision -> viewModel.setBundle(type)
            else -> retValue = super.onOptionsItemSelected(item)
        }

        return retValue
    }

    private fun setQRCode(qrcode: Bitmap, url: String){
        binding.ivQR.setImageBitmap(qrcode)
        binding.tvUrl.text = url

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.title = "画像からスキャン"
            it.setDisplayHomeAsUpEnabled(true)
            it.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}