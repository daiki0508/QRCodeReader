package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.databinding.FragmentCreateurlBinding
import com.websarva.wings.android.qrcodereader.viewmodel.CreateViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateUrlFragment: Fragment() {
    private var _binding: FragmentCreateurlBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: CreateViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateurlBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        viewModel.init(this)
        //mainViewModel.setState(2)

        // qrコード作成とvalidationCheck
        binding.btCreate.setOnClickListener {
            viewModel.validationCheck(binding.edUrl.text.toString())
        }

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
    fun exitError(){
        activity?.let {
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            val fragmentManager = it.supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            it.finish()
        }
    }
    fun displayQRCode(bitmap: Bitmap){
        binding.ivQR.setImageBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}