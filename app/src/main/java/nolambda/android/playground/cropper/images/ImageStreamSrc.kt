package nolambda.android.playground.cropper.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class ImageStreamSrc private constructor(
    private val dataSource: ImageStream,
    private val exifInfo: ExifInfo,

    override val size: IntSize
) : ImageSrc {

    override suspend fun open(params: DecodeParams): DecodeResult? {
        val requestedSize = params.requestedSize

        return dataSource.tryUse { stream ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            val sampleSize = calculateInSampleSize(options, requestedSize)

            // Decode bitmap with inSampleSize set
            options.inSampleSize = sampleSize
            options.inJustDecodeBounds = false

            val bmp = BitmapFactory.decodeStream(stream, null, options)?.asTransformedBitmap()
                ?: return@tryUse null

            DecodeResult(
                params = params,
                sampleSize = sampleSize,
                bmp = bmp.asImageBitmap()
            )
        }
    }

    private fun Bitmap.asTransformedBitmap(): Bitmap {
        return BitmapExifUtils.transformBitmap(this, exifInfo)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        requestedSize: IntSize,
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        val reqWidth = requestedSize.width
        val reqHeight = requestedSize.height

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
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
