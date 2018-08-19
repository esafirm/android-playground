package com.esafirm.androidplayground.ui.vectortest

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger

/** This controller test how we can achieve using VectorDrawable in many OS level **/
/**
 * Few ways to sue [VectorDrawable] in your android application
 * 1. Don't use `vectorDrawables.useSupportLibrary true`, i think with this flag set to false, our build tools with generate PNGs for our vectors
 * 2. Set all the things in Java/Kotlin. Using `ContextCompat.getDrawable` make all this breeze
 * 3. Set `vectorDrawables.useSupportLibrary true`, wrap the vectors in Drawable container
 */
class VectorTestController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val context = container.context
        val ll = inflater.inflate(R.layout.controller_vector_test, container, false)
        return ll.apply {
            this as LinearLayout
            addView(Logger.getLogView(context))
            addView(Button(context).apply {
                text = "Add"
                setOnClickListener {
                    addView(createImageView(context))
                    addView(createText(context))
                }
            })
        }
    }

    private fun createImageView(context: Context) =
            ImageView(context).apply {
                setImageResource(R.drawable.ic_3d_rotation_black_24dp)
            }

    private fun createText(context: Context) =
            TextView(context).apply {
                text = "Hello world1"
                setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_3d_rotation_black_24dp),
                        null, null, null
                )
            }
}
