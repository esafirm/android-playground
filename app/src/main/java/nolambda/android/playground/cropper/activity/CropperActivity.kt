package nolambda.android.playground.cropper.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import nolambda.android.playground.ImagesViewModel
import nolambda.android.playground.cropper.CropError
import nolambda.android.playground.cropper.CropResult
import nolambda.android.playground.cropper.CropState
import nolambda.android.playground.cropper.CropperStyle
import nolambda.android.playground.cropper.HandlesConfig
import nolambda.android.playground.cropper.ui.CropperPreview

class CropperActivity : ComponentActivity() {

    private val viewModel: ImagesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.extras?.getParcelable<Uri>("uri")
        viewModel.setSelectedImage(requireNotNull(uri)) {
            when (it) {
                CropError.LoadingError,
                CropError.SavingError,
                CropResult.Cancelled,
                    -> finish()

                is CropResult.Success -> {
                    val resultUri = viewModel.saveImage(this@CropperActivity, uri, it.bitmap)
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("uri", resultUri)
                    })
                    finish()
                }
            }
        }

        setContent {
            Cropper(viewModel.imageCropper.cropState)
        }
    }

    @Composable
    private fun Cropper(state: CropState?) {
        if (state == null) return
        val style = remember {
            CropperStyle(
                handlesConfig = HandlesConfig.None,
            )
        }

        Column {
            DefaultTopBar(state)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clipToBounds()
            ) {
                CropperPreview(state = state, style = style, modifier = Modifier.fillMaxSize())
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

    data class CropperResult(val uri: Uri?)

    class CropperContract : ActivityResultContract<Uri, CropperResult>() {

        override fun createIntent(
            context: Context,
            input: Uri,
        ): Intent {
            return Intent(context, CropperActivity::class.java).apply {
                putExtra("uri", input)
            }
        }

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): CropperResult {
            val uri = intent?.getParcelableExtra<Uri>("uri")
            return CropperResult(uri)
        }
    }
}
