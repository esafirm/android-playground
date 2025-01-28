package nolambda.android.playground

import android.app.Application
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import nolambda.android.playground.cropper.CropError
import nolambda.android.playground.cropper.CropResult
import nolambda.android.playground.cropper.ImageCropper
import nolambda.android.playground.cropper.crop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImagesViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _selectedImage = MutableStateFlow<ImageBitmap?>(null)
    private val _cropError = MutableStateFlow<CropError?>(null)

    val imageCropper = ImageCropper()
    val selectedImage = _selectedImage.asStateFlow()
    val cropError = _cropError.asStateFlow()

    fun cropErrorShown() {
        _cropError.value = null
    }

    fun setSelectedImage(uri: Uri) {
        viewModelScope.launch {
            when (val result = imageCropper.crop(uri, app)) {
                CropResult.Cancelled -> {}
                is CropError -> _cropError.value = result
                is CropResult.Success -> {
                    _selectedImage.value = result.bitmap
                }
            }
        }
    }
}
