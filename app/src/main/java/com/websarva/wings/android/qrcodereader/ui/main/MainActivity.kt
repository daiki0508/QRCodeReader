package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.main.afterscan.AfterScanActivity
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private lateinit var qrScanIntegrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // qrコードライブラリの設定
        this.qrScanIntegrator = IntentIntegrator(this)
        viewModel.initQRScanIntegrator(this.qrScanIntegrator)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null){
            Log.d("scanResult", result.contents)
            Intent(this, AfterScanActivity::class.java).apply {
                this.putExtra("scanUrl", /*result.contents*/"file://hogehoge")
                startActivity(this)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}