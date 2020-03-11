package com.esafirm.androidplayground.utils

import kotlin.system.measureTimeMillis

inline fun logMeasure(tag: String, block: () -> Unit) {
    val time = measureTimeMillis(block)
    Logger.log("$tag took $time ms")
}