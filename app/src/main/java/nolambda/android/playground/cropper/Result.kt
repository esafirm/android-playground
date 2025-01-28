package nolambda.android.playground.cropper

import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import nolambda.android.playground.cropper.images.getDecodeParams
import nolambda.android.playground.cropper.utils.*
import nolambda.android.playground.cropper.utils.atOrigin
import nolambda.android.playground.cropper.utils.coerceAtMost
import nolambda.android.playground.cropper.utils.roundUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Creates an [ImageBitmap] using the parameters in [createCropState].
 * If [maxSize] is not null, the result will be scaled down to match it.
 * Returns null if the image could not be created.
 */
suspend fun CropState.createResult(
    maxSize: IntSize?
): ImageBitmap? = withContext(Dispatchers.Default) {
    runCatching { doImageCrop(maxSize) }
        .onFailure { it.printStackTrace() }
        .getOrNull()
}

private suspend fun CropState.doImageCrop(maxSize: IntSize?): ImageBitmap? {
    val finalRegion = generateCropRegion()

    val finalSize = finalRegion.size
        .coerceAtMost(maxSize?.toSize())
        .roundUp()

    val result = ImageBitmap(finalSize.width, finalSize.height)
    val canvas = Canvas(result)
    val viewMat = ViewMatrixImpl()

    viewMat.fit(finalRegion, finalSize.toSize().toRect())
    val imgMat = transform.asMatrix(src.size)
    val totalMat = imgMat * viewMat.matrix

    canvas.clipPath(shapePathOrError(rect = finalRegion.atOrigin()))
    canvas.concat(totalMat)

    val inParams = getDecodeParams(view = finalSize, img = src.size, totalMat) ?: return null
    val decoded = src.open(inParams) ?: return null
    val paint = Paint().apply { filterQuality = FilterQuality.High }

    canvas.drawImageRect(
        image = decoded.bmp,
        paint = paint,
        dstOffset = decoded.params.subset.topLeft,
        dstSize = decoded.params.subset.size,
    )
    return result
}
