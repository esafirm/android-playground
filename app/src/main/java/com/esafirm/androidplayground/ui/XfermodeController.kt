package com.esafirm.androidplayground.ui

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.conductorextra.butterknife.BinderController

class XfermodeController : BinderController() {

    init {
        Logger.clear()
    }

    override fun getLayoutResId(): Int = R.layout.controller_xfermode

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        bindingResult.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                bindingResult.viewTreeObserver.removeOnPreDrawListener(this)

                val bg = Bitmap.createBitmap(bindingResult.width, bindingResult.height, Bitmap.Config.ARGB_8888)
                val icon = BitmapFactory.decodeResource(applicationContext?.resources, R.drawable.ic_copyright).let {
                    Bitmap.createScaledBitmap(it, 400, 400, true)
                }

                val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#88000000")
                }

                val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.BLACK
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                }

                Canvas(bg).apply {
                    drawRect(0f, 0f, bindingResult.width.toFloat(), bindingResult.height.toFloat(), rectPaint)
                    drawBitmap(icon, bindingResult.width.toFloat() / 2, bindingResult.height.toFloat() / 2, iconPaint)
                }

                with(bindingResult.findViewById<ImageView>(R.id.imageview)) {
                    background = BitmapDrawable(bg)
                }

                return false
            }
        })
    }
}
