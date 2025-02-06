package nolambda.android.playground.cropper.utils

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

fun Float.alignDown(alignment: Int): Float = floor(this / alignment) * alignment
fun Float.alignUp(alignment: Int): Float = ceil(this / alignment) * alignment
fun Float.align(alignment: Int): Float = round(this / alignment) * alignment
