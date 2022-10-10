package com.esafirm.androidplayground.rxjava2

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.libs.Logger
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.disposables.CompositeDisposable
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

class RxJava2InteropAct : BaseAct() {

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.clear()
        setContentView(Logger.getLogView(this))

        // RxJava 1 Observable
        val observable = Observable.from(listOf(1, 2, 3, 4, 5))
            .subscribeOn(Schedulers.computation())

        // to RxJava 2 Observable
        val observable2 = RxJavaInterop.toV2Observable(observable)

        // to RxJava 2 Flowable
        val flowable = RxJavaInterop.toV2Flowable(observable)

        // Subscription now disposable
        // CompositeSubscription now CompositeDisposable
        compositeDisposable = CompositeDisposable().apply {

            // forEach now return Disposable
            add(
                observable2.forEach { integer -> Logger.log("ObservableV2:" + integer!!) }
            )

            add(
                flowable.forEach { integer -> Logger.log("FlowableV2:" + integer!!) }
            )
        }
    }

    override fun onDestroy() {
        compositeDisposable?.clear()
        super.onDestroy()
    }
}
