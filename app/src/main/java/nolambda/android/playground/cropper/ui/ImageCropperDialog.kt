package nolambda.android.playground.cropper.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.CropperStyle

private val CropperDialogProperties = DialogProperties(
    usePlatformDefaultWidth = false,
    dismissOnBackPress = false,
    dismissOnClickOutside = false
)

@Composable
fun ImageCropperDialog(
    state: CropState,
    style: CropperStyle = CropperStyle(),
    dialogProperties: DialogProperties = CropperDialogProperties,
    dialogPadding: PaddingValues = PaddingValues(16.dp),
    dialogShape: Shape = RoundedCornerShape(8.dp),
    topBar: @Composable (CropState) -> Unit = { DefaultTopBar(it) },
) {
    Dialog(
        onDismissRequest = { state.done(accept = false) },
        properties = dialogProperties,
    ) {
        Surface(
            modifier = Modifier.padding(dialogPadding),
            shape = dialogShape,
        ) {
            Column {
                topBar(state)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clipToBounds()
                ) {
                    CropperPreview(state = state, style = style, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultTopBar(state: CropState) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { state.done(accept = false) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = { state.reset() }) {
                Icon(Icons.Default.Refresh, null)
            }
            IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
                Icon(Icons.Default.Done, null)
            }
        }
    )
}
