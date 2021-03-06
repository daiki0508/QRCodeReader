package com.websarva.wings.android.qrcodereader.ui.fragment.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentAfterscanBinding
import com.websarva.wings.android.qrcodereader.model.IntentBundle
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.afterscan.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.scan.AfterScanViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AfterScanFragment: Fragment() {
    private var _binding: FragmentAfterscanBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by sharedViewModel<AfterScanViewModel>()
    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            // transactionにTransactionを代入
            transaction = it.supportFragmentManager.beginTransaction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            mainViewModel.state().value?.let {
                if (it == 0) {
                    transaction.setCustomAnimations(
                        R.anim.nav_up_pop_enter_anim,
                        R.anim.nav_up_pop_exit_anim
                    )
                    when(arguments?.getInt(IntentBundle.ScanType.name)){
                        0 -> transaction.replace(R.id.container, CameraFragment()).commit()
                        1 -> {
                            transaction.replace(R.id.container, ScanFragment()).commit()
                        }
                        2 -> {
                            transaction.remove(this@AfterScanFragment).commit()
                            activity?.finish()
                        }else ->{
                        }
                    }
                } else if (it == 3) {
                    transaction.setCustomAnimations(
                        R.anim.nav_up_pop_enter_anim,
                        R.anim.nav_up_pop_exit_anim
                    )
                    transaction.replace(R.id.container, HistoryFragment()).commit()
                }
            }
        }
    }

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

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.hide()
        }

        // 初期設定
        viewModel.init(arguments?.getString(IntentBundle.ScanUrl.name)!!, this)
    }

    fun afterValidationCheck(valFlag: Boolean, type: Int?){
        // OKはtrue
        if (valFlag){
            binding.scanUrl.text = viewModel.scanUri().value.toString()
            viewModel.historySave()
            createRecyclerView(type!!)
        }else{
            Log.e("ERROR", "不正な操作が行われた可能性があります。")
            Toast.makeText(activity, R.string.no_support_qrcode, Toast.LENGTH_LONG).show()
            activity?.let {
                transaction.remove(this).commit()
                it.finish()
            }
        }
    }
    private fun createRecyclerView(type: Int){
        // recyclerview
        activity?.let {
            val actionRecyclerViewAdapter = RecyclerViewAdapter(it, viewModel.scanUri().value!!, type)
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