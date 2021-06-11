package com.esafirm.androidplayground.rxjava2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import rx.Observable
import rx.Single
import java.util.concurrent.TimeUnit

class RxJava2ZipController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Zip All") {
                zipAll()
            }
            logger()
        }
    }

    private fun zipAll() {
        val list = listOf(1, 2, 3, 4, 5)
        val singles = list.map { createSingles(it.toString()) }

        Single.zip(singles) {
            it.joinToString()
        }.subscribe {
            Logger.log(it)
        }
    }

    private fun createSingles(text: String): Single<String> {
        return Observable.timer(1000, TimeUnit.MILLISECONDS)
            .map { text }
            .toSingle()
    }
}