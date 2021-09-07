package com.websarva.wings.android.qrcodereader.ui.recyclerview.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.viewmodel.HistoryViewModel

class RecyclerViewAdapter(
    private val items: MutableList<MutableMap<String, Any>>,
    private val viewModel: HistoryViewModel
): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_history, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = items[position][History.Title.name] as String
        holder.date.text = items[position][History.Time.name] as String
        holder.icon.setImageResource(
            when(items[position][History.Type.name]){
                0 -> R.drawable.ic_baseline_language_24
                else -> R.drawable.ic_baseline_open_in_browser_24
            }
        )

        // viewタップ時の処理
        holder.view.setOnClickListener {
            Log.d("recyclerview", "Click")
            viewModel.setBundle(items[position][History.Title.name] as String)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}