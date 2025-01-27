package nolambda.android.playground.cropper.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.BorderHorizontal
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.FlipToFront
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import nolambda.android.playground.cropper.AspectRatio
import nolambda.android.playground.cropper.CropShape
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.LocalCropperStyle
import nolambda.android.playground.cropper.flipHorizontal
import nolambda.android.playground.cropper.flipVertical
import nolambda.android.playground.cropper.rotLeft
import nolambda.android.playground.cropper.rotRight
import nolambda.android.playground.cropper.utils.eq0
import nolambda.android.playground.cropper.utils.setAspect

private fun Size.isAspect(aspect: AspectRatio): Boolean {
    return ((width / height) - (aspect.x.toFloat() / aspect.y)).eq0()
}

internal val LocalVerticalControls = staticCompositionLocalOf { false }

@Composable
internal fun CropperControls(
    isVertical: Boolean,
    state: CropState,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalVerticalControls provides isVertical) {
        ButtonsBar(modifier = modifier) {
            IconButton(onClick = { state.rotLeft() }) {
                Icon(Icons.AutoMirrored.Filled.RotateLeft, null)
            }
            IconButton(onClick = { state.rotRight() }) {
                Icon(Icons.AutoMirrored.Filled.RotateRight, null)
            }
            IconButton(onClick = { state.flipHorizontal() }) {
                Icon(Icons.Default.Flip, null)
            }
            IconButton(onClick = { state.flipVertical() }) {
                Icon(Icons.Default.BorderHorizontal, null)
            }
            Box {
                var menu by remember { mutableStateOf(false) }
                IconButton(onClick = { menu = !menu }) {
                    Icon(Icons.Default.FlipToFront, null)
                }
                if (menu) AspectSelectionMenu(
                    onDismiss = { menu = false },
                    region = state.region,
                    onRegion = { state.region = it },
                    lock = state.aspectLock,
                    onLock = { state.aspectLock = it }
                )
            }
            LocalCropperStyle.current.shapes?.let { shapes ->
                Box {
                    var menu by remember { mutableStateOf(false) }
                    IconButton(onClick = { menu = !menu }) {
                        Icon(Icons.Default.Star, null)
                    }
                    if (menu) ShapeSelectionMenu(
                        onDismiss = { menu = false },
                        selected = state.shape,
                        onSelect = { state.shape = it },
                        shapes = shapes
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonsBar(
    modifier: Modifier = Modifier,
    buttons: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = .8f),
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
    ) {
        if (LocalVerticalControls.current) Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            buttons()
        } else Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            buttons()
        }
    }
}


@Composable
private fun ShapeSelectionMenu(
    onDismiss: () -> Unit,
    shapes: List<CropShape>,
    selected: CropShape,
    onSelect: (CropShape) -> Unit,
) {
    OptionsPopup(onDismiss = onDismiss, optionCount = shapes.size) { i ->
        val shape = shapes[i]
        ShapeItem(
            shape = shape, selected = selected == shape,
            onSelect = { onSelect(shape) })
    }
}


@Composable
private fun ShapeItem(
    shape: CropShape, selected: Boolean, onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (!selected) LocalContentColor.current
        else MaterialTheme.colorScheme.secondary
    )
    IconButton(
        modifier = modifier,
        onClick = onSelect
    ) {
        val shapeState by rememberUpdatedState(newValue = shape)
        Box(
            modifier = Modifier
                .size(20.dp)
                .drawWithCache {
                    val path = shapeState.asPath(size.toRect())
                    val strokeWidth = 2.dp.toPx()
                    onDrawWithContent {
                        drawPath(path = path, color = color, style = Stroke(strokeWidth))
                    }
                })
    }
}


@Composable
private fun AspectSelectionMenu(
    onDismiss: () -> Unit,
    region: Rect,
    onRegion: (Rect) -> Unit,
    lock: Boolean,
    onLock: (Boolean) -> Unit
) {
    val aspects = LocalCropperStyle.current.aspectRatios
    OptionsPopup(onDismiss = onDismiss, optionCount = 1 + aspects.size) { i ->
        val unselectedTint = LocalContentColor.current
        val selectedTint = MaterialTheme.colorScheme.secondary
        if (i == 0) IconButton(onClick = { onLock(!lock) }) {
            Icon(
                Icons.Default.Lock, null,
                tint = if (lock) selectedTint else unselectedTint
            )
        } else {
            val aspect = aspects[i - 1]
            val isSelected = region.size.isAspect(aspect)
            IconButton(
                onClick = { onRegion(region.setAspect(aspect)) }
            ) {
                Text(
                    "${aspect.x}:${aspect.y}",
                    color = if (isSelected) selectedTint else unselectedTint
                )
            }
        }
    }
}
