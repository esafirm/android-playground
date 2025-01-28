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
import nolambda.android.playground.cropper.DragHandle
import nolambda.android.playground.cropper.LocalCropperStyle
import nolambda.android.playground.cropper.animateImgTransform
import nolambda.android.playground.cropper.asMatrix
import nolambda.android.playground.cropper.cropperTouch
import nolambda.android.playground.cropper.images.rememberLoadedImage
import nolambda.android.playground.cropper.shapePathOrError
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.setAspect
import nolambda.android.playground.cropper.utils.times
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

private val AutoContainsDelay = 200.milliseconds

@Composable
fun CropperPreview(
    state: CropState,
    modifier: Modifier = Modifier
) {
    val style = LocalCropperStyle.current
    val viewPadding = LocalDensity.current.run { style.touchRad.toPx() }

    var pendingDrag by remember { mutableStateOf<DragHandle?>(null) }
    val actionSession = remember { ActionSession() }

    val viewMatrix = state.viewMatrix
    val imgTransform by animateImgTransform(target = state.transform)
    val imgMat = remember(imgTransform, state.src.size) { imgTransform.asMatrix(state.src.size) }
    val totalMat = remember(viewMatrix.matrix, imgMat) { imgMat * viewMatrix.matrix }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    val image = rememberLoadedImage(state.src, viewSize, totalMat)

    val imgRect = viewMatrix.matrix.map(state.imgRect)
    val outerRect = remember(viewSize, viewPadding) {
        viewSize.toSize().toRect().deflate(viewPadding)
    }
    val cropSpec = state.cropSpec

    LaunchedEffect(outerRect) {
        if (outerRect.isEmpty) return@LaunchedEffect

        state.region = state.region.setAspect(style.defaultAspectRatio)
        viewMatrix.fit(viewMatrix.matrix.map(state.region), outerRect)

        val cropRect = viewMatrix.matrix.map(state.region)
        state.cropSpec = CropSpec.Ready(rect = cropRect)
    }

    LaunchedEffect(state.transform) {
        actionSession.next()
    }

    AutoContains(
        mat = viewMatrix,
        cropSpec = cropSpec,
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
                    cropSpec.whenReady {
                        viewMatrix.translate(
                            offset = delta,
                            crop = (cropSpec as CropSpec.Ready).rect,
                            imgRect = imgRect
                        )
                    }
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
    inner: Rect,
    outerRect: Rect,
    actionId: Int,
) {
    if (actionId == ActionSession.DEFAULT) return
    cropSpec.whenReady { spec ->
        LaunchedEffect(actionId, outerRect, spec.rect) {
            delay(AutoContainsDelay)
            mat.animateContains(
                inner = mat.matrix.map(inner),
                outer = outerRect,
                crop = spec.rect,
                imgRect = imgRect,
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
