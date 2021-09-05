package com.websarva.wings.android.qrcodereader.ui.afterscan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.databinding.FragmentAfterscanBinding
import com.websarva.wings.android.qrcodereader.ui.afterscan.recyclerView.RecyclerViewAdapter

class AfterScanFragment: Fragment() {
    private var _binding: FragmentAfterscanBinding? = null
    private val binding
    get() = _binding!!

    private lateinit var scanUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfterscanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // intentを取得
        activity?.let {
            scanUri = Uri.parse(it.intent.getStringExtra("scanUrl"))

            // ヴァリデーションチェック
            if (scanUri.scheme == "http" || scanUri.scheme == "https"){
                binding.scanUrl.text = scanUri.toString()
            }else{
                Log.e("ERROR", "不正な操作が行われた可能性があります。")
                val fragmentManager = it.supportFragmentManager
                fragmentManager.beginTransaction().remove(this).commit()
                it.finish()
            }

            // recyclerview
            val actionRecyclerViewAdapter = RecyclerViewAdapter(it, scanUri)
            binding.selectRecyclerView.adapter = actionRecyclerViewAdapter
            binding.selectRecyclerView.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.selectRecyclerView.layoutManager = LinearLayoutManager(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}