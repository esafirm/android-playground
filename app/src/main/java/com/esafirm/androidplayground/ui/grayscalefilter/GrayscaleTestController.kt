package com.esafirm.androidplayground.ui.grayscalefilter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController

class GrayscaleTestController : BinderController() {

    override fun getLayoutResId(): Int = R.layout.controller_grayscale_test

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        view?.setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setSaturation(0F)
            })
        })
    }
}
