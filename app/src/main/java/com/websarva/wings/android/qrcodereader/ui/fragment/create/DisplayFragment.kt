package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentDisplayBinding
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.create.web.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.display.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.DisplayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayFragment: Fragment() {
    private var _binding: FragmentDisplayBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: DisplayViewModel by viewModel()

    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // transactionにTransactionを代入
        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            transaction.replace(R.id.container, CreateUrlFragment()).commit()
        }
    }

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
        viewModel.init(this)

        // ヴァリデーションチェック
        viewModel.validationCheck(
            arguments?.getString(IntentBundle.ScanUrl.name)!!,
            arguments?.getInt(IntentBundle.ScanType.name)!!
        )

        // qrObserver
        activity?.let {
            viewModel.qrBitmap().observe(it, { bitmap ->
                if (bitmap != null){
                    // 生成したQRコードをImageViewにセット
                    binding.ivQR.setImageBitmap(bitmap)
                    // textViewにUrlをセット
                    binding.tvUrl.text = arguments?.getString(IntentBundle.ScanUrl.name)

                    // recyclerViewの生成
                    val displayRecyclerViewAdapter = RecyclerViewAdapter(viewModel, it)
                    binding.rvShare.adapter = displayRecyclerViewAdapter
                    binding.rvShare.layoutManager = LinearLayoutManager(it)
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

    override fun onStop() {
        // キャッシュの削除
        viewModel.deleteChaChe()

        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // bindingの解放
        _binding = null
    }
}