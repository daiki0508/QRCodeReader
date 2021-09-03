package com.websarva.wings.android.qrcodereader.ui.afterscan.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view) {
    val icon: ImageView = view.findViewById(R.id.actionIcon)
    val text: TextView = view.findViewById(R.id.actionText)
}