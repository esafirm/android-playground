package nolambda.android.playground.cropper.utils

import androidx.compose.animation.core.animate
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import kotlin.math.min

@Stable
interface ViewMatrix {
    fun zoomStart(center: Offset)
    fun zoom(center: Offset, scale: Float)

    fun rotate(center: Offset, rotation: Float)

    fun fit(inner: Rect, outer: Rect)
    fun translate(offset: Offset, crop: Rect, imgRect: Rect)

    suspend fun animateFit(inner: Rect, outer: Rect)
    suspend fun animateImageToWrapCropBounds(
        outer: Rect,
        crop: Rect,
        imgRect: Rect,
        originalImg: Rect
    )

    val matrix: Matrix
    val invMatrix: Matrix
    val scale: Float
}

internal class ViewMatrixImpl : ViewMatrix {

    private var mat by mutableStateOf(Matrix(), neverEqualPolicy())
    private val inv by derivedStateOf {
        Matrix().apply {
            setFrom(mat)
            invert()
        }
    }

    override val scale by derivedStateOf {
        mat.values[Matrix.ScaleX]
    }

    override fun zoomStart(center: Offset) {
    }

    override fun zoom(center: Offset, scale: Float) {
        val s = Matrix().apply {
            translate(center.x, center.y)
            scale(scale, scale)
            translate(-center.x, -center.y)
        }
        update { it *= s }
    }

    inline fun update(op: (Matrix) -> Unit) {
        mat = mat.copy().also(op)
    }

    override val matrix: Matrix
        get() = mat
    override val invMatrix: Matrix
        get() = inv

    private val currentRotation get() = mat.rotation()

    override fun fit(inner: Rect, outer: Rect) {
        val dst = getDst(inner, outer) ?: return
        update { it *= Matrix().apply { setRectToRect(inner, dst) } }
    }

    private fun getDst(inner: Rect, outer: Rect): Rect? {
        val scale = min(outer.width / inner.width, outer.height / inner.height)
        return Rect(Offset.Zero, inner.size * scale).centerIn(outer)
    }

    override fun translate(offset: Offset, crop: Rect, imgRect: Rect) {
        update { it.translate(offset.x, offset.y) }
    }

    override fun rotate(center: Offset, rotation: Float) {
        update { it.rotate(center, rotation) }
    }

    override suspend fun animateFit(inner: Rect, outer: Rect) {
        val dst = getDst(inner, outer) ?: return
        val mat = Matrix()
        val initial = this.mat.copy()
        animate(0f, 1f) { p, _ ->
            update {
                it.setFrom(initial)
                it *= mat.apply { setRectToRect(inner, inner.lerp(dst, p)) }
            }
        }
    }

    suspend fun animateTranslate(offset: Offset) {
        val initial = mat.copy()
        animate(0f, 1f) { p, _ ->
            update {
                it.setFrom(initial)
                it.translate(offset.x * p, offset.y * p)
            }
        }
    }

    override suspend fun animateImageToWrapCropBounds(
        outer: Rect,
        crop: Rect,
        imgRect: Rect,
        originalImg: Rect,
    ) {
        val isWrapped = isImageWrapCropBounds(originalImg, crop)
        log("Is wrapped: $isWrapped")
    }

    /**
     * This methods checks whether a rectangle that is represented as 4 corner points (8 floats)
     * fills the crop bounds rectangle.
     *
     * @return - true if it wraps crop bounds, false - otherwise
     */
    private fun isImageWrapCropBounds(
        img: Rect,
        crop: Rect,
    ): Boolean {
        val tempMatrix = Matrix()

        tempMatrix.setFrom(mat)
        tempMatrix.rotate(img.center, currentRotation)
        val unrotatedImage = tempMatrix.map(img)

        tempMatrix.reset()
        tempMatrix.rotate(crop.center, currentRotation)
        val unrotatedCrop = tempMatrix.map(crop)

        return unrotatedImage.contains(unrotatedCrop)
    }

    private fun Rect.info(): String = "$this (${width}x${height}) - c: $center"

    private fun log(msg: String) {
        android.util.Log.d("ViewMatrixImpl", msg)
    }
}
