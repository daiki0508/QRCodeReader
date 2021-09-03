package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.main.afterscan.AfterScanActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var qrScanIntegrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        this.qrScanIntegrator = IntentIntegrator(this)
        // 縦画面に固定
        this.qrScanIntegrator.setOrientationLocked(false)
        // ヒープ音停止
        this.qrScanIntegrator.setBeepEnabled(false)
        // スキャン開始
        this.qrScanIntegrator.initiateScan()
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