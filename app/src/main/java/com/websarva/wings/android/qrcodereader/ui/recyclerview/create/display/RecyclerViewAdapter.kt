package com.websarva.wings.android.qrcodereader.ui.recyclerview.create.display

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.recyclerview.create.RecyclerViewHolder
import com.websarva.wings.android.qrcodereader.viewmodel.create.DisplayViewModel

class RecyclerViewAdapter(
    private val viewModel: DisplayViewModel,
    private val activity: FragmentActivity
    ): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_select, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = activity.getString(R.string.rv_title_share)
        holder.icon.setImageResource(R.drawable.ic_baseline_share_24_white)

        // タップ時の処理
        holder.view.setOnClickListener {
            Intent().apply {
                this.action = Intent.ACTION_SEND
                this.type = "image/jpeg"
                this.putExtra(Intent.EXTRA_STREAM, viewModel.getQRCodeUri().value)

                // 共有開始
                try {
                    activity.startActivity(Intent.createChooser(this, activity.getString(R.string.share_sheet_title)))
                } catch (e: ActivityNotFoundException) {
                    Log.w("Warning", "SHARE ACTIVITY NOT FOUND")
                    Toast.makeText(activity, activity.getString(R.string.no_share_apps), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}