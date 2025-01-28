package nolambda.android.playground

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.CropperLoading
import nolambda.android.playground.cropper.CropperStyle
import nolambda.android.playground.cropper.HandlesConfig
import nolambda.android.playground.cropper.ui.ImageCropperDialog

@Composable
fun DemoContent(
    cropState: CropState?,
    loadingStatus: CropperLoading?,
    selectedImage: ImageBitmap?,
    onPick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (cropState != null) {
        ImageCropperDialog(
            state = cropState,
            style = CropperStyle(
                handlesConfig = HandlesConfig.None,
            ),
        )
    }
    if (cropState == null && loadingStatus != null) {
        LoadingDialog(status = loadingStatus)
    }
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImage != null) Image(
            bitmap = selectedImage, contentDescription = null,
            modifier = Modifier.weight(1f)
        ) else Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
            Text("No image selected !")
        }
        Button(onClick = onPick) {
            Text("Choose Image")
        }
    }
}
