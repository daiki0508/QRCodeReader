package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.photo.PhotoFragment
import com.websarva.wings.android.qrcodereader.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // アプリのテーマ設定
        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        val themeId = preference.getString("theme", "0")
        if (themeId == "1"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // toolBarの設定
        setSupportActionBar(binding.toolbar)
        supportActionBar?.hide()

        // fragmentの起動
        supportFragmentManager.beginTransaction().replace(binding.fragment.id, BottomNavFragment()).commit()

        if (viewModel.state().value == null){
            if (intent.action != Intent.ACTION_SEND){
                Log.d("intent", "null")
                supportFragmentManager.beginTransaction().replace(binding.container.id, ScanFragment()).commit()
            }else{
                Log.d("intent", "intent")
                handleSendImage(intent)
            }
        }

        // bundleのobserver
        viewModel.bundle().observe(this, {
            if (it != null){
                PhotoFragment().apply {
                    this.arguments = it

                    // PhotoFragmentへ遷移
                    supportFragmentManager.beginTransaction().replace(binding.container.id, this)
                        .commit()
                }
            }else{
                this.exitError()
            }
        })
    }

    private fun handleSendImage(intent: Intent){
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // ShareSheetから読みだされた場合
            viewModel.validationCheck(it.toString(), this)
        }
    }
    private fun exitError(){
        Log.e("ERROR", "不正な操作が行われた可能性があります。")
        finish()
    }
}