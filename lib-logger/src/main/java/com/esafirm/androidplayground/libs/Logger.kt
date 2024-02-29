package com.esafirm.androidplayground.libs

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor

object Logger {

    private val subject = PublishProcessor.create<String>()
    private val loggerBuilder = StringBuilder()

    private val logText: String
        get() = loggerBuilder.toString()

    private val obs: Flowable<String>
        get() = subject.startWith(logText).onBackpressureLatest()

    fun clear() {
        loggerBuilder.setLength(0)
    }

    fun divider() {
        log("-----------")
    }

    fun log(o: Any?) {
        val text = o?.toString() ?: "Object null"
        println(text)
        loggerBuilder.append(text).append("\n")
        subject.onNext(loggerBuilder.toString())
    }

    fun getLogView(context: Context): ViewGroup {
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics
        ).toInt()

        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setTextColor(Color.BLACK)
        textView.setTextIsSelectable(true)
        textView.setPadding(padding, padding / 2, padding, padding / 2)

        var disposable: Disposable? = null

        textView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(view: View) {
                disposable?.dispose()
                disposable = obs.observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        textView.text = null
                        textView.text = it
                    }

            }

            override fun onViewDetachedFromWindow(view: View) {
                disposable?.dispose()
            }
        })

        val scrollView = ScrollView(context)
        scrollView.addView(textView)
        return scrollView
    }
}
