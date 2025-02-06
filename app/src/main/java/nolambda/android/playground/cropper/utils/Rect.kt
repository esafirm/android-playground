package nolambda.android.playground.cropper.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntRect
import nolambda.android.playground.cropper.AspectRatio
import kotlin.math.*

internal fun Rect.lerp(target: Rect, p: Float): Rect {
    val tl0 = topLeft
    val br0 = bottomRight
    val dtl = target.topLeft - tl0
    val dbr = target.bottomRight - br0
    return Rect(tl0 + dtl * p, br0 + dbr * p)
}

internal fun Rect.centerIn(outer: Rect): Rect =
    translate(outer.center.x - center.x, outer.center.y - center.y)

internal fun Rect.fitIn(outer: Rect): Rect {
    val scaleF = min(outer.width / width, outer.height / height)
    return scale(scaleF, scaleF)
}

internal fun Rect.scale(sx: Float, sy: Float) = setSizeTL(width = width * sx, height = height * sy)

internal fun Rect.setSizeTL(width: Float, height: Float) =
    Rect(offset = topLeft, size = Size(width, height))

internal fun Rect.roundOut(): IntRect = IntRect(
    left = floor(left).toInt(), top = floor(top).toInt(),
    right = ceil(right).toInt(), bottom = ceil(bottom).toInt()
)

internal fun Rect.setAspect(aspect: AspectRatio): Rect = setAspect(aspect.x.toFloat() / aspect.y)

private fun Rect.setAspect(aspect: Float): Rect {
    val dim = max(width, height)
    return Rect(Offset.Zero, Size(dim * aspect, height = dim))
        .fitIn(this)
        .centerIn(this)
}

internal fun Size.keepAspect(old: Size): Size {
    val a = width * height
    return Size(
        width = sqrt((a * old.width) / old.height),
        height = sqrt((a * old.height) / old.width)
    )
}

internal fun Rect.keepAspect(old: Rect): Rect {
    return setSize(old, size.keepAspect(old.size))
}

internal fun Rect.setSize(old: Rect, size: Size): Rect {
    var (l, t, r, b) = this
    if ((old.left - l).absoluteValue < (old.right - r).absoluteValue) {
        r = l + size.width
    } else {
        l = r - size.width
    }
    if ((old.top - t).absoluteValue < (old.bottom - b).absoluteValue) {
        b = t + size.height
    } else {
        t = b - size.height
    }
    return Rect(l, t, r, b)
}

internal fun Rect.scaleToFit(bounds: Rect, old: Rect): Rect {
    val (l, t, r, b) = this
    val scale = minOf(
        (bounds.right - l) / (r - l),
        (bounds.bottom - t) / (b - t),
        (r - bounds.left) / (r - l),
        (bottom - bounds.top) / (b - t),
    )
    if (scale >= 1f) return this
    return setSize(old, size * scale)
}

internal fun IntRect.containsInclusive(other: IntRect): Boolean {
    return other.left >= left && other.top >= top &&
            other.right <= right && other.bottom <= bottom
}

internal fun Rect.align(alignment: Int): Rect = Rect(
    left.alignDown(alignment), top.alignDown(alignment),
    right.alignUp(alignment), bottom.alignUp(alignment)
)

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
