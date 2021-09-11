package com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentPhotoBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.viewmodel.PhotoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoFragment: Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: PhotoViewModel by viewModel()

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
        activity?.let {
            viewModel.url().observe(it, { url ->
                if (!url.isNullOrBlank()){
                    setQRCode(viewModel.qrcode().value!!, url)
                }else{
                    // フォトライブラリを開く
                    viewModel.createGetDeviceImageIntent()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            when(resultCode){
                Activity.RESULT_OK -> {
                    viewModel.readQRCodeFromImage(data?.data!!)
                }
                else -> {
                    Log.w("Warning", "ACTIVITY RESULT FAILURE")
                }
            }
        }
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
            android.R.id.home -> transaction.replace(R.id.container, PhotoFragment()).commit()
            R.id.decision -> viewModel.setBundle()
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
    fun afterScanFragment(){
        // afterScanFragmentへの遷移
        transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
        transaction.replace(R.id.container, viewModel.afterScanFragment().value!!).commit()
    }
}