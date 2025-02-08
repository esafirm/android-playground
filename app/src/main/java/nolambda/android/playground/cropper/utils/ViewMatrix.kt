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
import kotlin.math.max
import kotlin.math.min
import android.graphics.Matrix as AndroidMatrix

@Stable
interface ViewMatrix {
    fun zoom(center: Offset, scale: Float)
    fun rotate(center: Offset, rotation: Float)
    fun translate(offset: Offset)

    fun fit(inner: Rect, outer: Rect)

    fun setInitialMatrix(initialImg: Rect)

    suspend fun animateZoom(scale: Float, center: Offset)
    suspend fun animateImageToWrapCropBounds(
        outer: Rect,
        crop: Rect,
        imgRect: Rect,
        originalImg: Rect
    )

    val matrix: Matrix
    val invMatrix: Matrix
    val scale: Float
    val currentRotation: Float

    val currentImageRect: Rect
}

internal class ViewMatrixImpl : ViewMatrix {

    private lateinit var initialImageCorners: FloatArray
    private val currentImageCorners: FloatArray = FloatArray(8)

    private var mat by mutableStateOf(Matrix(), neverEqualPolicy())
    private val inv by derivedStateOf {
        Matrix().apply {
            setFrom(mat)
            invert()
        }
    }

    override val scale get() = mat.values[Matrix.ScaleX]
    override val currentRotation get() = mat.rotation()

    override val currentImageRect: Rect get() = currentImageCorners.trapToRect()

    override fun zoom(center: Offset, scale: Float) {
        if (scale == 0f) return

        val s = Matrix().apply {
            translate(center.x, center.y)
            scale(scale, scale)
            translate(-center.x, -center.y)
        }
        updateNative { it *= s }
    }

    inline fun updateNative(op: (Matrix) -> Unit) {
        mat = mat.copy().also(op)
        mat.mapPoints(currentImageCorners, initialImageCorners)
    }

    inline fun update(op: (AndroidMatrix) -> Unit) {
        val androidMat = mat.toAndroidMatrix().also { op(it) }
        androidMat.mapPoints(currentImageCorners, initialImageCorners)
        mat = androidMat.toMatrix()
    }

    override val matrix: Matrix
        get() = mat
    override val invMatrix: Matrix
        get() = inv

    override fun setInitialMatrix(initialImg: Rect) {
        initialImageCorners = initialImg.asCorners()
        initialImageCorners.copyInto(currentImageCorners)
    }

    override fun fit(inner: Rect, outer: Rect) {
        val dst = getDst(inner, outer) ?: return
        updateNative { it *= Matrix().apply { setRectToRect(inner, dst) } }
    }

    private fun getDst(inner: Rect, outer: Rect): Rect? {
        val scale = min(outer.width / inner.width, outer.height / inner.height)
        return Rect(Offset.Zero, inner.size * scale).centerIn(outer)
    }

    override fun translate(offset: Offset) {
        if (offset == Offset.Zero) return
        update { it.postTranslate(offset.x, offset.y) }
    }

    override fun rotate(center: Offset, rotation: Float) {
        updateNative { it.rotate(center, rotation) }
    }

    /**
     * Not really sure why but the translation can be wrong if we do it in one pass.
     */
    private suspend fun calculateImageIndentsAndAnimate(crop: Rect) {
        val imageIndents = calculateImageIndents(crop)
        val dx = -(imageIndents[0] + imageIndents[2])
        val dy = -(imageIndents[1] + imageIndents[3])

        animateTranslate(dx, dy)
    }

    private suspend fun animateTranslate(dx: Float, dy: Float) {
        var accOffsetX = 0f
        var accOffsetY = 0f

        animate(0f, 1f) { p, _ ->
            val offsetX = (dx * p) - accOffsetX
            val offsetY = (dy * p) - accOffsetY

            accOffsetX += offsetX
            accOffsetY += offsetY

            update { it.postTranslate(offsetX, offsetY) }
        }
    }

