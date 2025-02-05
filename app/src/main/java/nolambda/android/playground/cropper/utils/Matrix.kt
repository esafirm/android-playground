package nolambda.android.playground.cropper.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.core.graphics.values
import kotlin.math.atan2
import android.graphics.Matrix as AndroidMatrix

internal val IdentityMat = Matrix()

internal operator fun Matrix.times(other: Matrix): Matrix = copy().apply {
    this *= other
}

internal operator fun AndroidMatrix.timesAssign(other: AndroidMatrix) {
    preConcat(other)
}

internal fun Matrix.setScaleTranslate(sx: Float, sy: Float, tx: Float, ty: Float) {
    reset()
    values[Matrix.ScaleX] = sx
    values[Matrix.TranslateX] = tx
    values[Matrix.ScaleY] = sy
    values[Matrix.TranslateY] = ty
}

internal fun AndroidMatrix.setScaleTranslate(sx: Float, sy: Float, tx: Float, ty: Float) {
    reset()
    val values = values()
    values[AndroidMatrix.MSCALE_X] = sx
    values[AndroidMatrix.MTRANS_X] = tx
    values[AndroidMatrix.MSCALE_Y] = sy
    values[AndroidMatrix.MTRANS_Y] = ty
    setValues(values)
}

/**
 * Sets this matrix to map the source rectangle to the destination rectangle.
 *
 * @param src The source rectangle.
 * @param dst The destination rectangle.
 */
internal fun Matrix.setRectToRect(src: Rect, dst: Rect) {
    val sx: Float = dst.width / src.width
    val tx = dst.left - src.left * sx
    val sy: Float = dst.height / src.height
    val ty = dst.top - src.top * sy
    setScaleTranslate(sx, sy, tx, ty)
}

/**
 * Sets this AndroidMatrix to map the source rectangle to the destination rectangle.
 *
 * @param src The source rectangle.
 * @param dst The destination rectangle.
 */
internal fun AndroidMatrix.setRectToRect(src: Rect, dst: Rect) {
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

internal fun AndroidMatrix.toMatrix(): Matrix {
    if (this.isIdentity) return IdentityMat

    val androidValues = values()
    return Matrix().apply {
        values[Matrix.TranslateX] = androidValues[AndroidMatrix.MTRANS_X]
        values[Matrix.TranslateY] = androidValues[AndroidMatrix.MTRANS_Y]
        values[Matrix.ScaleX] = androidValues[AndroidMatrix.MSCALE_X]
        values[Matrix.ScaleY] = androidValues[AndroidMatrix.MSCALE_Y]
        values[Matrix.SkewX] = androidValues[AndroidMatrix.MSKEW_X]
        values[Matrix.SkewY] = androidValues[AndroidMatrix.MSKEW_Y]
    }
}

internal fun AndroidMatrix.rotation(): Float {
    val values = this.values()
    val skewX = values[AndroidMatrix.MSKEW_X]
    val scaleX = values[AndroidMatrix.MSCALE_X]
    return Math.toDegrees(atan2(skewX.toDouble(), scaleX.toDouble())).toFloat()
}

internal fun Matrix.mapPoints(points: FloatArray) {
    toAndroidMatrix().mapPoints(points)
}

internal fun Matrix.mapPoints(dst: FloatArray, src: FloatArray) {
    toAndroidMatrix().mapPoints(dst, src)
}

internal fun AndroidMatrix.copy(): AndroidMatrix {
    val values = this.values()
    return AndroidMatrix().apply {
        setValues(values)
    }
}

internal fun AndroidMatrix.map(offset: Offset): Offset {
    val points = floatArrayOf(offset.x, offset.y)
    mapPoints(points)
    return Offset(points[0], points[1])
}
