package nolambda.android.playground.cropper.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize

@Composable
internal fun rememberLoadedImage(
    src: ImageSrc,
    view: IntSize,
): DecodeResult? {
    var image by remember { mutableStateOf<DecodeResult?>(null) }
    LaunchedEffect(src, view) {
        image = src.open(DecodeParams(view))
    }
    return image
}
