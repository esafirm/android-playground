package nolambda.android.playground.cropper

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path

/**
 * A Shape used to clip the resulting image.
 * Implementations should provide a meaningful equals method,
 * such as (A == B) => A.asPath(R) == B.asPath(R)
 */
@Stable
fun interface CropShape {
    fun asPath(rect: Rect): Path
}

@Stable
val RectCropShape = CropShape { rect -> Path().apply { addRect(rect) } }

@Stable
val CircleCropShape = CropShape { rect -> Path().apply { addOval(rect) } }

@Stable
val TriangleCropShape = CropShape { rect ->
    Path().apply {
        moveTo(rect.left, rect.bottom)
        lineTo(rect.center.x, rect.top)
        lineTo(rect.right, rect.bottom)
        close()
    }
}

val StarCropShape = CropShape { rect ->
    polygonPath(
        tx = rect.left, ty = rect.top,
        sx = rect.width / 32, sy = rect.height / 32,
        points = floatArrayOf(
            31.95f, 12.418856f,
            20.63289f, 11.223692f,
            16f, 0.83228856f,
            11.367113f, 11.223692f,
            0.05000003f, 12.418856f,
            8.503064f, 20.03748f,
            6.1431603f, 31.167711f,
            16f, 25.48308f,
            25.85684f, 31.167711f,
            23.496937f, 20.03748f
        )
    )
}

data class RoundRectCropShape(private val cornersPercent: Int) : CropShape {
    override fun asPath(rect: Rect): Path {
        val radius = CornerRadius(rect.minDimension * cornersPercent / 100f)
        return Path().apply { addRoundRect(RoundRect(rect = rect, radius)) }
    }
}

val DefaultCropShapes = listOf(
    RectCropShape,
    CircleCropShape,
    RoundRectCropShape(15),
    StarCropShape,
    TriangleCropShape
)


private fun polygonPath(
    tx: Float = 0f, ty: Float = 0f,
    sx: Float = 1f, sy: Float = 1f,
    points: FloatArray
): Path = Path().apply {
    if (points.size < 2) return@apply
    moveTo(points[0] * sx + tx, points[1] * sy + ty)
    for (i in 1 until points.size / 2) {
        lineTo(points[(i * 2) + 0] * sx + tx, points[(i * 2) + 1] * sy + ty)
    }
    close()
}
