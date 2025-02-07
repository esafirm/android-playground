package nolambda.android.playground.cropper.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import nolambda.android.playground.cropper.AspectRatio
import kotlin.math.*

internal fun Rect.centerIn(outer: Rect): Rect =
    translate(outer.center.x - center.x, outer.center.y - center.y)

internal fun Rect.fitIn(outer: Rect): Rect {
    val scaleF = min(outer.width / width, outer.height / height)
    return scale(scaleF, scaleF)
}

internal fun Rect.scale(sx: Float, sy: Float) = setSizeTL(width = width * sx, height = height * sy)

private fun Rect.setSizeTL(width: Float, height: Float) =
    Rect(offset = topLeft, size = Size(width, height))

internal fun Rect.setAspect(aspect: AspectRatio): Rect = setAspect(aspect.x.toFloat() / aspect.y)

private fun Rect.setAspect(aspect: Float): Rect {
    val dim = max(width, height)
    return Rect(Offset.Zero, Size(dim * aspect, height = dim))
        .fitIn(this)
        .centerIn(this)
}

/**
 * Returns true if the specified rectangle r is inside or equal to this
 * rectangle. An empty rectangle never contains another rectangle.
 *
 * @param r The rectangle being tested for containment.
 * @return true if the specified rectangle r is inside or equal to this
 *              rectangle
 */
internal fun Rect.contains(r: Rect): Boolean {
    // check for empty first
    return this.left < this.right && this.top < this.bottom
            // now check for containment
            && left <= r.left && top <= r.top
            && right >= r.right && bottom >= r.bottom
}


internal fun Rect.asCorners(): FloatArray {
    return floatArrayOf(
        left, top,
        right, top,
        right, bottom,
        left, bottom
    )
}


internal fun FloatArray.trapToRect(): Rect {
    var left = Float.POSITIVE_INFINITY
    var top = Float.POSITIVE_INFINITY
    var right = Float.NEGATIVE_INFINITY
    var bottom = Float.NEGATIVE_INFINITY

    for (i in 1 until size step 2) {
        val x = (this[i - 1] * 10).roundToInt() / 10f
        val y = (this[i] * 10).roundToInt() / 10f
        left = minOf(x, left)
        top = minOf(y, top)
        right = maxOf(x, right)
        bottom = maxOf(y, bottom)
    }

    return Rect(left, top, right, bottom)
}

/**
 * Gets a float array of two lengths representing a rectangles width and height
 * The order of the corners in the input float array is:
 * 0------->1
 * ^        |
 * |        |
 * |        v
 * 3<-------2
 *
 * @param [FloatArray] the float array of corners (8 floats)
 * @return the float array of width and height (2 floats)
 */
internal fun FloatArray.toRectSides(): FloatArray {
    return floatArrayOf(
        sqrt((this[0] - this[2]).pow(2) + (this[1] - this[3]).pow(2)),
        sqrt((this[2] - this[4]).pow(2) + (this[3] - this[5]).pow(2))
    )
}

private fun Float.pow(n: Int): Float = this.toDouble().pow(n).toFloat()
