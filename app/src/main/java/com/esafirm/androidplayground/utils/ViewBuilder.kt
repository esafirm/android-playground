package com.esafirm.androidplayground.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.esafirm.androidplayground.common.BaseController

private fun linearLayout(context: Context, orientation: Int): LinearLayout {
    return LinearLayout(context).apply {
        this.orientation = orientation
    }
}

fun BaseController.row(block: ViewGroup.() -> Unit): LinearLayout {
    return linearLayout(requiredContext, LinearLayout.VERTICAL).also(block)
}

fun BaseController.column(block: ViewGroup.() -> Unit): LinearLayout {
    return linearLayout(requiredContext, LinearLayout.HORIZONTAL).also(block)
}

fun ViewGroup.button(text: String, onClick: () -> Unit) =
    addView(Button(context).apply {
        this.text = text
        setOnClickListener { onClick.invoke() }
        matchParent(horizontal = true)
    })

fun ViewGroup.matchParent(horizontal: Boolean = true, vertical: Boolean = true) {
    layoutParams = ViewGroup.LayoutParams(
        if (horizontal) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
        if (vertical) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun ViewGroup.logger() = addView(Logger.getLogView(context))
