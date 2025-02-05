package nolambda.android.playground

import android.graphics.RectF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import nolambda.android.playground.cropper.utils.contains
import nolambda.android.playground.cropper.utils.mapPoints
import nolambda.android.playground.ui.theme.AndroidPlaygroundTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val viewModel: ImagesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        correctCalculation()
        correctCalculationCompose()
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewModelDemo(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    private fun experiment() {
        val rect = Rect(0f, 208f, 984f, 1192f)
        log("first rect: ${rect.info()}")

        val px = 0f
        val py = 0f

        val matrix = Matrix()
        matrix.translate(px, py)
        matrix.rotateZ(5.1692085f)
        matrix.translate(-px, -py)

        val second = matrix.map(rect)
        log("second rect: ${second.info()}")
    }

    /**
     * A correct calculation in android.graphics.Matrix
     */
    private fun correctCalculation() {
        val imageCorners = floatArrayOf(
            -3735.4475f,
            -3414.5076f,
            798.6365f,
            -3612.6084f,
            1227.8547f,
            6211.24f,
            -3306.2292f,
            6409.341f
        )

        val cropRect = RectF(0.0f, 208.0f, 984.0f, 1192.0f)

        val androidMatrix = android.graphics.Matrix()
        androidMatrix.setRotate(2.5017447f)

        // Unrotate image
        val unrotatedImageCorners = imageCorners.copyOf(imageCorners.size)
        androidMatrix.mapPoints(unrotatedImageCorners)

        // Unrotate crop
        val unrotatedCropCorner = getCornersFromRectF(cropRect)
        androidMatrix.mapPoints(unrotatedCropCorner)

        val unrotatedImageRect = trapToRectF(unrotatedImageCorners)
        val unrotatedCropRect = trapToRectF(unrotatedCropCorner)

        log("image: ${unrotatedImageRect.info()}")
        log("crop: ${unrotatedCropRect.info()}")
        log("contains: ${unrotatedImageRect.contains(unrotatedCropRect)}\n")

        // Result should be
        // Img: RectF(-3582.8, -3574.3, 955.6, 6258.9)
        // Crop: RectF(-52.0, 207.8, 974.0, 1233.8)
    }

    private fun correctCalculationCompose() {
        val imageCorners = floatArrayOf(
            -3735.4475f,
            -3414.5076f,
            798.6365f,
            -3612.6084f,
            1227.8547f,
            6211.24f,
            -3306.2292f,
            6409.341f
        )

        val cropRect = Rect(0.0f, 208.0f, 984.0f, 1192.0f)

        val matrix = Matrix()
        matrix.rotateZ(2.5017447f)

        // Unrotate image
        val unrotatedImageCorners = imageCorners.copyOf(imageCorners.size)
        matrix.mapPoints(unrotatedImageCorners)

        // Unrotate crop
        val unrotatedCropCorner = getCornersFromRect(cropRect)
        matrix.mapPoints(unrotatedCropCorner)

        val unrotatedImageRect = trapToRect(unrotatedImageCorners)
        val unrotatedCropRect = trapToRect(unrotatedCropCorner)

        log("image: ${unrotatedImageRect.info()}")
        log("crop: ${unrotatedCropRect.info()}")
        log("contains: ${unrotatedImageRect.contains(unrotatedCropRect)}\n")

        // Result should be
        // Img: RectF(-3582.8, -3574.3, 955.6, 6258.9)
        // Crop: RectF(-52.0, 207.8, 974.0, 1233.8)
    }

    private fun log(message: String) {
        android.util.Log.d("ViewMat", message)
    }

    private fun Rect.info(): String {
        return "$this (${center})"
    }

    private fun RectF.info(): String {
        return "$this (${centerX()}, ${centerY()})"
    }

    private fun getCornersFromRectF(r: RectF): FloatArray {
        return floatArrayOf(
            r.left, r.top,
            r.right, r.top,
            r.right, r.bottom,
            r.left, r.bottom
        )
    }

    private fun getCornersFromRect(r: Rect): FloatArray {
        return floatArrayOf(
            r.left, r.top,
            r.right, r.top,
            r.right, r.bottom,
            r.left, r.bottom
        )
    }

    private fun trapToRectF(array: FloatArray): RectF {
        val r = RectF(
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
            Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
        )
        for (i in 1 until array.size step 2) {
            val x = (array[i - 1] * 10).roundToInt() / 10f
            val y = (array[i] * 10).roundToInt() / 10f
            r.left = minOf(x, r.left)
            r.top = minOf(y, r.top)
            r.right = maxOf(x, r.right)
            r.bottom = maxOf(y, r.bottom)
        }
        r.sort()
        return r
    }

    private fun trapToRect(array: FloatArray): Rect {
        var left = Float.POSITIVE_INFINITY
        var top = Float.POSITIVE_INFINITY
        var right = Float.NEGATIVE_INFINITY
        var bottom = Float.NEGATIVE_INFINITY

        for (i in 1 until array.size step 2) {
            val x = (array[i - 1] * 10).roundToInt() / 10f
            val y = (array[i] * 10).roundToInt() / 10f
            left = minOf(x, left)
            top = minOf(y, top)
            right = maxOf(x, right)
            bottom = maxOf(y, bottom)
        }

        return Rect(left, top, right, bottom)
    }
}
