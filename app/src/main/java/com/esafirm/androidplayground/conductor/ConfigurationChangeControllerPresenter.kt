package com.esafirm.androidplayground.conductor

import com.esafirm.androidplayground.utils.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ConfigurationChangeControllerPresenter {

    var view: ConfigurationChangeControllerView? = null

    fun fetch() {
        Logger.log("Fetching ~")

        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.showResult()
                }
    }
}
