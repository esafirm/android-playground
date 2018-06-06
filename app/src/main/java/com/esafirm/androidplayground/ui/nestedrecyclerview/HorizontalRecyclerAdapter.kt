package com.esafirm.androidplayground.ui.nestedrecyclerview

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esafirm.androidplayground.utils.dp

class HorizontalRecyclerAdapter(private val items: List<String>) : RecyclerView.Adapter<HorizontalRecyclerAdapter.SimpleHorizontalVH>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SimpleHorizontalVH {
        return SimpleHorizontalVH(TextView(parent?.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(20.dp.toInt(), 20.dp.toInt(), 20.dp.toInt(), 20.dp.toInt())
            }
            setTextColor(Color.DKGRAY)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
            setAllCaps(true)
        })
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SimpleHorizontalVH?, position: Int) {
        holder?.textView?.text = items[position]
    }

    class SimpleHorizontalVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textView by lazy { itemView as TextView }
    }
}
