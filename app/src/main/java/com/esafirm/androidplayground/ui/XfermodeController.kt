package com.esafirm.androidplayground.ui

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewTreeObserver
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.Logger
import extra.conductor.esafirm.com.conductorextra.components.AbsController

class XfermodeController : AbsController() {

    init {
        Logger.clear()
    }

    override fun getLayoutResId(): Int = R.layout.controller_xfermode

    override fun onViewBound(view: View) {

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)

                val bg = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val bgImage = BitmapFactory.decodeResource(applicationContext?.resources, R.drawable.bg_laptop)
                val icon = BitmapFactory.decodeResource(applicationContext?.resources, R.drawable.ic_copyright)

                val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.BLACK
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                }

                Canvas(bg).apply {
                    drawBitmap(bgImage, 0f, 0f, null)
                    drawBitmap(icon, 0f, 0f, iconPaint)
                }

                with(view.findViewById(R.id.imageview)) {
                    background = BitmapDrawable(bg)
                }

                return false
            }
        })


    }
}
