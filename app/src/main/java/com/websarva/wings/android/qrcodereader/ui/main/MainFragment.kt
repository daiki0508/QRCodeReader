package com.websarva.wings.android.qrcodereader.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.websarva.wings.android.qrcodereader.databinding.FragmentMainBinding
import com.websarva.wings.android.qrcodereader.ui.afterscan.AfterScanActivity
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding
    get() = _binding!!
    private val viewModel by sharedViewModel<MainViewModel>()

    private lateinit var qrScanIntegrator: IntentIntegrator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // qrコードライブラリの設定
            this.qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
            viewModel.initQRScanIntegrator(this.qrScanIntegrator)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null){
            Log.d("scanResult", result.contents)
            activity?.let {
                Intent(it, AfterScanActivity::class.java).apply {
                    this.putExtra("scanUrl", result.contents)
                    startActivity(this)
                    it.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    val fragmentManager = it.supportFragmentManager
                    fragmentManager.beginTransaction().remove(this@MainFragment).commit()
                    it.finish()
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}