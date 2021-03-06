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
import com.skydoves.balloon.showAlignTop
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentHistoryBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.AfterScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.settings.SettingsFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.history.RecyclerViewAdapter
import com.websarva.wings.android.qrcodereader.viewmodel.bottomnav.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.history.HistoryViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.main.MainViewModel
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

        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        // toolBar??????????????????
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.hide()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ????????????
        mainViewModel.setState(3)
        viewModel.init(this)
        transaction = activity?.supportFragmentManager!!.beginTransaction()

        // ????????????????????????
        viewModel.getHistoryData()

        // contextMenu?????????
        registerForContextMenu(binding.rvHistory)

        if (viewModel.showBalloonFlag()){
            with(viewModel){
                bottomNavViewModel.let {
                    binding.tvNoContents.let { tv ->
                        // balloon????????????????????????
                        it.bottomNavBalloonHistory().value!!
                            .relayShowAlignBottom(historyBalloon().value!!, tv)
                            .relayShowAlignBottom(historyBalloon2().value!!, tv)
                            .relayShowAlignBottom(historyBalloon3().value!!, tv)

                        // balloon?????????
                        it.bottomNavView().value!!.showAlignTop(it.bottomNavBalloonHistory().value!!)

                        // 3?????????balloon????????????????????????????????????
                        historyBalloon3().value!!.setOnBalloonDismissListener {
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, SettingsFragment()).commit()
                        }
                    }
                }
            }
        }

        // historyList???observer
        viewModel.historyList().observe(this.viewLifecycleOwner, {
            recyclerView(it)
        })

        // bundle???observer
        viewModel.bundle().observe(this.viewLifecycleOwner, {
            this.arguments = it

            // afterScanFragment????????????
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
                // NoContents????????????
                binding.tvNoContents.visibility = View.GONE
            }
        }
    }
    fun contextItemClick(){
        // ???????????????position?????????
        val position = adapter.getPosition()

        // preference???????????????
        viewModel.delete(adapter, position)

        // position???????????????????????????
        adapter.items.removeAt(position)
        if (adapter.itemCount == 0){
            binding.rvHistory.visibility = View.GONE
            binding.tvNoContents.visibility = View.VISIBLE
        }
        // ?????????RecyclerView?????????
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