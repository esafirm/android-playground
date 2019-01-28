package com.esafirm.androidplayground.utils

import android.util.Log
import kotlin.system.measureTimeMillis

fun <T> logMeasure(name: String, block: () -> T): T {
    var result: T? = null
    val time = measureTimeMillis { result = block() }
    Log.d("Measure", "$name took $time ms")
    return result!!
}
