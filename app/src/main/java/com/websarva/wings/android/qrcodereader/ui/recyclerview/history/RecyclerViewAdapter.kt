package com.websarva.wings.android.qrcodereader.ui.recyclerview.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.History

class RecyclerViewAdapter(private val items: MutableList<MutableMap<String, Any>>): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_history, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = items[position][History.Title.name] as String
        holder.date.text = items[position][History.Time.name] as String
        holder.icon.setImageResource(R.drawable.ic_baseline_open_in_browser_24)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}