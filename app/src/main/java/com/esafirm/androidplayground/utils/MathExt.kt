package com.esafirm.androidplayground.utils

fun clamp(top: Number, bottom: Number, variable: Number): Number =
        Math.min(Math.max(variable.toInt(), top.toInt()), bottom.toInt())
