package com.esafirm.androidplayground.ui.cliptopadding

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.esafirm.androidplayground.utils.dp

class ClipToPaddingAdapter : Adapter<ClipToPaddingAdapter.ClipToPaddingViewHolder>() {

    override fun getItemCount(): Int = 100

    override fun onBindViewHolder(holder: ClipToPaddingViewHolder, position: Int) {
        holder.itemView.let { it as TextView }
                .text = "item $position"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipToPaddingViewHolder =
            ClipToPaddingViewHolder(TextView(parent.context).apply {
                textSize = 22f
                setTextColor(Color.BLACK)
                val padding = 8.dp.toInt()
                setPadding(padding, padding, padding, padding)
            })

    class ClipToPaddingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
