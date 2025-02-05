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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import nolambda.android.playground.cropper.initialize
import nolambda.android.playground.cropper.shapePathOrError
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.rotation
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
        state.initialize(outer = outerRect, aspectRatio = style.defaultAspectRatio)
    }

    LaunchedEffect(state.transform) {
        actionSession.next()
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
                center = outerRect.center,
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
                })
    ) {
        withTransform({ transform(viewMatrix.matrix) }) {
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

                drawHelperBounds(
                    imgRect = imgRect,
                    state = state,
                    crop = it.rect,
                    viewMatrix = viewMatrix
                )
            }
        }
    }
}

/**
 * Bounds that drawed to help development. Should be not used in production
 */
private fun DrawScope.drawHelperBounds(
    imgRect: Rect,
    state: CropState,
    crop: Rect,
    viewMatrix: ViewMatrix,
) {
//    val rotation = Matrix().apply {
//        rotateZ(viewMatrix.matrix.rotation())
//        scale(0.5f, 0.5f)
//    }
//    val rotatedCrop = rotation.map(crop)
//    val translate = crop.center - rotatedCrop.center
//    val finalCrop = rotatedCrop.translate(
//        translateX = translate.x,
//        translateY = translate.y
//    )
//
//    drawBounds(
//        xAxisColor = Color.Red,
//        yAxisColor = Color.Blue,
//        rect = finalCrop
//    )
//
//    val matrixWithoutRotation = Matrix().apply {
//        setFrom(viewMatrix.matrix)
//        rotateZ(viewMatrix.matrix.rotation())
//        scale(0.5f, 0.5f)
//    }
//    val unrotatedImg = matrixWithoutRotation.map(state.imgRect)
//    val translateImg = crop.center - unrotatedImg.center
//    val centeredImg = unrotatedImg.translate(
//        translateX = translateImg.x,
//        translateY = translateImg.y
//    )
//    drawBounds(
//        xAxisColor = Color.Yellow,
//        yAxisColor = Color.Green,
//        rect = centeredImg
//    )
}

private fun DrawScope.drawBounds(
    xAxisColor: Color,
    yAxisColor: Color,
    rect: Rect,
    strokeWidth: Float = 4f,
) {
    drawCircle(
        color = xAxisColor,
        center = rect.center,
        radius = 10f
    )

    drawLine(
        yAxisColor,
        start = rect.topLeft,
        end = rect.bottomLeft,
        strokeWidth = strokeWidth,
    )
    drawLine(
        yAxisColor,
        start = rect.topRight,
        end = rect.bottomRight,
        strokeWidth = strokeWidth,
    )
    drawLine(
        xAxisColor,
        start = rect.topLeft,
        end = rect.topRight,
        strokeWidth = strokeWidth,
    )
    drawLine(
        xAxisColor,
        start = rect.bottomLeft,
        end = rect.bottomRight,
        strokeWidth = strokeWidth,
    )
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
