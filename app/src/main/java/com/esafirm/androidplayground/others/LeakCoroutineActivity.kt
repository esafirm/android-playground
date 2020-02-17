package com.esafirm.androidplayground.others

import android.os.Bundle
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class LeakCoroutineActivity : BaseAct(), CoroutineScope {

    private var counter = 0

    val job = SupervisorJob()

    private val executors = Executors.newSingleThreadExecutor()

    // Must use [SupervisorJob] to actually free from the leak
    override val coroutineContext: CoroutineContext = Dispatchers.Default + job

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

                button(
                    text = "Click Me to Thread",
                    onClick = {
                        runJavaThread()
                    }
                )
            }
        )
    }

    private fun runJavaThread() {
        executors.submit {
            longRunningWorks()
        }
    }

    private fun runCoroutine() {
        Logger.log("Clicked!")
        launch {
            runTask()
        }.invokeOnCompletion {
            Logger.log("Coroutine on completion: $it")
        }
    }

    private suspend fun runTask() {
        counter++
        Logger.log("Counter active!")
        runTaskSuspending()
        Logger.log("Coroutine isActive: $isActive")
        Logger.log("Sleep done! Counter: $counter")
    }

    private suspend fun runTaskSuspending() = withContext(Dispatchers.Default) {
        longRunningWorks()
    }

    private fun longRunningWorks() {
        try {
            var timer = 0
            // Is active must be used to enable cancel on computation
            // Otherwise the `while` will be still running
            while (timer < 10 && isActive) {
                timer += 1
                Thread.sleep(1_000)
                Logger.log("Count: $timer")
            }
        } catch (e: Exception) {
            Logger.log("Sleep exception : $e")
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineContext.cancel()
        executors.shutdownNow()
    }
}