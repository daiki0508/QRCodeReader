package com.websarva.wings.android.qrcodereader.ui.recyclerview.history

import android.view.ContextMenu
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val icon: ImageView = view.findViewById(R.id.icon)
    val date: TextView = view.findViewById(R.id.date)
}