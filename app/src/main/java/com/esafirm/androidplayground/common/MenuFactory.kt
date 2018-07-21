package com.esafirm.androidplayground.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

object MenuFactory {

    fun create(context: Context, data: List<String>, onNavigatePage: OnNavigatePage?) =
            create(context, data) {
                onNavigatePage?.navigate(it)
            }

    fun create(context: Context, data: List<String>, onNavigate: (Int) -> Unit): View {
        val listView = ListView(context)
        listView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val menuListAdapter = MenuListAdapter(context)
        menuListAdapter.setData(data)
        listView.adapter = menuListAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            onNavigate(i)
        }

        return listView
    }
}
