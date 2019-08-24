package com.esafirm.androidplayground.androidarch.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.esafirm.androidplayground.utils.Logger
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class ComputeWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return Observable.interval(1, TimeUnit.SECONDS)
            .doOnNext { Logger.log("Testing") }
            .filter { it >= 5 }
            .map { Random().nextBoolean() }
            .map { if (it) Result.SUCCESS else Result.RETRY }
            .blockingFirst()
    }
}
