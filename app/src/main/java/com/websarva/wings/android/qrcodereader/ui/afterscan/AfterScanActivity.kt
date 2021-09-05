package com.websarva.wings.android.qrcodereader.ui.afterscan

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.ActivityAfterScanBinding
import com.websarva.wings.android.qrcodereader.ui.afterscan.recyclerView.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.ui.main.MainActivity

class AfterScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAfterScanBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // fragmentの起動
        supportFragmentManager.beginTransaction().replace(binding.container.id, AfterScanFragment()).commit()
    }

    override fun onBackPressed() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
    }
}