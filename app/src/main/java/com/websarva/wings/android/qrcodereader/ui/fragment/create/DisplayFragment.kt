package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.databinding.FragmentDisplayBinding
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.viewmodel.DisplayViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayFragment: Fragment() {
    private var _binding: FragmentDisplayBinding? = null
    private val binding
    get() = _binding!!

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val viewModel: DisplayViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        mainViewModel.setState(5)
        viewModel.init(this)

        // ヴァリデーションチェック
        viewModel.validationCheck(arguments?.getString(IntentBundle.ScanUrl.name)!!)

        // qrObserver
        activity?.let {
            viewModel.qrBitmap().observe(it, { bitmap ->
                if (bitmap != null){
                    binding.ivQR.setImageBitmap(bitmap)
                }
            })
        }
    }

    fun blackToast(){
        activity?.let {
            Toast.makeText(it, "作成したいurlが入力されていません。", Toast.LENGTH_SHORT).show()
        }
    }
    fun exitError() {
        activity?.let {
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            val fragmentManager = it.supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            it.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}