package com.esafirm.androidplayground.others

import android.os.Bundle
import android.os.SystemClock
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LeakCoroutineActivity : BaseAct(), CoroutineScope {

    private var counter = 0

    // Must use [SupervisorJob] to actually free from the leak
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            row {
                logger()
                button(
                    text = "Click Me to Leak",
                    onClick = {
                        runCoroutine()
                    }
                )
            }
        )
    }

    private fun runCoroutine() {
        Logger.log("Clicked!")
        launch {
            counter++
            Logger.log("Counter active!")
            SystemClock.sleep(10_000)
            Logger.log("Sleep done!")
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineContext.cancelChildren()
    }
}