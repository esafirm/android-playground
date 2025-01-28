package nolambda.android.playground.cropper.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

    var pendingDrag by remember { mutableStateOf<DragHandle?>(null) }
    val actionSession = remember { ActionSession() }

    val imgTransform by animateImgTransform(target = state.transform)
    val imgMat = remember(imgTransform, state.src.size) { imgTransform.asMatrix(state.src.size) }
    val viewMatrix: ViewMatrix = remember { ViewMatrixImpl() }
    var view by remember { mutableStateOf(IntSize.Zero) }
    val viewPadding = LocalDensity.current.run { style.touchRad.toPx() }
    val totalMat = remember(viewMatrix.matrix, imgMat) { imgMat * viewMatrix.matrix }
    val image = rememberLoadedImage(state.src, view, totalMat)

    var isInitialized by remember { mutableStateOf(false) }
    val cropRect = remember(state.region, isInitialized) {
        viewMatrix.matrix.map(state.region)
    }
    val cropPath = remember(state.shape, cropRect) {
        state.shape.asPath(cropRect)
    }
    val imgRect = viewMatrix.matrix.map(state.imgRect)
    val outerRect = view.toSize().toRect().deflate(viewPadding)

    // Set the cropper initial aspect ratio
    LaunchedEffect(Unit) {
        state.region = state.region.setAspect(style.defaultAspectRatio)
    }

    AutoContains(
        mat = viewMatrix,
        cropRect = cropRect,
        imgRect = imgRect,
        outerRect = outerRect,
        inner = state.region,
        actionId = actionSession.actionId,
    )

    BringToView(
        autoZoomEnabled = false,
        hasOverride = pendingDrag != null,
        outer = outerRect,
        mat = viewMatrix,
        inner = state.region,
    ) {
        isInitialized = true
    }

    Canvas(
        modifier = modifier
            .onGloballyPositioned { view = it.size }
            .background(color = style.backgroundColor)
            .cropperTouch(
                region = state.region,
                onRegion = { state.region = it },
                touchRad = style.touchRad,
                handles = style.handles,
                viewMatrix = viewMatrix,
                pending = pendingDrag,
                onPending = { nextPending ->
                    pendingDrag = nextPending
                    if (nextPending == null) {
                        actionSession.next()
                    }
                },
                onZoomEnd = {
                    actionSession.next()
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

/**
 * A composable to control the view matrix to fit the region.
 */
@Composable
private fun BringToView(
    autoZoomEnabled: Boolean,
    hasOverride: Boolean,
    mat: ViewMatrix,
    outer: Rect,
    inner: Rect,
    onInitialized: () -> Unit = {}
) {
    if (outer.isEmpty) return
    DisposableEffect(Unit) {
        mat.fit(mat.matrix.map(inner), outer)
        onInitialized()
        onDispose { }
    }
    if (!autoZoomEnabled) return
    var overrideBlock by remember { mutableStateOf(false) }
    LaunchedEffect(hasOverride, outer, inner) {
        if (hasOverride) overrideBlock = true
        else {
            if (overrideBlock) {
                delay(500)
                overrideBlock = false
            }
            Log.d("CropperPreview", "BringToView: inner rect: $inner")
            mat.animateFit(mat.matrix.map(inner), outer)
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
