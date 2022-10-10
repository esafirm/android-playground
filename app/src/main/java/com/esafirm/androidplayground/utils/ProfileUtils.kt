package com.esafirm.androidplayground.utils

import com.esafirm.androidplayground.libs.Logger
import kotlin.system.measureTimeMillis

inline fun logMeasure(tag: String, block: () -> Unit) {
    val time = measureTimeMillis(block)
    Logger.log("$tag took $time ms")
}
