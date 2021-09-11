package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // toolBarの設定
        setSupportActionBar(binding.toolbar)
        supportActionBar?.hide()

        //初期設定
        viewModel.init(this)

        // fragmentの起動
        if (viewModel.state().value == null){
            if (intent.action != Intent.ACTION_SEND){
                Log.d("intent", "null")
                supportFragmentManager.beginTransaction().replace(binding.container.id, ScanFragment()).commit()
            }else{
                Log.d("intent", "intent")
                handleSendImage(intent)
            }
            supportFragmentManager.beginTransaction().replace(binding.fragment.id, BottomNavFragment()).commit()
        }
    }

    private fun handleSendImage(intent: Intent){
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // ShareSheetから読みだされた場合
            Log.d("url", it.toString())
            viewModel.validationCheck(it.toString())
        }
    }
    fun afterScanFragment(){
        supportFragmentManager.beginTransaction().replace(binding.container.id, viewModel.photoFragment().value!!).commit()
    }
    fun exitError(){
        Log.e("ERROR", "不正な操作が行われた可能性があります。")
        finish()
    }
}