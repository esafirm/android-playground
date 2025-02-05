package nolambda.android.playground.cropper.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import kotlin.math.atan2
import android.graphics.Matrix as AndroidMatrix

internal val IdentityMat = Matrix()

internal operator fun Matrix.times(other: Matrix): Matrix = copy().apply {
    this *= other
}

internal fun Matrix.setScaleTranslate(sx: Float, sy: Float, tx: Float, ty: Float) {
    reset()
    values[Matrix.ScaleX] = sx
    values[Matrix.TranslateX] = tx
    values[Matrix.ScaleY] = sy
    values[Matrix.TranslateY] = ty
}

internal fun Matrix.setRectToRect(src: Rect, dst: Rect) {
    val sx: Float = dst.width / src.width
    val tx = dst.left - src.left * sx
    val sy: Float = dst.height / src.height
    val ty = dst.top - src.top * sy
    setScaleTranslate(sx, sy, tx, ty)
}

internal fun Matrix.copy(): Matrix = Matrix(values.clone())

internal fun Matrix.inverted() = copy().apply { invert() }

internal fun Matrix.rotation(): Float {
    val skewX = values[Matrix.SkewX]
    val scaleX = values[Matrix.ScaleX]
    return Math.toDegrees(atan2(skewX.toDouble(), scaleX.toDouble())).toFloat()
}

internal fun Matrix.rotate(center: Offset, rotation: Float) {
    val px = center.x
    val py = center.y

    translate(px, py)
    rotateZ(rotation)
    translate(-px, -py)
}

internal fun Matrix.scale(center: Offset, scale: Float) {
    val px = center.x
    val py = center.y

    translate(px, py)
    scale(scale, scale)
    translate(-px, -py)
}

fun Matrix.toAndroidMatrix(): AndroidMatrix {
    val composeValues = FloatArray(16)
    this.values.copyInto(composeValues)

    // Extract the relevant values from the 4x4 matrix
    val androidValues = floatArrayOf(
        composeValues[0], composeValues[4], composeValues[12],
        composeValues[1], composeValues[5], composeValues[13],
        composeValues[3], composeValues[7], composeValues[15]
    )

    return AndroidMatrix().apply {
        setValues(androidValues)
    }
}

internal fun Matrix.mapPoints(points: FloatArray) {
    toAndroidMatrix().mapPoints(points)
}

internal fun Matrix.mapPoints(dst: FloatArray, src: FloatArray) {
    toAndroidMatrix().mapPoints(dst, src)
}
