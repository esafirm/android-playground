package nolambda.android.playground.cropper

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.toSize
import nolambda.android.playground.cropper.images.ImageSrc
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.ViewMatrixImpl
import nolambda.android.playground.cropper.utils.setAspect

sealed class CropSpec {
    class Ready(
        val rect: Rect,
        val shape: CropShape = RectCropShape,
    ) : CropSpec()

    object Empty : CropSpec()
}

/** State for the current image being cropped */
@Stable
interface CropState {
    val src: ImageSrc
    val imgRect: Rect
    var aspectLock: Boolean
    val accepted: Boolean
    var cropSpec: CropSpec
    val viewMatrix: ViewMatrix
    fun done(accept: Boolean)
    fun reset()
}

internal fun createCropState(src: ImageSrc, onDone: () -> Unit): CropState = object : CropState {
    val defaultAspectLock: Boolean = false

    override val viewMatrix = ViewMatrixImpl()
    override var cropSpec by mutableStateOf<CropSpec>(CropSpec.Empty)

    override val src: ImageSrc get() = src
    override val imgRect: Rect = src.size.toSize().toRect()

    override var aspectLock by mutableStateOf(defaultAspectLock)

    override fun reset() {
        aspectLock = defaultAspectLock
        cropSpec = CropSpec.Empty
    }

    override var accepted: Boolean by mutableStateOf(false)

    override fun done(accept: Boolean) {
        accepted = accept
        onDone()
    }
}

internal fun CropState.shapePathOrError(rect: Rect? = null): Path {
    return when (val spec = cropSpec) {
        is CropSpec.Ready -> spec.shape.asPath(rect ?: spec.rect)
        else -> error("CropSpec is not available. $this")
    }
}

internal fun CropState.initialize(
    outer: Rect,
    aspectRatio: AspectRatio,
) {
    val region = imgRect.setAspect(aspectRatio)

    viewMatrix.setInitialMatrix(imgRect)
    viewMatrix.fit(viewMatrix.matrix.map(region), outer)

    val cropRect = viewMatrix.matrix.map(region)
    cropSpec = CropSpec.Ready(rect = cropRect)
}
