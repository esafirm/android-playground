package nolambda.android.playground.cropper

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.scale
import androidx.core.graphics.values
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nolambda.android.playground.cropper.images.DecodeParams
import kotlin.math.min
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

    var currentScale = viewMatrix.scale
    val currentRotation = viewMatrix.currentRotation
    val cropRect = cropRectOrError()

    val scaleResult = decoded.bmp.asAndroidBitmap().scaleBitmap(
        crop = cropRect,
        currentScale = currentScale,
        maxSize = maxSize
    )

    // Update scale
    currentScale = scaleResult.newScale

    log("scaled bitmap size: ${scaleResult.bitmap.width}x${scaleResult.bitmap.height}")
    val rotatedBitmap = scaleResult.bitmap.rotateIfNeeded(currentRotation)
    log("rotated bitmap size: ${rotatedBitmap.width}x${rotatedBitmap.height}")

    return rotatedBitmap.crop(
        cropRect = cropRect,
        imageRect = viewMatrix.currentImageRect,
        currentScale = currentScale
    ).asImageBitmap()
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

private data class ScaleResult(
    val bitmap: Bitmap,
    val newScale: Float
)

private fun Bitmap.scaleBitmap(
    crop: Rect,
    currentScale: Float,
    maxSize: IntSize?,
): ScaleResult {
    if (maxSize == null) return ScaleResult(this, currentScale)

    val cropWidth = crop.width / currentScale
    val cropHeight = crop.height / currentScale

    if (cropWidth <= maxSize.width && cropHeight <= maxSize.height) {
        return ScaleResult(this, currentScale)
    }

    val scaleX = maxSize.width / cropWidth
    val scaleY = maxSize.height / cropHeight
    val resizeScale = min(scaleX, scaleY)

    val newWidth = (width / resizeScale).roundToInt()
    val newHeight = (height / resizeScale).roundToInt()

    val scaledBitmap = scale(newWidth, newHeight)
    if (this != scaledBitmap) {
        this.recycle()
    }
    return ScaleResult(
        newScale = currentScale / resizeScale,
        bitmap = scaledBitmap
    )
}

private fun Bitmap.rotateIfNeeded(rotation: Float): Bitmap {
    if (rotation == 0f) return this

    val tempMatrix = Matrix()
    tempMatrix.setRotate(-rotation, width / 2f, height / 2f)

    // Still not sure why, but a pure rotation matrix affect the scale of the matrix
    // That's why in here we restore the scale to 1
    val currentMatrixScale = tempMatrix.values()[Matrix.MSCALE_X]
    tempMatrix.postScale(1f / currentMatrixScale, 1f / currentMatrixScale)

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

private fun log(message: String) {
    android.util.Log.d("ViewMatrix", message)
}
