package nolambda.android.playground.cropper.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntRect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean

internal class ImageStreamSrc private constructor(
    private val dataSource: ImageStream,
    private val exifInfo: ExifInfo,

    override val size: IntSize
) : ImageSrc {

    // P.S: Not working properly with transform bitmap. Let's revisit later
    private val allowRegion = AtomicBoolean(false)

    override suspend fun open(params: DecodeParams): DecodeResult? {
        if (allowRegion.get()) {
            val region = openRegion(params)
            if (region != null) return region
            else allowRegion.set(false)
        }
        openFull(params.sampleSize)?.let { return it }
        return null
    }

    /**
     * Open a region of the image with the given parameters.
     */
    private suspend fun openRegion(params: DecodeParams): DecodeResult? {
        return dataSource.tryUse { stream ->
            regionDecoder(stream)!!.decodeRegion(params)?.asTransformedBitmap()
        }?.let { bmp ->
            DecodeResult(params, bmp.asImageBitmap())
        }
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

private fun regionDecoder(stream: InputStream): BitmapRegionDecoder? {
    @Suppress("DEPRECATION")
    return BitmapRegionDecoder.newInstance(stream, false)
}

private fun BitmapRegionDecoder.decodeRegion(params: DecodeParams): Bitmap? {
    val rect = params.subset.toAndroidRect()
    val options = bitmapFactoryOptions(params.sampleSize)
    return decodeRegion(rect, options)
}

private fun IntRect.toAndroidRect(): android.graphics.Rect {
    return android.graphics.Rect(left, top, right, bottom)
}

private fun bitmapFactoryOptions(sampleSize: Int) = BitmapFactory.Options().apply {
    inMutable = false
    inSampleSize = sampleSize
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
