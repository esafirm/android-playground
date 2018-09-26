package com.esafirm.androidplayground.ui.nestedrecyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class NestedRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var newsItems = mutableListOf<AdapterItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return newsItems.firstOrNull { it.javaClass.hashCode() == viewType }.let {
            it!!.create(parent)
        }
    }

    override fun getItemCount(): Int = newsItems.size

    override fun getItemViewType(position: Int): Int {
        return newsItems[position].javaClass.hashCode()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        newsItems[position].bind(holder)
    }
}
