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
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.DragHandle
import nolambda.android.playground.cropper.LocalCropperStyle
import nolambda.android.playground.cropper.animateImgTransform
import nolambda.android.playground.cropper.asMatrix
import nolambda.android.playground.cropper.cropperTouch
import nolambda.android.playground.cropper.images.rememberLoadedImage
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.ViewMatrixImpl
import nolambda.android.playground.cropper.utils.setAspect
import nolambda.android.playground.cropper.utils.times
import kotlin.random.Random

@Composable
fun CropperPreview(
    state: CropState,
    modifier: Modifier = Modifier
) {
    val style = LocalCropperStyle.current
    val viewPadding = LocalDensity.current.run { style.touchRad.toPx() }

    var pendingDrag by remember { mutableStateOf<DragHandle?>(null) }
    val actionSession = remember { ActionSession() }

    val viewMatrix: ViewMatrix = remember { ViewMatrixImpl() }
    val imgTransform by animateImgTransform(target = state.transform)
    val imgMat = remember(imgTransform, state.src.size) { imgTransform.asMatrix(state.src.size) }
    val totalMat = remember(viewMatrix.matrix, imgMat) { imgMat * viewMatrix.matrix }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    val image = rememberLoadedImage(state.src, viewSize, totalMat)

    val imgRect = viewMatrix.matrix.map(state.imgRect)
    val outerRect = remember(viewSize, viewPadding) {
        viewSize.toSize().toRect().deflate(viewPadding)
    }

    LaunchedEffect(outerRect) {
        if (outerRect.isEmpty) return@LaunchedEffect
        state.region = state.region.setAspect(style.defaultAspectRatio)
        viewMatrix.fit(viewMatrix.matrix.map(state.region), outerRect)
    }

    LaunchedEffect(state.transform) {
        actionSession.next()
    }

    val cropRect = remember(state.region, outerRect) {
        viewMatrix.matrix.map(state.region)
    }
    val cropPath = remember(state.shape, cropRect) {
        state.shape.asPath(cropRect)
    }

    AutoContains(
        mat = viewMatrix,
        cropRect = cropRect,
        imgRect = imgRect,
        outerRect = outerRect,
        inner = state.region,
        actionId = actionSession.actionId,
    )

    Canvas(
        modifier = modifier
            .onGloballyPositioned { viewSize = it.size }
            .background(color = style.backgroundColor)
            .cropperTouch(
                region = state.region,
                touchRad = style.touchRad,
                handles = style.handles,
                viewMatrix = viewMatrix,
                pending = pendingDrag,

                onRegion = { state.region = it },
                onZoomEnd = { actionSession.next() },
                onPending = { nextPending ->
                    pendingDrag = nextPending
                    if (nextPending == null) {
                        actionSession.next()
                    }
                },
                onTranslate = { delta ->
                    viewMatrix.translate(
                        offset = delta,
                        crop = cropRect,
                        imgRect = imgRect
                    )
                }
            )
    ) {
        withTransform({ transform(totalMat) }) {
            image?.let { (params, bitmap) ->
                drawImage(
                    image = bitmap,
                    dstOffset = params.subset.topLeft,
                    dstSize = params.subset.size
                )
            }
        }
        with(style) {
            clipPath(cropPath, ClipOp.Difference) {
                drawRect(color = overlayColor)
            }
            drawCropRect(cropRect)
        }

    }
}

@Composable
private fun AutoContains(
    mat: ViewMatrix,
    cropRect: Rect,
    imgRect: Rect,
    inner: Rect,
    outerRect: Rect,
    actionId: Int,
) {
    if (actionId == ActionSession.DEFAULT) return
    LaunchedEffect(actionId, outerRect, cropRect) {
        delay(200)
        mat.animateContains(
            inner = mat.matrix.map(inner),
            outer = outerRect,
            crop = cropRect,
            imgRect = imgRect,
        )
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
