package com.esafirm.androidplayground.rxjava2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.Logger

class RxJava2NullItemAct : BaseAct() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        button.text = "Start Null Observable"
        button.setOnClickListener { doStartObservable() }

        val linearLayout = LinearLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            addView(button)
            addView(Logger.getLogView(this@RxJava2NullItemAct))
        }

        setContentView(linearLayout)
    }

    @SuppressLint("CheckResult")
    private fun doStartObservable() {
        io.reactivex.Observable.just<Any>(null)
            .subscribe(
                { Logger.log("onNext on Observable.just(null)") },
                { throwable -> Logger.log("onError on Observable.just(null)$throwable") }
            )
    }
}
