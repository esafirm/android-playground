package com.esafirm.androidplayground.conductor.sharedtransition

import android.os.Bundle
import androidx.core.view.ViewCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.esafirm.androidplayground.R
import com.esafirm.conductorextra.butterknife.BinderController
import com.esafirm.conductorextra.getProps
import com.esafirm.conductorextra.toPropsBundle

class SharedTransitionDetailController : BinderController {

    @BindView(R.id.img) lateinit var imageView: ImageView
    @BindView(R.id.txt) lateinit var textView: TextView

    constructor(bundle: Bundle) : super(bundle)
    constructor(sharedItem: SharedItem) : this(sharedItem.toPropsBundle())

    override fun getLayoutResId(): Int = R.layout.controller_share_transition_detail

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        val item = getProps<SharedItem>()

        textView.text = item.text
        imageView.setImageResource(item.imageRes)

        ViewCompat.setTransitionName(imageView, "IMAGE${item.imageRes}")
        ViewCompat.setTransitionName(textView, "TEXT${item.imageRes}")
    }
}
