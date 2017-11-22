package com.esafirm.androidplayground.utils

import android.content.res.Resources
import android.util.TypedValue

val Int.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

