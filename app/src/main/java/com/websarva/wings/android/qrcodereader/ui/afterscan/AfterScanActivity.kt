package com.websarva.wings.android.qrcodereader.ui.afterscan

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.ActivityAfterScanBinding
import com.websarva.wings.android.qrcodereader.ui.afterscan.recyclerView.RecyclerViewAdapter

class AfterScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterScanBinding

    private lateinit var scanUrl: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfterScanBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // intentを取得
        scanUrl = Uri.parse(intent.getStringExtra("scanUrl"))

        // ヴァリデーションチェック
        if (scanUrl.scheme == "http" || scanUrl.scheme == "https"){
            binding.scanUrl.text = scanUrl.toString()
        }else{
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            finish()
        }

        // recyclerview
        val actionRecyclerViewAdapter = RecyclerViewAdapter()
        binding.selectRecyclerView.adapter = actionRecyclerViewAdapter
        binding.selectRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.selectRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}