package com.esafirm.androidplayground.ui.cliptopadding

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.utils.dp

class ClipToPaddingController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            RecyclerView(container.context).apply {
                clipToPadding = false
                setPadding(0, 0, 0, 72.dp.toInt())
                layoutManager = LinearLayoutManager(container.context)
                adapter = ClipToPaddingAdapter()
            }
}
