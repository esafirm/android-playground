package nolambda.android.playground.cropper.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max

internal interface GestureState {
    val zoom: ZoomState
    val drag: DragState
    val tap: TapState
    val rotate: RotateState
}

internal interface DragState {
    fun onBegin(x: Float, y: Float) = Unit
    fun onNext(dx: Float, dy: Float, x: Float, y: Float, pointers: Int) = Unit
    fun onDone() = Unit
}

internal inline fun DragState(
    crossinline begin: (pos: Offset) -> Unit,
    crossinline done: () -> Unit,
    crossinline next: (delta: Offset, pos: Offset, pointers: Int) -> Unit,
): DragState = object : DragState {
    override fun onBegin(x: Float, y: Float) = begin(Offset(x, y))
    override fun onNext(dx: Float, dy: Float, x: Float, y: Float, pointers: Int) =
        next(Offset(dx, dy), Offset(x, y), pointers)

    override fun onDone() = done()
}

internal interface TapState {
    fun onTap(x: Float, y: Float, pointers: Int) = Unit
    fun onLongPress(x: Float, y: Float, pointers: Int) = Unit
}

internal inline fun TapState(
    crossinline longPress: (pos: Offset, pointers: Int) -> Unit = { _, _ -> },
    crossinline tap: (pos: Offset, pointers: Int) -> Unit = { _, _ -> },
) = object : TapState {
    override fun onTap(x: Float, y: Float, pointers: Int) = tap(Offset(x, y), pointers)
    override fun onLongPress(x: Float, y: Float, pointers: Int) = longPress(Offset(x, y), pointers)
}

internal interface ZoomState {
    fun onBegin(cx: Float, cy: Float) = Unit
    fun onNext(scale: Float, cx: Float, cy: Float) = Unit
    fun onDone() = Unit
}

internal inline fun ZoomState(
    crossinline begin: (center: Offset) -> Unit,
    crossinline done: () -> Unit,
    crossinline next: (center: Offset, scale: Float) -> Unit,
): ZoomState = object : ZoomState {
    override fun onBegin(cx: Float, cy: Float) = begin(Offset(cx, cy))
    override fun onNext(scale: Float, cx: Float, cy: Float) = next(Offset(cx, cy), scale)
    override fun onDone() = done()
}

internal interface RotateState {
    fun onNext(rotation: Float, cx: Float, cy: Float) = Unit
    fun onDone() = Unit
}

internal inline fun RotateState(
    crossinline done: () -> Unit,
    crossinline next: (rotation: Float, center: Offset) -> Unit,
): RotateState = object : RotateState {
    override fun onNext(rotation: Float, cx: Float, cy: Float) = next(rotation, Offset(cx, cy))
    override fun onDone() = done()
}

@Composable
internal fun rememberGestureState(
    zoom: ZoomState? = null,
    drag: DragState? = null,
    tap: TapState? = null,
    rotate: RotateState? = null,
): GestureState {
    val zoomState by rememberUpdatedState(newValue = zoom ?: object : ZoomState {})
    val dragState by rememberUpdatedState(newValue = drag ?: object : DragState {})
    val tapState by rememberUpdatedState(newValue = tap ?: object : TapState {})
    val rotateState by rememberUpdatedState(newValue = rotate ?: object : RotateState {})

    return object : GestureState {
        override val zoom: ZoomState get() = zoomState
        override val drag: DragState get() = dragState
        override val tap: TapState get() = tapState
        override val rotate: RotateState get() = rotateState
    }
}

private data class GestureData(
    var dragId: PointerId = PointerId(-1),
    var firstPos: Offset = Offset.Unspecified,
    var pos: Offset = Offset.Unspecified,
    var nextPos: Offset = Offset.Unspecified,
    var pointers: Int = 0,
    var maxPointers: Int = 0,
    var isDrag: Boolean = false,
    var isZoom: Boolean = false,
    var isRotate: Boolean = false,
    var isTap: Boolean = false,
)


internal fun Modifier.onGestures(state: GestureState): Modifier {
    return pointerInput(Unit) {
        coroutineScope {
            var info = GestureData()
            launch {
                detectTapGestures(
                    onLongPress = { state.tap.onLongPress(it.x, it.y, info.maxPointers) },
                    onTap = { state.tap.onTap(it.x, it.y, info.maxPointers) }
                )
            }
            launch {
                detectTransformGestures(panZoomLock = true) { c, _, zoom, rotation ->
                    android.util.Log.d("Gesture", "c: $c, zoom: $zoom, rotation: $rotation")
                    when {
                        !info.isDrag && !info.isZoom -> {
                            if (info.pointers == 1) {
                                state.drag.onBegin(info.firstPos.x, info.firstPos.y)
                                info.pos = info.firstPos
                                info.isDrag = true
                            } else if (info.pointers > 1) {
                                state.zoom.onBegin(c.x, c.y)
                                info.isZoom = true
                                info.isRotate = true
                            }
                        }

                        info.isDrag -> {
                            state.drag.onNext(
                                info.nextPos.x - info.pos.x, info.nextPos.y - info.pos.y,
                                info.nextPos.x, info.nextPos.y, info.pointers
                            )
                            info.pos = info.nextPos
                        }

                        else -> {
                            if (zoom != 1f) state.zoom.onNext(zoom, c.x, c.y)
                            if (rotation != 0f) state.rotate.onNext(rotation, c.x, c.y)
                        }
                    }
                }
            }
            launch {
                awaitEachGesture {
                    info = GestureData()
                    val first = awaitFirstDown(requireUnconsumed = false)
                    info.apply {
                        dragId = first.id
                        firstPos = first.position
                        pointers = 1
                        maxPointers = 1
                    }
                    while (info.pointers > 0) {
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        var dragPointer: PointerInputChange? = null
                        for (change in event.changes) {
                            when {
                                change.changedToDown() -> info.pointers++
                                change.changedToUp() -> info.pointers--
                            }
                            info.maxPointers = max(info.maxPointers, info.pointers)
                            if (change.id == info.dragId) dragPointer = change
                        }
                        dragPointer = dragPointer ?: event.changes.firstOrNull { it.pressed }
                        dragPointer?.let {
                            info.nextPos = it.position
                            if (info.dragId != it.id) {
                                info.pos = info.nextPos
                                info.dragId = it.id
                            }
                        }
                    }
                    if (info.isDrag) state.drag.onDone()
                    if (info.isZoom) state.zoom.onDone()
                    if (info.isRotate) state.rotate.onDone()
                }
            }
        }
    }
}
