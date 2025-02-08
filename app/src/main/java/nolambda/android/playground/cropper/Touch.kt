package nolambda.android.playground.cropper

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import nolambda.android.playground.cropper.utils.ViewMatrix

private const val DoubleTapZoom = 1.5f

@Composable
internal fun Modifier.cropperTouch(
    viewMatrix: ViewMatrix,
    onActionEnd: () -> Unit,
): Modifier {
    val scope = rememberCoroutineScope()

    return this
        .pointerInput(Unit) {
            detectTransformGestures(panZoomLock = true) { c, pan, zoom, rotation ->
                viewMatrix.translate(pan)
                viewMatrix.zoom(c, zoom)
                viewMatrix.rotate(c, rotation)

                onActionEnd()
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    scope.launch {
                        viewMatrix.animateZoom(DoubleTapZoom, it)
                        onActionEnd()
                    }
                }
            )
        }
}
