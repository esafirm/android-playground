package nolambda.android.playground.cropper.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.BorderHorizontal
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.flipHorizontal
import nolambda.android.playground.cropper.flipVertical
import nolambda.android.playground.cropper.rotLeft
import nolambda.android.playground.cropper.rotRight

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
