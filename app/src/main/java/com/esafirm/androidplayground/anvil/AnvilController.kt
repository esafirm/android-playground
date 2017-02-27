package com.esafirm.androidplayground.anvil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.esafirm.androidplayground.common.BaseController
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import java.util.concurrent.TimeUnit

class AnvilController : BaseController() {

    var text: String = "tesu"
    var textOutside: String = "outside"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            object : RenderableView(container.context) {
                override fun view() {
                    linearLayout {
                        orientation(LinearLayout.VERTICAL)
                        textView {
                            text(text)
                        }
                        textView {
                            text(textOutside)
                        }
                        button {
                            margin(12)
                            text("Click!")
                            onClick {
                                startTimer()
                                text = System.currentTimeMillis().toString()
                            }
                        }
                    }
                }
            }

    fun startTimer() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .forEach {
                    textOutside = "$it ~"
                    Anvil.render()
                }
    }
}
