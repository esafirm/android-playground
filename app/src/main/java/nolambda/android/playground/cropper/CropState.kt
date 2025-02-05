package nolambda.android.playground.cropper

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import nolambda.android.playground.cropper.images.ImageSrc
import nolambda.android.playground.cropper.utils.ViewMatrix
import nolambda.android.playground.cropper.utils.ViewMatrixImpl
import nolambda.android.playground.cropper.utils.constrainOffset
import nolambda.android.playground.cropper.utils.constrainResize
import nolambda.android.playground.cropper.utils.eq
import nolambda.android.playground.cropper.utils.keepAspect
import nolambda.android.playground.cropper.utils.next90
import nolambda.android.playground.cropper.utils.prev90
import nolambda.android.playground.cropper.utils.scaleToFit
import nolambda.android.playground.cropper.utils.setAspect
import nolambda.android.playground.cropper.utils.setSize
import nolambda.android.playground.cropper.utils.toRect

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
    var transform: ImgTransform
    val imgRect: Rect
    var region: Rect
    var aspectLock: Boolean
    val accepted: Boolean
    var cropSpec: CropSpec
    val viewMatrix: ViewMatrix
    fun done(accept: Boolean)
    fun reset()
}

internal fun createCropState(src: ImageSrc, onDone: () -> Unit): CropState = object : CropState {
    val defaultTransform: ImgTransform = ImgTransform.Identity
    val defaultAspectLock: Boolean = false

    override val viewMatrix = ViewMatrixImpl()
    override var cropSpec by mutableStateOf<CropSpec>(CropSpec.Empty)

    override val src: ImageSrc get() = src
    private var _transform: ImgTransform by mutableStateOf(defaultTransform)
    override var transform: ImgTransform
        get() = _transform
        set(value) {
            onTransformUpdated(transform, value)
            _transform = value
        }

    private val defaultRegion = src.size.toSize().toRect()

    private var _region by mutableStateOf(defaultRegion)
    override var region
        get() = _region
        set(value) {
            _region = updateRegion(
                old = _region,
                new = value,
                bounds = imgRect,
                aspectLock = aspectLock
            )
        }

    override val imgRect by derivedStateOf { getTransformedImageRect(transform, src.size) }

    override var aspectLock by mutableStateOf(defaultAspectLock)

    private fun onTransformUpdated(old: ImgTransform, new: ImgTransform) {
        val unTransform = old.asMatrix(src.size).apply { invert() }
        _region = new.asMatrix(src.size).map(unTransform.map(region))
    }

    override fun reset() {
        transform = defaultTransform
        _region = defaultRegion
        aspectLock = defaultAspectLock
        cropSpec = CropSpec.Empty
    }

    override var accepted: Boolean by mutableStateOf(false)

    override fun done(accept: Boolean) {
        accepted = accept
        onDone()
    }
}

internal fun getTransformedImageRect(transform: ImgTransform, size: IntSize): Rect {
    val dstMat = transform.asMatrix(size)
    return dstMat.map(size.toIntRect().toRect())
}

internal fun CropState.rotLeft() {
    transform = transform.copy(angleDeg = transform.angleDeg.prev90())
}

internal fun CropState.rotRight() {
    transform = transform.copy(angleDeg = transform.angleDeg.next90())
}

internal fun CropState.flipHorizontal() {
    if ((transform.angleDeg / 90) % 2 == 0) flipX() else flipY()
}

internal fun CropState.flipVertical() {
    if ((transform.angleDeg / 90) % 2 == 0) flipY() else flipX()
}

internal fun CropState.flipX() {
    transform = transform.copy(scale = transform.scale.copy(x = -1 * transform.scale.x))
}

internal fun CropState.flipY() {
    transform = transform.copy(scale = transform.scale.copy(y = -1 * transform.scale.y))
}

internal fun updateRegion(
    old: Rect,
    new: Rect,
    bounds: Rect,
    aspectLock: Boolean
): Rect {
    val offsetOnly = old.width.eq(new.width) && old.height.eq(new.height)
    return if (offsetOnly) new.constrainOffset(bounds)
    else {
        val result = when {
            aspectLock -> new.keepAspect(old).scaleToFit(bounds, old)
            else -> new.constrainResize(bounds)
        }
        return when {
            result.isEmpty -> result.setSize(old, Size(1f, 1f)).constrainOffset(bounds)
            else -> result
        }
    }
}

internal fun CropState.generateCropRegion(): Rect {
    val scale = viewMatrix.scale
    val spec = cropSpec
    if (spec is CropSpec.Ready) {
        val bounds = viewMatrix.matrix.map(imgRect)
        val cropRect = spec.rect

        val actualX = cropRect.left - bounds.left
        val actualY = cropRect.top - bounds.top

        val left = actualX.coerceAtLeast(0f) / scale + imgRect.left
        val top = actualY.coerceAtLeast(0f) / scale + imgRect.top

        val actualWidth = cropRect.width / scale
        val actualHeight = cropRect.height / scale

        return Rect(
            left = left,
            top = top,
            right = (left + actualWidth).coerceAtMost(imgRect.right),
            bottom = (top + actualHeight).coerceAtMost(imgRect.bottom),
        )
    }
    return Rect.Zero
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
    val image = src.size.toSize().toRect()
    val region = image.setAspect(aspectRatio)

    viewMatrix.setInitialMatrix(image)
    viewMatrix.fit(viewMatrix.matrix.map(region), outer)

    val cropRect = viewMatrix.matrix.map(region)
    cropSpec = CropSpec.Ready(rect = cropRect)
}
