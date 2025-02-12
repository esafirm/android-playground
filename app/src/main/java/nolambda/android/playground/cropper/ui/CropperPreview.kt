package nolambda.android.playground.cropper.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import nolambda.android.playground.cropper.CropSpec
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.CropperStyle
import nolambda.android.playground.cropper.cropperTouch
import nolambda.android.playground.cropper.images.rememberLoadedImage
import nolambda.android.playground.cropper.initialize
import nolambda.android.playground.cropper.shapePathOrError
import nolambda.android.playground.cropper.utils.ViewMatrix
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

private val AutoContainsDelay = 200.milliseconds

@Composable
fun CropperPreview(
    state: CropState,
    style: CropperStyle,
    modifier: Modifier = Modifier
) {
    val viewPadding = LocalDensity.current.run { style.touchRad.toPx() }

    val viewMatrix = state.viewMatrix
    val cropSpec = state.cropSpec

    val actionSession = remember { ActionSession() }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }

    val image = rememberLoadedImage(state.src, viewSize)

    val imgRect = remember(viewMatrix.matrix) { viewMatrix.matrix.map(state.imgRect) }
    val outerRect = remember(viewSize, viewPadding) {
        viewSize.toSize().toRect().deflate(viewPadding)
    }

    LaunchedEffect(outerRect, cropSpec) {
        if (outerRect.isEmpty) return@LaunchedEffect
        if (cropSpec !is CropSpec.Empty) return@LaunchedEffect
        state.initialize(outer = outerRect, aspectRatio = style.defaultAspectRatio)
    }

    AutoContains(
        mat = viewMatrix,
        cropSpec = cropSpec,
        imgRect = imgRect,
        outerRect = outerRect,
        originalImg = state.imgRect,
        actionId = actionSession.actionId,
    )

    Canvas(
        modifier = modifier
            .onGloballyPositioned { viewSize = it.size }
            .background(color = style.backgroundColor)
            .cropperTouch(
                viewMatrix = viewMatrix,
                onActionEnd = actionSession::next
            )
    ) {
        withTransform({ transform(viewMatrix.matrix) }) {
            image?.let {
                drawImage(
                    image = it.bmp,
                    dstSize = state.src.size,
                )
            }
        }
        with(style) {
            cropSpec.whenReady {
                clipPath(state.shapePathOrError(), ClipOp.Difference) {
                    drawRect(color = overlayColor)
                }
                drawCropRect(it.rect)
            }
        }
    }
}

@Composable
private fun AutoContains(
    mat: ViewMatrix,
    cropSpec: CropSpec,
    imgRect: Rect,
    outerRect: Rect,
    originalImg: Rect,
    actionId: Int,
) {
    if (actionId == ActionSession.DEFAULT) return
    cropSpec.whenReady { spec ->
        LaunchedEffect(actionId, outerRect, spec.rect) {
            delay(AutoContainsDelay)
            mat.animateImageToWrapCropBounds(
                outer = outerRect,
                crop = spec.rect,
                imgRect = imgRect,
                originalImg = originalImg
            )
        }
    }
}

private inline fun CropSpec.whenReady(body: (CropSpec.Ready) -> Unit) {
    if (this is CropSpec.Ready) {
        body(this)
    }
}

@Stable
private class ActionSession {
    private val actionIdState = mutableIntStateOf(DEFAULT)
    val actionId get() = actionIdState.intValue

    fun next() {
        actionIdState.intValue = Random.nextInt()
    }

    companion object {
        const val DEFAULT = 1
    }
}
