package com.websarva.wings.android.qrcodereader.ui.fragment.create.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentCreateAppsBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.DisplayFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.app.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.CreateAppsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAppsFragment: Fragment() {
    private var _binding: FragmentCreateAppsBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: CreateAppsViewModel by viewModel()

    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            transaction = it.supportFragmentManager.beginTransaction()
        }

        // バックボタンタップ時の処理
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            transaction.setCustomAnimations(
                R.anim.nav_up_pop_enter_anim,
                R.anim.nav_up_pop_exit_anim
            )
            transaction.replace(R.id.container, SelectFragment()).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAppsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // インストール済みアプリの情報取得
        viewModel.getAppsInfo()

        // recyclerViewの作成
        activity?.let {
            val appRecyclerViewAdapter = RecyclerViewAdapter(viewModel.appList().value!!, viewModel)
            binding.rvApps.adapter = appRecyclerViewAdapter
            binding.rvApps.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.rvApps.layoutManager = LinearLayoutManager(it)
        }

        // bundleのobserver
        viewModel.bundle().observe(this.viewLifecycleOwner, {
            DisplayFragment().apply {
                this.arguments = it

                // DisplayFragmentへの遷移
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                transaction.replace(R.id.container, this).commit()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}