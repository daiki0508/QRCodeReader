package com.websarva.wings.android.qrcodereader.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.websarva.wings.android.qrcodereader.databinding.ActivityMainBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav.BottomNavFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // fragmentの起動
        supportFragmentManager.beginTransaction().replace(binding.container.id, MainFragment()).commit()
        supportFragmentManager.beginTransaction().replace(binding.fragment.id, BottomNavFragment()).commit()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStack()
    }
}