package com.esafirm.androidplayground.rxjava2

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.Logger
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class RxJava2ObservableFlowableAct : BaseAct() {

    private var disposable: Disposable? = null
    private var secondDisposable: Disposable? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.clear()
        setContentView(Logger.getLogView(this))

        disposable = Observable.interval(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .forEach { aLong -> Logger.log("Observable:$aLong") }

        secondDisposable = Flowable.interval(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureLatest() // Flowable can be backpressured
            .forEach { aLong -> Logger.log("Flowable:$aLong") }
    }

    override fun onDestroy() {
        disposable?.dispose()
        secondDisposable?.dispose()
        super.onDestroy()
    }
}
