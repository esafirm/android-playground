package com.esafirm.androidplayground.rxjava2

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.libs.Logger
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class RxJava2RetryWhen : BaseAct() {

    private var constaraint = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.clear()

        val button = Button(this)
        button.text = "Retry"
        button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val linearLayout = LinearLayout(this)
        linearLayout.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(button)
        linearLayout.addView(Logger.getLogView(this))

        setContentView(linearLayout)

        io.reactivex.Observable.just(1, 2, 3)
                .flatMap {
                    if (it > constaraint) {
                        Observable.error<Any>(IllegalStateException())
                    } else {
                        Observable.just(it)
                    }
                }
                .retryWhen {
                    Logger.log("retryWhen")
                    it.flatMap {
                        onClick(button).doOnNext {
                            Logger.log("onClickNext")
                            constaraint += 1
                        }
                    }
                }
                .subscribe(
                        { o -> Logger.log(o) },
                        { throwable -> Logger.log(throwable) }
                )
    }

    private fun onClick(button: Button): Observable<ClickEvent> {
        return Observable.create { e ->
            button.setOnClickListener {
                if (!e.isDisposed) {
                    e.onNext(ClickEvent())
                }
            }

            e.setDisposable(object : Disposable {
                override fun dispose() {
                    button.setOnClickListener(null)
                }

                override fun isDisposed(): Boolean {
                    return false
                }
            })
        }
    }

    internal class ClickEvent
}
