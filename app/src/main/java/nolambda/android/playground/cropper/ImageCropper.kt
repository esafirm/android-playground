package nolambda.android.playground.cropper

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import nolambda.android.playground.cropper.images.ImageBitmapSrc
import nolambda.android.playground.cropper.images.ImageSrc

/** Union type denoting the possible results after a crop operation is done */
sealed interface CropResult {
    /** The final result as an ImageBitmap.
     * use [asAndroidBitmap] if you need an [android.graphics.Bitmap].
     */
    data class Success(val bitmap: ImageBitmap) : CropResult

    /** The user has cancelled the operation or another session was started. */
    object Cancelled : CropResult
}

enum class CropError : CropResult {
    /** The supplied image is invalid, not supported by the codec
     * or you don't have the required permissions to read it */
    LoadingError,

    /** The result could not be saved. Try reducing the maxSize supplied to [ImageCropper.crop] */
    SavingError
}

enum class CropperLoading {
    /** The image is being prepared. */
    PreparingImage,

    /** The user has accepted the cropped image and the result is being saved. */
    SavingResult,
}

internal val DefaultMaxCropSize = IntSize(3000, 3000)

/**
 * State holder for the image cropper.
 * Allows starting new crop sessions as well as getting the state of the pending crop.
 */
@Stable
interface ImageCropper {
    /** The pending crop state, if any */
    val cropState: CropState?

    val loadingStatus: CropperLoading?

    /**
     * Initiates a new crop session, cancelling the current one, if any.
     * Suspends until a result is available (cancellation, error, success) and returns it.
     * The resulting image will be scaled down to fit [maxResultSize] (if provided).
     * [createSrc] will be used to construct an [ImageSrc] instance.
     */
    suspend fun crop(
        maxResultSize: IntSize? = DefaultMaxCropSize,
        createSrc: suspend () -> ImageSrc?
    ): CropResult

    companion object {
        fun create(): ImageCropper = ImageCropperImpl()
    }
}

/**
 * Initiates a new crop session, cancelling the current one, if any.
 * Suspends until a result is available (cancellation, error, success) and returns it.
 * The resulting image will be scaled down to fit [maxResultSize] if provided.
 * [bmp] will be used as a source.
 */
suspend fun ImageCropper.crop(
    maxResultSize: IntSize? = DefaultMaxCropSize,
    bmp: ImageBitmap
): CropResult = crop(maxResultSize = maxResultSize) {
    ImageBitmapSrc(bmp)
}

/**
 * Creates an [ImageCropper] instance.
 */
internal class ImageCropperImpl : ImageCropper {
    private val cropStateFlow = snapshotFlow { cropState }

    override var cropState: CropState? by mutableStateOf(null)
    override var loadingStatus: CropperLoading? by mutableStateOf(null)

    override suspend fun crop(
        maxResultSize: IntSize?,
        createSrc: suspend () -> ImageSrc?
    ): CropResult {
        cropState = null
        val src = withLoading(CropperLoading.PreparingImage) { createSrc() }
            ?: return CropError.LoadingError
        val newCrop = createCropState(src) { cropState = null }
        cropState = newCrop
        cropStateFlow.takeWhile { it === newCrop }.collect()
        if (!newCrop.accepted) return CropResult.Cancelled

        return withLoading(CropperLoading.SavingResult) {
            val result = newCrop.createResult(maxResultSize)
            if (result == null) CropError.SavingError
            else CropResult.Success(result)
        }
    }

    inline fun <R> withLoading(status: CropperLoading, op: () -> R): R {
        return try {
            loadingStatus = status
            op()
        } finally {
            loadingStatus = null
        }
    }
}
