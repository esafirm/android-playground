package nolambda.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import nolambda.android.playground.cropper.activity.CropperActivity
import nolambda.android.playground.ui.theme.AndroidPlaygroundTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ImagesViewModel by viewModels()

    private val crop = registerForActivityResult(CropperActivity.CropperContract()) {
        android.util.Log.d("Cropper", "Result: $it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewModelDemo(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        crop.launch(it)
                    }
                }
            }
        }
    }
}
