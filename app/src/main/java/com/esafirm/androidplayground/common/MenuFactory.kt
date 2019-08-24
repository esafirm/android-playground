package com.esafirm.androidplayground.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.main.RouterAct

object MenuFactory {

    private val VIEW_ID = 912346

    class NavigateItem(val name: String, val onNavigate: (Context) -> Unit)

    fun create(context: Context, data: List<NavigateItem>): View {
        val items = data.map { it.name }
        val onNavigatePage = { index: Int ->
            data[index].onNavigate.invoke(context)
        }
        return create(context, items, onNavigatePage)
    }

    fun create(
        context: Context,
        data: List<String>,
        onNavigate: (Int) -> Unit
    ): View {
        val listView = ListView(context)
        listView.id = VIEW_ID
        listView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val menuListAdapter = MenuListAdapter(context)
        menuListAdapter.setData(data)
        listView.adapter = menuListAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            onNavigate(i)
        }

        return listView
    }

    fun render(
        group: ViewGroup,
        data: List<NavigateItem>
    ): View {
        val attachedView: ListView? = group.findViewById(VIEW_ID)
        return if (attachedView == null) {
            create(group.context, data).also {
                group.addView(it)
            }
        } else {
            val items = data.map { it.name }
            val newAdapter = MenuListAdapter(group.context).apply {
                setData(items)
            }
            attachedView.adapter = newAdapter
            attachedView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                data[i].onNavigate.invoke(group.context)
            }
            attachedView
        }
    }
}

infix fun String.navigateToController(onNavigatePage: () -> Controller) =
    MenuFactory.NavigateItem(this) {
        RouterAct.start(it, ControllerMaker(onNavigatePage))
    }


infix fun String.navigateTo(onNavigatePage: (Context) -> Unit) =
    MenuFactory.NavigateItem(this, onNavigatePage)
