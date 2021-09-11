package com.websarva.wings.android.qrcodereader.ui.recyclerview.create.app

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.App
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.RecyclerViewHolder

class RecyclerViewAdapter(
    private val items: MutableList<MutableMap<String, Any>>
    ): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_apps, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = items[position][App.Label.name] as String
        holder.icon.setImageDrawable(items[position][App.Icon.name] as Drawable)

        holder.view.setOnClickListener {
            // TODO("未実装")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}