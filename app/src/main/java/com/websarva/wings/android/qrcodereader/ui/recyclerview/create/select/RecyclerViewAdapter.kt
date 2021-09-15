package com.websarva.wings.android.qrcodereader.ui.recyclerview.create.select

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.showAlignBottom
import com.skydoves.balloon.showAlignTop
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.create.map.CreateMapFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.web.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.app.CreateAppsFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.RecyclerViewHolder
import com.websarva.wings.android.qrcodereader.viewmodel.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.SelectViewModel

class RecyclerViewAdapter(
    private val activity: FragmentActivity,
    private val fragment: SelectFragment,
    private val viewModel: SelectViewModel,
    private val bottomNavViewModel: BottomNavViewModel
    ): RecyclerView.Adapter<RecyclerViewHolder>() {
    private lateinit var transaction: FragmentTransaction

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_select, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // transactionにTransactionを代入
        transaction = activity.supportFragmentManager.beginTransaction()
        // カスタムアニメーションの設定
        transaction.setCustomAnimations(R.anim.nav_up_enter_anim, R.anim.nav_up_exit_anim)

        when(position){
            0 -> {
                holder.title.text = "ウェブページ"
                holder.icon.setImageResource(R.drawable.ic_baseline_language_24_white)

                // タップ時の処理
                holder.view.setOnClickListener {
                    transaction.replace(R.id.container, CreateUrlFragment()).commit()
                }

                // holderViewの保存
                viewModel.setHolderView0(holder.view)
            }
            1 -> {
                holder.title.text = "地図"
                holder.icon.setImageResource(R.drawable.ic_baseline_map_24_white)

                // タップ時の処理
                holder.view.setOnClickListener {
                    holder.view.setOnClickListener {
                        transaction.replace(R.id.container, CreateMapFragment()).commit()
                    }
                }

                // holderViewの保存
                viewModel.setHolderView1(holder.view)
            }
            2 -> {
                holder.title.text = "アプリ"
                holder.icon.setImageResource(R.drawable.ic_baseline_apps_24_white)

                // タップ時の処理
                holder.view.setOnClickListener {
                    holder.view.setOnClickListener {
                        transaction.replace(R.id.container, CreateAppsFragment()).commit()
                    }
                }

                with(viewModel){
                    // balloonの表示順番設定
                    bottomNavViewModel.let {
                        it.bottomNavBalloonCreate().value!!
                            .relayShowAlignBottom(recyclerViewBalloonWeb().value!!, holderView0().value!!)
                            .relayShowAlignBottom(recyclerViewBalloonMap().value!!, holderView1().value!!)
                            .relayShowAlignBottom(recyclerViewBalloonApp().value!!, holder.view)

                        if (viewModel.showBalloonFlag()){
                            // balloonの表示
                            it.bottomNavView().value!!.showAlignTop(it.bottomNavBalloonCreate().value!!)
                        }

                        // アプリのballoonが非表示になった際の処理
                        recyclerViewBalloonApp().value!!.setOnBalloonDismissListener {
                            // HistoryFragmentへの遷移
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, HistoryFragment()).commit()
                        }
                    }
                }
            }
            else ->{
                Log.e("ERROR", "不正な操作が行われた可能性があります。")
                activity.let {
                    transaction.remove(fragment).commit()
                    it.finish()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}