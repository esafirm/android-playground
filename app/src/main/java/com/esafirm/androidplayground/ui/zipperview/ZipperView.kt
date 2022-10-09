package com.esafirm.androidplayground.ui.zipperview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.libs.Logger

class ZipperView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private fun <T> fastLazy(init: () -> T) = lazy(LazyThreadSafetyMode.NONE, init)

    private val START_OFFSET = -750

    private var xProgress = START_OFFSET
    private val progressUpdater = Runnable {
        xProgress += 2
    }

    private val zipper by fastLazy {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.zipper_the_zipper)
        Bitmap.createScaledBitmap(bitmap, (measuredWidth + Math.abs(START_OFFSET)), measuredHeight, true)
    }
    private val mask by fastLazy {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.zipper_mask, BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }).also { it.setHasAlpha(true) }
        Bitmap.createScaledBitmap(bitmap, (measuredWidth + Math.abs(START_OFFSET)), measuredHeight, true)
    }
    private val bg by fastLazy {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.zipper_bg, BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }).also { it.setHasAlpha(true) }
        Bitmap.createScaledBitmap(bitmap, measuredWidth, measuredHeight, true)
    }
    private val pendant by fastLazy {
        BitmapFactory.decodeResource(resources, R.drawable.zipper_pendant)
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        isDither = true
    }

    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        setBackgroundColor(Color.TRANSPARENT)
        start()
    }

    private var task: Runnable? = null
    private fun start() {
        task = Runnable {
            Logger.log("Task updated : ${xProgress}")
            progressUpdater.run()
            task?.let { removeCallbacks(task) }
            postInvalidate()
            start()
        }
        postDelayed(task, 1)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Logger.log("onDraw")

        val x = xProgress.toFloat()
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        canvas.drawBitmap(mask, x, 0F, null)
        canvas.drawBitmap(bg, 0F, 0F, paint)
        canvas.drawBitmap(zipper, x, 0F, null)

    }
}
