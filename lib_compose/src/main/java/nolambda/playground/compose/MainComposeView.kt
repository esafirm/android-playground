package nolambda.playground.compose

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setViewContent

@Composable
fun SimpleSample() {
    Column {
        Text(text = "Hiyaaa")
    }
}

//class MainActivity : Activity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent(SimpleSample())
//    }
//}