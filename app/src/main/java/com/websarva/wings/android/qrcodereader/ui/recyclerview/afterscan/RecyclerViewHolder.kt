package com.websarva.wings.android.qrcodereader.ui.recyclerview.afterscan

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view) {
    val icon: ImageView = view.findViewById(R.id.icon)
    val text: TextView = view.findViewById(R.id.title)
}