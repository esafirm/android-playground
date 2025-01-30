package nolambda.android.playground.cropper

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import nolambda.android.playground.cropper.utils.DragState
import nolambda.android.playground.cropper.utils.RotateState
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.ZoomState
import nolambda.android.playground.cropper.utils.abs
import nolambda.android.playground.cropper.utils.onGestures
import nolambda.android.playground.cropper.utils.rememberGestureState
import nolambda.android.playground.cropper.utils.resize

private val MoveHandle = Offset(.5f, .5f)

internal class DragHandle(
    val handle: Offset,
    val initialPos: Offset,
    val initialRegion: Rect
)

internal fun Modifier.cropperTouch(
    region: Rect,
    touchRad: Dp,
    handles: List<Offset>,
    viewMatrix: ViewMatrix,
    pending: DragHandle?,

    onRegion: (Rect) -> Unit,
    onTranslate: (Offset) -> Unit,
    onPending: (DragHandle?) -> Unit,
    onZoomEnd: () -> Unit,
): Modifier = composed {
    val touchRadPx2 = LocalDensity.current.run {
        remember(touchRad, viewMatrix.scale) { touchRad.toPx() / viewMatrix.scale }.let { it * it }
    }
    onGestures(
        rememberGestureState(
            zoom = ZoomState(
                begin = { center -> viewMatrix.zoomStart(center) },
                next = { center, scale -> viewMatrix.zoom(center, scale) },
                done = onZoomEnd
            ),
            drag = DragState(
                begin = { pos ->
                    val localPos = viewMatrix.invMatrix.map(pos)
                    val handle = handles.findHandle(
                        region = region,
                        pos = localPos,
                        touchRadPx2 = touchRadPx2
                    )
                    if (handle != null) {
                        onPending(DragHandle(handle, localPos, region))
                    }
                },
                next = { _, pos, _ ->
                    pending?.let {
                        val localPos = viewMatrix.invMatrix.map(pos)
                        val delta = (localPos - pending.initialPos).round().toOffset()
                        if (pending.handle != MoveHandle) {
                            val newRegion = pending.initialRegion.resize(pending.handle, delta)
                            onRegion(newRegion)
                        } else {
                            onTranslate(delta)
                        }
                    }
                },
                done = {
                    onPending(null)
                }
            ),
            rotate = RotateState(
                next = { rotation, center -> viewMatrix.rotate(center, rotation) },
                done = onZoomEnd
            )
        )
    )
}

private fun List<Offset>.findHandle(
    region: Rect,
    pos: Offset,
    touchRadPx2: Float
): Offset? {
    firstOrNull { (region.abs(it) - pos).getDistanceSquared() <= touchRadPx2 }?.let { return it }
    if (region.contains(pos)) return MoveHandle
    return null
}
