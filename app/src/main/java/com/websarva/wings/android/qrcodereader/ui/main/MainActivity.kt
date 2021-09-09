package com.websarva.wings.android.qrcodereader.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
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

        // fragmentの起動
        if (viewModel.state().value == null){
            supportFragmentManager.beginTransaction().replace(binding.container.id, ScanFragment()).commit()
            supportFragmentManager.beginTransaction().replace(binding.fragment.id, BottomNavFragment()).commit()
        }
    }
}