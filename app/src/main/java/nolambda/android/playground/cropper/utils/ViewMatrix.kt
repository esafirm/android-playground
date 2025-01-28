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
internal interface ViewMatrix {
    fun zoomStart(center: Offset)
    fun zoom(center: Offset, scale: Float)
    fun fit(inner: Rect, outer: Rect)
    fun translate(offset: Offset, crop: Rect, imgRect: Rect)

    suspend fun animateFit(inner: Rect, outer: Rect)
    suspend fun animateContains(inner: Rect, outer: Rect, crop: Rect, imgRect: Rect)

    val matrix: Matrix
    val invMatrix: Matrix
    val scale: Float
}

internal class ViewMatrixImpl : ViewMatrix {
    var c0 = Offset.Zero
    var mat by mutableStateOf(Matrix(), neverEqualPolicy())
    val inv by derivedStateOf {
        Matrix().apply {
            setFrom(mat)
            invert()
        }
    }
    override val scale by derivedStateOf {
        mat.values[Matrix.ScaleX]
    }

    override fun zoomStart(center: Offset) {
        c0 = center
    }

    override fun zoom(center: Offset, scale: Float) {
        val s = Matrix().apply {
            translate(center.x - c0.x, center.y - c0.y)
            translate(center.x, center.y)
            scale(scale, scale)
            translate(-center.x, -center.y)
        }
        update { it *= s }
        c0 = center
    }

    inline fun update(op: (Matrix) -> Unit) {
        mat = mat.copy().also(op)
    }

    override val matrix: Matrix
        get() = mat
    override val invMatrix: Matrix
        get() = inv

    override fun fit(inner: Rect, outer: Rect) {
        val dst = getDst(inner, outer) ?: return
        update { it *= Matrix().apply { setRectToRect(inner, dst) } }
    }

    private fun getDst(inner: Rect, outer: Rect): Rect? {
        val scale = min(outer.width / inner.width, outer.height / inner.height)
        return Rect(Offset.Zero, inner.size * scale).centerIn(outer)
    }

    override fun translate(offset: Offset, crop: Rect, imgRect: Rect) {
        var x = offset.x * scale
        var y = offset.y * scale
        update { it.translate(x, y) }
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

    override suspend fun animateContains(
        inner: Rect,
        outer: Rect,
        crop: Rect,
        imgRect: Rect,
    ) {
        val needToScale = crop.width > imgRect.width || crop.height > imgRect.height
        if (needToScale) {
            animateFit(inner, outer)
            return
        }

        val x = when {
            crop.left < imgRect.left -> crop.left - imgRect.left
            crop.right > imgRect.right -> crop.right - imgRect.right
            else -> 0f
        }
        val y = when {
            crop.top < imgRect.top -> crop.top - imgRect.top
            crop.bottom > imgRect.bottom -> crop.bottom - imgRect.bottom
            else -> 0f
        }
        if (x == 0f && y == 0f) return

        var accOffsetX = 0f
        var accOffsetY = 0f

        animate(0f, 1f) { p, _ ->
            val offsetX = (x / scale * p) - accOffsetX
            val offsetY = (y / scale * p) - accOffsetY

            accOffsetX += offsetX
            accOffsetY += offsetY

            update { it.translate(offsetX, offsetY) }
        }
    }
}
