package nolambda.android.playground

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nolambda.android.playground.cropper.CropError
import nolambda.android.playground.cropper.CropResult
import nolambda.android.playground.cropper.ImageCropper
import nolambda.android.playground.cropper.crop
import java.io.File

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _selectedImage = MutableStateFlow<ImageBitmap?>(null)
    private val _cropError = MutableStateFlow<CropError?>(null)

    val imageCropper = ImageCropper.create()
    val selectedImage = _selectedImage.asStateFlow()
    val cropError = _cropError.asStateFlow()

    fun cropErrorShown() {
        _cropError.value = null
    }

    fun setSelectedImage(
        uri: Uri,
        onResult: (CropResult) -> Unit
    ) = viewModelScope.launch {
        val result = imageCropper.crop(uri, app)
        onResult(result)
    }

    fun saveImage(context: Context, source: Uri, bmp: ImageBitmap): Uri {
        val bitmap = bmp.asAndroidBitmap()

        val outputDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileOutput = buildImageCacheFile(source, outputDir).toUri()

        val output = context.contentResolver.openOutputStream(fileOutput)
        if (output == null) error("Could not open output stream for $fileOutput")

        output.use {
            val result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            if (!result) error("Failed to save bitmap to $fileOutput")
            bitmap.recycle()
        }

        return fileOutput
    }

    private fun buildImageCacheFile(source: Uri, imageCache: File): File {
        val fileName = source.toString()
            .substringAfterLast("/") // get file name
            // remove duplicated `image-cropper-` prefix in case of
            // building cropped file path based on the copied file
            .removePrefix("image-cropper-")

        val path = "image-cropper-${System.currentTimeMillis()}-${fileName}.png"
        return File(imageCache, path)
    }
}
