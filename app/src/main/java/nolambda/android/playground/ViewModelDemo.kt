package nolambda.android.playground

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import nolambda.android.playground.cropper.rememberImagePicker

@Composable
fun ViewModelDemo(
    viewModel: ImagesViewModel,
    modifier: Modifier = Modifier,
    onCropRequest: (Uri) -> Unit
) {
    val imagePicker = rememberImagePicker { uri -> onCropRequest(uri) }
    DemoContent(
        cropState = viewModel.imageCropper.cropState,
        loadingStatus = viewModel.imageCropper.loadingStatus,
        selectedImage = viewModel.selectedImage.collectAsState().value,
        onPick = { imagePicker.pick() },
        modifier = modifier
    )
    viewModel.cropError.collectAsState().value?.let { error ->
        CropErrorDialog(error, onDismiss = { viewModel.cropErrorShown() })
    }
}
