package com.esafirm.androidplayground.ui.viewdraghelper

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.clamp
import com.esafirm.androidplayground.utils.dp


class DargCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val dragHelper = ViewDragHelper.create(this, 1.0f, Callback())
            .also {
                it.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM)
            }

    private val childImg by lazy { findViewById<ImageView>(R.id.img_child) }
    private val card by lazy { findViewById<View>(R.id.card) }

    private val dragRange = 92.dp

    init {
        View.inflate(getContext(), R.layout.cv_drag_card_view, this)

        childImg.run {
            scaleX = 0.0f
            scaleY = 0.0f
        }
    }

    inner class Callback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
            Logger.log("Try to capture view: $child - $pointerId")
            return child != null && child == card
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            Logger.log("clamp vertical top: $top dy: $dy")
            return clamp(0, dragRange, top).toInt()
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = child.left

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            Logger.log("view released : $releasedChild")
            val top = when (card.top <= dragRange / 2) {
                true -> 0
                false -> dragRange.toInt()
            }
            if (dragHelper.settleCapturedViewAt(card.left, top)) {
                ViewCompat.postInvalidateOnAnimation(this@DargCardView)
            }
        }


        override fun onViewPositionChanged(changedView: View?, left: Int, top: Int, dx: Int, dy: Int) {
            val scale: Float = (dragRange - card.top) / dragRange
            childImg.apply {
                scaleX = scale
                scaleY = scale
            }
        }

        override fun getViewVerticalDragRange(child: View?): Int = dragRange.toInt()

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            Logger.log("on edge drag - flag: $edgeFlags pointerId: $pointerId")
            dragHelper.captureChildView(card, pointerId)
        }
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel()
            return false
        }
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }
}

