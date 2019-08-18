package com.esafirm.androidplayground.common

import android.content.Context
import android.widget.ArrayAdapter

class MenuListAdapter(
    context: Context
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {

    fun setData(data: List<String>) {
        clear()
        addAll(data)
        notifyDataSetChanged()
    }
}
