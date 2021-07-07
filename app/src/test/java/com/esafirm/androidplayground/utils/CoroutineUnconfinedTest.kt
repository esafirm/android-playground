package com.esafirm.androidplayground.utils

import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class CoroutineUnconfinedTest : StringSpec({
    val testDispatcher = TestCoroutineDispatcher()

    "Suspend function will resume on default dispatcher" {

        runBlockingTest {
            launch {
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
    }

    "Unconfined on multiple call in one launc" {
        launch(Dispatchers.Unconfined) {
            printCurrentThread("#AA - Unconfined")
            delay(100)
            printCurrentThread("#AB - Unconfined")
        }
    }

    "TestDispatcher on multiple call in one launch" {
        launch(testDispatcher) {
            printCurrentThread("#AA - Test")
            delay(100)
            printCurrentThread("#AB - Test")
            simulateSuspendFunction(100)
            printCurrentThread("#AC - Test")
        }
        testDispatcher.advanceUntilIdle()
    }

    "Test dispatcher on nested context" {
        launch(testDispatcher) {
            withContext(testDispatcher) {
                printCurrentThread("#AA - Nested")
                delay(100)
                printCurrentThread("#AB - Nested")
                simulateSuspendFunction(100)
                printCurrentThread("#AC - Nested")
            }
        }
        testDispatcher.advanceUntilIdle()
    }
})

@Suppress("BlockingMethodInNonBlockingContext")
private suspend fun simulateSuspendFunction(time: Long) {
    Thread.sleep(time)
}

@Suppress("NOTHING_TO_INLINE")
private inline fun printCurrentThread(callerId: String) {
    println("$callerId Current thread: ${Thread.currentThread().name}")
}
