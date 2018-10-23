package com.esafirm.androidplayground.ui.nestedrecyclerview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.androidplayground.utils.dp

interface AdapterItem {
    fun bind(viewHolder: RecyclerView.ViewHolder)
    fun create(viewParent: ViewGroup?): RecyclerView.ViewHolder
}

class SingleNewsItem(private val news: String) : AdapterItem {

    override fun bind(viewHolder: RecyclerView.ViewHolder) {
        viewHolder as SingleNewsViewHolder
        viewHolder.textView.text = news
    }

    override fun create(viewParent: ViewGroup?): RecyclerView.ViewHolder {
        val context = viewParent?.context
        return SingleNewsViewHolder(TextView(context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(20.dp.toInt(), 16.dp.toInt(), 20.dp.toInt(), 16.dp.toInt())
            }
            setTextColor(Color.BLACK)
        })
    }

    class SingleNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView by lazy { itemView as TextView }
    }
}


class HeadlineNewsItem(private val newsList: List<String>) : AdapterItem {

    override fun bind(viewHolder: RecyclerView.ViewHolder) {
        viewHolder as HeadlineNEwsViewHolder
        viewHolder.recyclerView.adapter = HorizontalRecyclerAdapter(newsList)
    }

    override fun create(viewParent: ViewGroup?): RecyclerView.ViewHolder {
        val context = viewParent?.context
        return HeadlineNEwsViewHolder(RecyclerView(context!!).apply {
            layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
            LinearSnapHelper().attachToRecyclerView(this)
        })
    }

    class HeadlineNEwsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView by lazy { itemView as RecyclerView }
    }
}
