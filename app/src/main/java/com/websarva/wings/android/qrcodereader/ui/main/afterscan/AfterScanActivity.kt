package com.websarva.wings.android.qrcodereader.ui.main.afterscan

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.websarva.wings.android.qrcodereader.databinding.ActivityAfterScanBinding
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class AfterScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterScanBinding

    private lateinit var scanUrl: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfterScanBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        scanUrl = Uri.parse(intent.getStringExtra("scanUrl"))

        if (scanUrl.scheme == "http" || scanUrl.scheme == "https"){
            binding.scanUrl.text = scanUrl.toString()
        }else{
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            finish()
        }
    }
}