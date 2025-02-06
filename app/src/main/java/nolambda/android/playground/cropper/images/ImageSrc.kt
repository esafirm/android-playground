package nolambda.android.playground.cropper.images

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize

data class DecodeParams(val requestedSize: IntSize)
data class DecodeResult(
    val params: DecodeParams,
    val sampleSize: Int,
    val bmp: ImageBitmap
)

@Stable
interface ImageSrc {
    val size: IntSize
    suspend fun open(params: DecodeParams): DecodeResult?
}

internal data class ImageBitmapSrc(private val data: ImageBitmap) : ImageSrc {
    override val size: IntSize = IntSize(data.width, data.height)
    private val resultParams = DecodeParams(size)
    override suspend fun open(params: DecodeParams) = DecodeResult(resultParams, 1, data)
}
