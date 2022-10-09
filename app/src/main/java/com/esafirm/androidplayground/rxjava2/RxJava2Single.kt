package com.esafirm.androidplayground.rxjava2

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.libs.Logger
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class RxJava2Single : BaseAct() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.clear()
        setContentView(Logger.getLogView(this))

        compositeDisposable add
            Flowable.just(1, 2, 3).reduce { integer, integer2 -> integer + integer2 }
                .subscribe { Logger.log(it) }

        compositeDisposable add
            Single.just(1).map(Int::toString)
                .subscribe { s, throwable ->
                    Logger.log(s)
                    Logger.log(throwable)
                }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    infix fun CompositeDisposable.add(sub: Disposable) {
        add(sub)
    }
}
