package com.websarva.wings.android.qrcodereader.ui.fragment.history

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.showAlignBottom
import com.skydoves.balloon.showAlignTop
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentHistoryBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.afterscan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.settings.SettingsFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.history.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.HistoryViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment: Fragment(), View.OnCreateContextMenuListener{
    private var _binding: FragmentHistoryBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel: HistoryViewModel by viewModel()
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val bottomNavViewModel by activityViewModels<BottomNavViewModel>()

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // toolBarに関する設定
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.hide()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        mainViewModel.setState(3)
        viewModel.init(this)
        transaction = activity?.supportFragmentManager!!.beginTransaction()

        // 履歴データの取得
        viewModel.getHistoryData()

        // contextMenuの登録
        registerForContextMenu(binding.rvHistory)

        if (viewModel.showBalloonFlag()){
            with(viewModel){
                bottomNavViewModel.let {
                    binding.tvNoContents.let { tv ->
                        // balloonの表示順番を設定
                        it.bottomNavBalloonHistory().value!!
                            .relayShowAlignBottom(historyBalloon().value!!, tv)
                            .relayShowAlignBottom(historyBalloon2().value!!, tv)
                            .relayShowAlignBottom(historyBalloon3().value!!, tv)

                        // balloonを表示
                        it.bottomNavView().value!!.showAlignTop(it.bottomNavBalloonHistory().value!!)

                        // 3つめのballoonが非表示になった際の処理
                        historyBalloon3().value!!.setOnBalloonDismissListener {
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, SettingsFragment()).commit()
                        }
                    }
                }
            }
        }

        // historyListのobserver
        viewModel.historyList().observe(this.viewLifecycleOwner, {
            recyclerView(it)
        })

        // bundleのobserver
        viewModel.bundle().observe(this.viewLifecycleOwner, {
            this.arguments = it

            // afterScanFragmentへの遷移
            AfterScanFragment().apply {
                transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)
                transaction.replace(R.id.container, this).commit()
            }
        })
    }

    private fun recyclerView(items: MutableList<MutableMap<String, Any>>){
        activity?.let {
            adapter = RecyclerViewAdapter(items, viewModel, this)
            binding.rvHistory.adapter = adapter
            binding.rvHistory.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
            binding.rvHistory.layoutManager = LinearLayoutManager(it)

            val itemTouchHelper = ItemTouchHelper(adapter.getRecyclerViewSimpleCallBack())
            itemTouchHelper.attachToRecyclerView(binding.rvHistory)

            if (adapter.itemCount != 0){
                // NoContentsを非表示
                binding.tvNoContents.visibility = View.GONE
            }
        }
    }
    fun contextItemClick(){
        // タップ時のpositionを取得
        val position = adapter.getPosition()

        // preferenceからも削除
        viewModel.delete(adapter, position)

        // positionから該当履歴を削除
        adapter.items.removeAt(position)
        if (adapter.itemCount == 0){
            binding.rvHistory.visibility = View.GONE
            binding.tvNoContents.visibility = View.VISIBLE
        }
        // 削除をRecyclerViewに通知
        adapter.notifyItemRemoved(position)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.history_context)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d("context", "Called!")
        var retValue = true

        when(item.itemId){
            1 -> {
                contextItemClick()
            }
            else -> retValue = super.onContextItemSelected(item)
        }

        return retValue
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}