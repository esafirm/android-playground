package com.esafirm.androidplayground.utils

import android.view.ViewGroup

fun ViewGroup.matchParent(horizontal: Boolean = true, vertical: Boolean = true) {
    layoutParams = ViewGroup.LayoutParams(
            if (horizontal) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
            if (vertical) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
