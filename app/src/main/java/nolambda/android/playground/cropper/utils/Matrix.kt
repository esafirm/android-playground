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

internal fun Matrix.copy(): Matrix = Matrix(values.clone())

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
    val androidValues = FloatArray(9)
    androidValues[AndroidMatrix.MTRANS_X] = composeValues[Matrix.TranslateX]
    androidValues[AndroidMatrix.MTRANS_Y] = composeValues[Matrix.TranslateY]
    androidValues[AndroidMatrix.MSCALE_X] = composeValues[Matrix.ScaleX]
    androidValues[AndroidMatrix.MSCALE_Y] = composeValues[Matrix.ScaleY]
    androidValues[AndroidMatrix.MSKEW_X] = composeValues[Matrix.SkewX]
    androidValues[AndroidMatrix.MSKEW_Y] = composeValues[Matrix.SkewY]
    androidValues[AndroidMatrix.MPERSP_0] = 0f
    androidValues[AndroidMatrix.MPERSP_1] = 0f
    androidValues[AndroidMatrix.MPERSP_2] = 1f

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

internal fun Matrix.mapPoints(points: FloatArray) {
    toAndroidMatrix().mapPoints(points)
}

internal fun Matrix.mapPoints(dst: FloatArray, src: FloatArray) {
    toAndroidMatrix().mapPoints(dst, src)
}
