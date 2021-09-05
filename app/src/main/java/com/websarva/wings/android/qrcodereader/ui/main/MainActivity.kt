package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.afterscan.AfterScanActivity
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // fragmentの起動
        supportFragmentManager.beginTransaction().replace(binding.container.id, MainFragment()).commit()
    }
}