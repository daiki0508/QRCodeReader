package com.websarva.wings.android.qrcodereader.ui.recyclerview.select

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment

class RecyclerViewAdapter(
    private val activity: FragmentActivity,
    private val fragment: SelectFragment
    ): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_select, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        when(position){
            0 -> {
                holder.title.text = "ウェブページ"
                holder.icon.setImageResource(R.drawable.ic_baseline_language_24_white)

                // タップ時の処理
                holder.view.setOnClickListener {
                    //TODO("未実装")
                }
            }
            1 -> {
                holder.title.text = "地図"
                holder.icon.setImageResource(R.drawable.ic_baseline_map_24_white)

                // タップ時の処理
                holder.view.setOnClickListener {
                    //TODO("未実装")
                }
            }
            else ->{
                Log.e("ERROR", "不正な操作が行われた可能性があります。")
                activity.let {
                    val fragmentManager = it.supportFragmentManager
                    fragmentManager.beginTransaction().remove(fragment).commit()
                    it.finish()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}