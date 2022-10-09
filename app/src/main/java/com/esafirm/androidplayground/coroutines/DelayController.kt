package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class DelayController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return Logger.getLogView(container.context)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        repeat(10) {
            DelayTracer(Dispatchers.Default).trace((1000..3000).random().toLong())
        }

        repeat(10) {
            SleepTracer(Dispatchers.Default).trace((1000..3000).random().toLong())
        }

        val singleDispatcher = newSingleThreadContext("Single")
        repeat(10) {
            DelayTracer(singleDispatcher).trace((1000..3000).random().toLong())
        }
    }
}

class SleepTracer(override val coroutineContext: CoroutineContext) : CoroutineScope {
    fun trace(milis: Long) = launch {
        val actual = measureTimeMillis {
            Thread.sleep(milis)
        }

        Logger.log("[Sleep] Expected $milis")
        Logger.log("[Sleep] Actual $actual")
        Logger.divider()
    }
}

class DelayTracer(private val coContext: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = coContext

    fun trace(milis: Long) = launch {
        val actual = measureTimeMillis {
            delay(milis)
        }

        Logger.log("Thread: ${Thread.currentThread().name}")
        Logger.log("[Delay] Actual : $actual")
        Logger.log("[Delay] Expected : $milis")
        Logger.divider()
    }
}
