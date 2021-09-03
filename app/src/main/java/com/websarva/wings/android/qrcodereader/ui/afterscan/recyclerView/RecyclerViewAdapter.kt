package com.websarva.wings.android.qrcodereader.ui.afterscan.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R

class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_afterscan, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        when(position){
            0 -> {
                holder.icon.setImageResource(R.drawable.ic_baseline_open_in_browser_24)
                holder.text.text = "ブラウザで開く"
            }
            1 -> {
                holder.icon.setImageResource(R.drawable.ic_baseline_content_copy_24)
                holder.text.text = "コピーする"
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}