    override suspend fun animateZoom(scale: Float, center: Offset) {
        val initialScale = this.scale

        animate(0f, 1f) { p, _ ->
            val deltaScale = (scale * initialScale - initialScale) * p
            val actualTargetScale = initialScale + deltaScale
            val relativeScale = actualTargetScale / this.scale

            update { it.postScale(relativeScale, relativeScale, center.x, center.y) }
        }
    }

    /**
     * First, un-rotate image and crop rectangles (make image rectangle axis-aligned).
     * Second, calculate deltas between those rectangles sides.
     * Third, depending on delta (its sign) put them or zero inside an array.
     * Fourth, using Matrix, rotate back those points (indents).
     *
     * @return - the float array of image indents (4 floats) - in this order [left, top, right, bottom]
     */
    private fun calculateImageIndents(crop: Rect): FloatArray {
        val tempMatrix = Matrix()
        tempMatrix.rotateZ(currentRotation)

        val unrotatedImageCorners = currentImageCorners.copyOf()
        tempMatrix.mapPoints(unrotatedImageCorners)

        val unrotatedImageRect = unrotatedImageCorners.trapToRect()
        val unrotatedCropRect = tempMatrix.map(crop)

        val deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left
        val deltaTop = unrotatedImageRect.top - unrotatedCropRect.top
        val deltaRight = unrotatedImageRect.right - unrotatedCropRect.right
        val deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom

        val indents = FloatArray(4)
        indents[0] = deltaLeft.coerceAtLeast(0f)
        indents[1] = deltaTop.coerceAtLeast(0f)
        indents[2] = deltaRight.coerceAtMost(0f)
        indents[3] = deltaBottom.coerceAtMost(0f)

        tempMatrix.reset()
        tempMatrix.rotateZ(-currentRotation)
        tempMatrix.mapPoints(indents)

        return indents
    }

    override suspend fun animateImageToWrapCropBounds(
        outer: Rect,
        crop: Rect,
        imgRect: Rect,
        originalImg: Rect,
    ) {
        val isWrapped = isImageWrapCropBounds(currentImageCorners, crop)
        if (isWrapped) {
            log("=> is wrapped!")
            return
        }

        var (dx, dy) = crop.center - imgRect.center
        val currentScale = scale

        val tempMatrix = Matrix()
        tempMatrix.translate(dx, dy)

        val tempCurrentImageCorners = currentImageCorners.copyOf()
        tempMatrix.mapPoints(tempCurrentImageCorners)

        val willWrapAfterTranslate = isImageWrapCropBounds(tempCurrentImageCorners, crop)
        log("will wrap after translate: $willWrapAfterTranslate")

        if (willWrapAfterTranslate) {
            calculateImageIndentsAndAnimate(crop)
        } else {
            tempMatrix.reset()
            tempMatrix.rotateZ(-currentRotation)
            val tempCropRect = tempMatrix.map(crop)

            val currentImageSides = currentImageCorners.toRectSides()
            var deltaScale = max(
                tempCropRect.width / currentImageSides[0],
                tempCropRect.height / currentImageSides[1]
            )

            // TODO: simplify this
            deltaScale = deltaScale * currentScale - currentScale
            deltaScale = currentScale + deltaScale
            deltaScale = deltaScale / currentScale

            val translateOffset = Offset(dx, dy)
            animateTranslate(translateOffset.x, translateOffset.y)
            animateZoom(deltaScale, crop.center)
        }
    }

    /**
     * This methods checks whether a rectangle that is represented as 4 corner points (8 floats)
     * fills the crop bounds rectangle.
     *
     * @return - true if it wraps crop bounds, false - otherwise
     */
    private fun isImageWrapCropBounds(
        imageCorners: FloatArray,
        crop: Rect
    ): Boolean {
        val matrix = Matrix()
        matrix.rotateZ(currentRotation)

        // Un-rotate image
        val unrotatedImageCorners = imageCorners.copyOf()
        matrix.mapPoints(unrotatedImageCorners)

        val unrotatedImageRect = unrotatedImageCorners.trapToRect()

        // Un-rotate crop
        val unrotatedCropRect = matrix.map(crop)

        return unrotatedImageRect.contains(unrotatedCropRect)
    }

    private fun log(msg: String) {
        android.util.Log.d("ViewMatrixImpl", msg)
    }
}
