package nolambda.android.playground.cropper.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import nolambda.android.playground.cropper.utils.ViewMat
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.times

@Composable
fun CropperPreview(
    state: CropState,
    modifier: Modifier = Modifier
) {
    val style = LocalCropperStyle.current
    val imgTransform by animateImgTransform(target = state.transform)
    val imgMat = remember(imgTransform, state.src.size) { imgTransform.asMatrix(state.src.size) }
    val viewMat = remember { ViewMat() }
    var view by remember { mutableStateOf(IntSize.Zero) }
    var pendingDrag by remember { mutableStateOf<DragHandle?>(null) }
    val viewPadding = LocalDensity.current.run { style.touchRad.toPx() }
    val totalMat = remember(viewMat.matrix, imgMat) { imgMat * viewMat.matrix }
    val image = rememberLoadedImage(state.src, view, totalMat)

    val cropRect = remember(state.region, viewMat.matrix) {
        viewMat.matrix.map(state.region)
    }
    val cropPath = remember(state.shape, cropRect) {
        state.shape.asPath(cropRect)
    }

    BringToView(
        autoZoomEnabled = style.autoZoom,
        hasOverride = pendingDrag != null,
        outer = view.toSize().toRect().deflate(viewPadding),
        mat = viewMat,
        local = state.region,
    )
    Canvas(
        modifier = modifier
            .onGloballyPositioned { view = it.size }
            .background(color = style.backgroundColor)
            .cropperTouch(
                region = state.region,
                onRegion = { state.region = it },
                touchRad = style.touchRad,
                handles = style.handles,
                viewMatrix = viewMat,
                pending = pendingDrag,
                onPending = { pendingDrag = it },
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
    outer: Rect,
    mat: ViewMatrix,
    local: Rect
) {
    if (outer.isEmpty) return
    DisposableEffect(Unit) {
        mat.snapFit(mat.matrix.map(local), outer)
        onDispose { }
    }
    if (!autoZoomEnabled) return
    var overrideBlock by remember { mutableStateOf(false) }
    LaunchedEffect(hasOverride, outer, local) {
        if (hasOverride) overrideBlock = true
        else {
            if (overrideBlock) {
                delay(500)
                overrideBlock = false
            }
            mat.fit(mat.matrix.map(local), outer)
        }
    }
}
