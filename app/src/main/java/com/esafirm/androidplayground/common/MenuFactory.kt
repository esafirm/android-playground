package com.esafirm.androidplayground.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

object MenuFactory {

    class NavigateItem(val name: String, val onNavigate: () -> Unit)

    fun create(context: Context, data: List<NavigateItem>): View {
        val items = data.map { it.name }
        val onNavigatePage = { index: Int ->
            data[index].onNavigate.invoke()
        }
        return create(context, items, onNavigatePage)
    }

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

infix fun String.navigateTo(onNavigatePage: () -> Unit) =
    MenuFactory.NavigateItem(this, onNavigatePage)
