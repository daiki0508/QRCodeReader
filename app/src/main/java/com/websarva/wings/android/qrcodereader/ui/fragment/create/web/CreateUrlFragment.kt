package com.websarva.wings.android.qrcodereader.ui.fragment.create.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentCreateurlBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.viewmodel.CreateUrlViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateUrlFragment: Fragment() {
    private var _binding: FragmentCreateurlBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: CreateUrlViewModel by viewModel()

    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // transactionにTransactionを代入
        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this){
            transaction.setCustomAnimations(R.anim.nav_up_pop_enter_anim, R.anim.nav_up_pop_exit_anim)
            transaction.replace(R.id.container, SelectFragment()).commit()
        }
    }

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
        viewModel.init(this, DisplayFragment())

        // qrコード作成と空文字チェック
        binding.btCreate.setOnClickListener {
            viewModel.setBundle(binding.edUrl.text.toString())
        }
    }
    fun blackToast(){
        // Toastを表示
        activity?.let {
            Toast.makeText(it, "作成したいurlが入力されていません。", Toast.LENGTH_SHORT).show()
        }
    }
    fun displayFragment(){
        // displayFragmentへ遷移
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.container, viewModel.displayFragment().value!!).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}