package nolambda.android.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import nolambda.android.playground.cropper.rememberImagePicker

@Composable
fun ViewModelDemo(viewModel: ImagesViewModel, modifier: Modifier = Modifier) {
    val imagePicker = rememberImagePicker { uri ->
        viewModel.setSelectedImage(uri)
    }
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
