package com.esafirm.androidplayground.ui.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition

class CallbackImageViewTarget(
    imageView: ImageView,
    private val onFinished: () -> Unit
) : DrawableImageViewTarget(imageView) {

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        onFinished()
    }
}