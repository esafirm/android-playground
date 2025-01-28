package nolambda.android.playground.cropper.images

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

internal data class ExifInfo(
    val orientation: Int,
    val degrees: Int,
    val translation: Int
)

internal fun ExifInfo.isSwapWidthAndHeight(): Boolean {
    val swappedOrientation = listOf(
        ExifInterface.ORIENTATION_ROTATE_90,
        ExifInterface.ORIENTATION_ROTATE_270
    )
    if (orientation in swappedOrientation) return true
    return degrees == 90 || degrees == 270
}

internal object BitmapExifUtils {

    private const val TAG = "BitmapExifUtils"

    fun getExifInfo(imageStream: ImageStream): ExifInfo {
        val exifOrientation = imageStream.exifOrientation()
        val exifDegrees = exifToDegrees(exifOrientation)
        val exifTranslation = exifToTranslation(exifOrientation)
        return ExifInfo(exifOrientation, exifDegrees, exifTranslation)
    }

    fun transformBitmap(source: Bitmap, exif: ExifInfo): Bitmap {
        val exifOrientation = exif.orientation
        if (exifOrientation == ExifInterface.ORIENTATION_UNDEFINED) return source

        val matrix = Matrix()
        if (exif.degrees != 0) {
            matrix.setRotate(exif.degrees.toFloat())
        }
        if (exif.translation != 1) {
            matrix.setScale(exif.translation.toFloat(), 1f)
        }
        if (!matrix.isIdentity) {
            return transformBitmap(source, matrix)
        }
        return source
    }

    private fun ImageStream.exifOrientation(): Int {
        var orientation = ExifInterface.ORIENTATION_UNDEFINED
        try {
            openStream()?.use { stream ->
                ExifInterface(stream).getAttribute(ExifInterface.TAG_ORIENTATION)
                    ?.toIntOrNull()
                    ?.let { orientation = it }
            }
        } catch (e: IOException) {
            android.util.Log.e(TAG, "getExifOrientation failed: $this", e)
        }
        return orientation
    }

    private fun exifToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90,
            ExifInterface.ORIENTATION_TRANSPOSE -> 90

            ExifInterface.ORIENTATION_ROTATE_180,
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180

            ExifInterface.ORIENTATION_ROTATE_270,
            ExifInterface.ORIENTATION_TRANSVERSE -> 270

            else -> 0
        }
    }

    private fun exifToTranslation(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL,
            ExifInterface.ORIENTATION_FLIP_VERTICAL,
            ExifInterface.ORIENTATION_TRANSPOSE,
            ExifInterface.ORIENTATION_TRANSVERSE -> -1

            else -> 1
        }
    }

    private fun transformBitmap(bitmap: Bitmap, transformMatrix: Matrix): Bitmap {
        return try {
            val converted = Bitmap.createBitmap(
                /* source = */ bitmap,
                /* x = */ 0,
                /* y = */ 0,
                /* width = */ bitmap.width,
                /* height = */ bitmap.height,
                /* m = */ transformMatrix,
                /* filter = */ true
            )

            android.util.Log.d(
                TAG,
                "transformBitmap: ${bitmap.width} x ${bitmap.height} -> ${converted.width}x${converted.height}"
            )

            if (!bitmap.sameAs(converted)) converted else bitmap
        } catch (error: OutOfMemoryError) {
            android.util.Log.e(TAG, "transformBitmap failed:", error)
            bitmap
        }
    }
}
