package com.esafirm.androidplayground.ui.nestedscroll

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseListAdapter

class TestAdapter(context: Context) : BaseListAdapter<String>(context) {
    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_square, parent, false)
        return TestViewHolder(view)
    }

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = 50
}
