package com.websarva.wings.android.qrcodereader.ui.recyclerview.create.display

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.RecyclerViewHolder

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_select, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = "他のアプリで共有する"
        holder.icon.setImageResource(R.drawable.ic_baseline_share_24_white)

        // タップ時の処理
        holder.view.setOnClickListener {
            //TODO("未実装")
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}