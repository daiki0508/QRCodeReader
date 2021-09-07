package com.websarva.wings.android.qrcodereader.ui.fragment.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentCreateurlBinding
import com.websarva.wings.android.qrcodereader.viewmodel.CreateUrlViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateUrlFragment: Fragment() {
    private var _binding: FragmentCreateurlBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: CreateUrlViewModel by viewModel()
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
        mainViewModel.setState(4)
        viewModel.init(this, DisplayFragment())

        // qrコード作成とvalidationCheck
        binding.btCreate.setOnClickListener {
            viewModel.setBundle(binding.edUrl.text.toString())
        }
    }
    fun displayFragment(){
        activity?.let {
            val fragmentManager = it.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            transaction.replace(R.id.container, viewModel.displayFragment().value!!).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}