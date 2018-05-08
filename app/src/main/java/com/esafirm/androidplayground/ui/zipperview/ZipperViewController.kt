package com.esafirm.androidplayground.ui.zipperview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseController

class ZipperViewController : BaseController() {

    private val matchParent = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return FrameLayout(container.context).apply {
            addView(ImageView(context).apply {
                layoutParams = matchParent
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageResource(R.drawable.bg_laptop)
            })
            addView(ZipperView(context).apply {
                layoutParams = matchParent
            })
        }
    }
}
