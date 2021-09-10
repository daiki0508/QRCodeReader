package com.websarva.wings.android.qrcodereader.ui.recyclerview.afterscan

import android.app.Activity
import android.content.*
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R

class RecyclerViewAdapter(
    private val activity: Activity,
    private val uri: Uri,
    private val type: Int
): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_select, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        when(type){
            0 -> {
                when(position){
                    // Browserで開く
                    0 -> {
                        holder.icon.setImageResource(R.drawable.ic_baseline_open_in_browser_24_white)
                        holder.text.text = "ブラウザで開く"

                        // Browserで開くをタップ時の処理
                        holder.view.setOnClickListener {
                            Intent(Intent.ACTION_VIEW).apply {
                                // ヴァリデーションチェック
                                if (uri.scheme == "http" || uri.scheme == "https"){
                                    this.data = uri

                                    activity.startActivity(this)
                                }else{
                                    Log.e("ERROR", "不正な操作が行われた可能性があります")
                                    activity.finish()
                                }
                            }
                        }
                    }
                    // コピーする
                    1 -> {
                        holder.icon.setImageResource(R.drawable.ic_baseline_content_copy_24_white)
                        holder.text.text = "コピーする"

                        // コピーするをタップ時の処理
                        holder.view.setOnClickListener {
                            val clipBound = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipBound.setPrimaryClip(ClipData.newUri(activity.contentResolver, "", uri))
                            Toast.makeText(activity, "クリップボードにコピーしました", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // 他のアプリで共有する
                    2 -> {
                        holder.icon.setImageResource(R.drawable.ic_baseline_share_24_white)
                        holder.text.text = "他のアプリで共有する"

                        // タップ時の動作
                        holder.view.setOnClickListener {
                            Intent().apply {
                                // ヴァリデーションチェック
                                if (uri.scheme == "http" || uri.scheme == "https"){
                                    this.action = Intent.ACTION_SEND
                                    this.type = "text/plain"
                                    this.putExtra(Intent.EXTRA_TEXT, uri)

                                    // 共有開始
                                    try {
                                        activity.startActivity(Intent.createChooser(this, "共有"))
                                    }catch (e: ActivityNotFoundException){
                                        Log.w("Warning", "SHARE ACTIVITY NOT FOUND")
                                        Toast.makeText(activity, "共有できるアプリがありません", Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    Log.e("ERROR", "不正な操作が行われた可能性があります")
                                    activity.finish()
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                holder.icon.setImageResource(R.drawable.ic_baseline_map_24_white)
                holder.text.text = "地図を開く"

                // Browserで開くをタップ時の処理
                holder.view.setOnClickListener {
                    Intent(Intent.ACTION_VIEW).apply {
                        // ヴァリデーションチェック
                        if (uri.scheme == "geo"){
                            this.data = uri

                            activity.startActivity(this)
                        }else{
                            Log.e("ERROR", "不正な操作が行われた可能性があります")
                            activity.finish()
                        }
                    }
                }
            }
            else -> {
                Log.e("ERROR", "不正な操作が行われた可能性があります")
                activity.finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}