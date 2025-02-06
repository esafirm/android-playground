package nolambda.android.playground.cropper.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import nolambda.android.playground.cropper.utils.fitIn
import nolambda.android.playground.cropper.utils.setRectToRect

@Composable
internal fun rememberLoadedImage(
    src: ImageSrc,
    view: IntSize,
    imgToView: Matrix
): DecodeResult? {
    var full by remember { mutableStateOf<DecodeResult?>(null) }
    var enhanced by remember { mutableStateOf<DecodeResult?>(null) }

    LaunchedEffect(src, view) {
        val fullMat = Matrix().apply {
            val imgRect = src.size.toSize().toRect()
            setRectToRect(imgRect, imgRect.fitIn(view.toSize().toRect()))
        }
        val fullParams = getDecodeParams(view, src.size, fullMat)
        if (fullParams != null) full = src.open(fullParams)
    }

    LaunchedEffect(src, view, imgToView, full == null) decode@{
        if (full == null) return@decode
        if (enhanced == null) yield()
        val params = getDecodeParams(view, src.size, imgToView) ?: return@decode
        if (enhanced?.params?.contains(params) == true) return@decode
        if (full?.params?.contains(params) == true) {
            enhanced = full
            return@decode
        }
        enhanced = null
        delay(500)
        enhanced = src.open(params)
    }

    return enhanced ?: full
}
