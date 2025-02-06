package nolambda.android.playground.cropper.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntRect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class ImageStreamSrc private constructor(
    private val dataSource: ImageStream,
    private val exifInfo: ExifInfo,

    override val size: IntSize
) : ImageSrc {

    override suspend fun open(params: DecodeParams): DecodeResult? {
        openFull(params.sampleSize)?.let { return it }
        return null
    }

    /**
     * Open the full image with the given sample size.
     *
     * BitmapFactory.decode supports more formats than BitmapRegionDecoder.
     */
    private suspend fun openFull(sampleSize: Int): DecodeResult? {
        return dataSource.tryUse { stream ->
            val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            BitmapFactory.decodeStream(stream, null, options)?.asTransformedBitmap()
        }?.let { bmp ->
            DecodeResult(DecodeParams(sampleSize, size.toIntRect()), bmp.asImageBitmap())
        }
    }

    private fun Bitmap.asTransformedBitmap(): Bitmap {
        return BitmapExifUtils.transformBitmap(this, exifInfo)
    }

    companion object {
        suspend operator fun invoke(dataSource: ImageStream): ImageStreamSrc? {
            val exif = BitmapExifUtils.getExifInfo(dataSource)
            val size = dataSource.tryUse { it.getImageSize(exif) }
                ?.takeIf { it.width > 0 && it.height > 0 }
                ?: return null
            return ImageStreamSrc(dataSource, exif, size)
        }
    }
}

private suspend fun <R> ImageStream.tryUse(op: (InputStream) -> R): R? {
    return withContext(Dispatchers.IO) {
        openStream()?.use { stream -> runCatching { op(stream) } }
    }?.onFailure {
        it.printStackTrace()
    }?.getOrNull()
}

private fun InputStream.getImageSize(exif: ExifInfo): IntSize {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeStream(this, null, options)

    return if (exif.isSwapWidthAndHeight()) {
        IntSize(options.outHeight, options.outWidth)
    } else {
        IntSize(options.outWidth, options.outHeight)
    }
}
