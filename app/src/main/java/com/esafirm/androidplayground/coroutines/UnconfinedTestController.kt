package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UnconfinedTestController : BaseController() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Test runBlocking") {
                Logger.clear()
                doRunBlockingTest()
            }

            button("Test - Custom Scope") {
                Logger.clear()
                doCustomScopeTest()
            }

            button("Test - Sleep") {
                Logger.clear()
                doSleepTest()
            }
            logger()
        }
    }

    private fun doRunBlockingTest() = runBlocking {
        launch(Dispatchers.Unconfined) {
            printCurrentThread("#AA")
            delay(500)
            printCurrentThread("#AA")
        }
        launch {
            printCurrentThread("#AB")
            delay(1000)
            printCurrentThread("#AB")
        }
    }

    private fun doCustomScopeTest() {
        scope.launch {
            printCurrentThread("#BA")
            delay(500)
            printCurrentThread("#BA")
        }
        scope.launch {
            printCurrentThread("#BB")
            delay(1000)
            printCurrentThread("#BB")
        }
    }

    private fun doSleepTest() = runBlocking {
        launch(Dispatchers.Unconfined) {
            printCurrentThread("#CA")
            sleep(500)
            printCurrentThread("#CA")
        }
        launch {
            printCurrentThread("#CB")
            sleep(500)
            printCurrentThread("#CB")
        }
    }

    private suspend fun sleep(duration: Long) = Thread.sleep(duration)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun printCurrentThread(callerId: String) {
        Logger.log("$callerId Current thread: ${Thread.currentThread().name}")
    }
}
