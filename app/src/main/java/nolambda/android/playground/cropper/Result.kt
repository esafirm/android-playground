package nolambda.android.playground.cropper

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nolambda.android.playground.cropper.images.DecodeParams
import kotlin.math.roundToInt

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
    val inParams = DecodeParams(requestedSize = src.size)
    val decoded = src.open(inParams) ?: return null

    val currentScale = viewMatrix.scale
    val currentRotation = viewMatrix.currentRotation

    val cropRect = cropRectOrError()
    val imageRect = viewMatrix.currentImageRect

    val rotatedBitmap = decoded.bmp.asAndroidBitmap().rotateIfNeeded(currentRotation)
    val croppedBitmap = rotatedBitmap.crop(
        cropRect = cropRect,
        imageRect = imageRect,
        currentScale = currentScale
    )

    return if (maxSize != null) {
        croppedBitmap.scaleBitmap(maxSize).asImageBitmap()
    } else {
        croppedBitmap.asImageBitmap()
    }
}

private fun Bitmap.crop(cropRect: Rect, imageRect: Rect, currentScale: Float): Bitmap {
    if (cropRect == imageRect && currentScale == 1f) return this

    val cropOffsetX = ((cropRect.left - imageRect.left) / currentScale).roundToInt()
    val cropOffsetY = ((cropRect.top - imageRect.top) / currentScale).roundToInt()
    val croppedImageWidth = (cropRect.width / currentScale).roundToInt()
    val croppedImageHeight = (cropRect.height / currentScale).roundToInt()

    val croppedBitmap = Bitmap.createBitmap(
        this,
        cropOffsetX,
        cropOffsetY,
        croppedImageWidth,
        croppedImageHeight
    )

    if (this != croppedBitmap) {
        this.recycle()
    }
    return croppedBitmap
}

private fun Bitmap.scaleBitmap(maxSize: IntSize): Bitmap {
    val scale = width.coerceAtLeast(height).toFloat() / maxSize.width.coerceAtLeast(maxSize.height)
    val newWidth = (width / scale).roundToInt()
    val newHeight = (height / scale).roundToInt()

    val scaledBitmap = scale(newWidth, newHeight)
    if (this != scaledBitmap) {
        this.recycle()
    }
    return scaledBitmap
}

private fun Bitmap.rotateIfNeeded(rotation: Float): Bitmap {
    if (rotation == 0f) return this

    val tempMatrix = Matrix()
    tempMatrix.setRotate(rotation, width / 2f, height / 2f)

    val rotatedBitmap = Bitmap.createBitmap(this, 0, 0, width, height, tempMatrix, true)
    if (this != rotatedBitmap) {
        this.recycle()
    }
    return rotatedBitmap
}

private fun CropState.cropRectOrError(): Rect {
    val spec = cropSpec
    if (spec is CropSpec.Ready) {
        return spec.rect
    } else {
        error("CropSpec is not available. $this")
    }
}
