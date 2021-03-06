package com.websarva.wings.android.qrcodereader.ui.recyclerview.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.viewmodel.history.HistoryViewModel

class RecyclerViewAdapter(
    val items: MutableList<MutableMap<String, Any>>,
    private val viewModel: HistoryViewModel,
    private val fragment: HistoryFragment
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
                1 -> R.drawable.ic_baseline_map_24
                else -> Log.e("ERROR", "不正な操作が行われた可能性があります。")
            }
        )

        // viewタップ時の処理
        holder.view.setOnClickListener {
            Log.d("recyclerview", "Click")
            viewModel.setBundle(items[position][History.Title.name] as String)
        }

        // viewを長押しタップした時の処理
        holder.view.setOnLongClickListener {
            longClick(holder)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getRecyclerViewSimpleCallBack() = object: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE,
        ItemTouchHelper.LEFT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            setPosition(viewHolder.absoluteAdapterPosition)
            fragment.contextItemClick()
        }
    }
    private fun longClick(holder: RecyclerViewHolder): Boolean{
        Log.d("historyRecyclerView", "Called")
        setPosition(holder.absoluteAdapterPosition)

        return false
    }

    private var position = 0
    fun getPosition(): Int{
        return position
    }
    private fun setPosition(position: Int){
        this.position = position
    }
}