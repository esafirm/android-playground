package com.esafirm.androidplayground.ui.glide

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.dp
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.utils.switch

class GlideController : BaseController() {

    private var imageView: ImageView? = null
    private var useCache: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            setPadding(16.dp.toInt())
            switch("Use cache") { isChecked ->
                useCache = isChecked
            }
            button("Request") {
                imageView?.requestImage()
            }
            button("Request with Measure") {
                imageView?.requestImageWithMeasure()
            }
            imageView = addImageView()
        }
    }

    private fun ViewGroup.addImageView(): ImageView {
        val imageView = ImageView(context).apply {
            matchParent(vertical = false)
        }
        addView(imageView)
        return imageView
    }
}