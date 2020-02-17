package com.esafirm.androidplayground.utils

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.*

class CoroutineComputationCounterSpec : StringSpec({
    "It should cancelled right away" {
        val counter = CoroutineComputationCounter()
        counter.count() shouldBe 3
    }
})

class CoroutineComputationCounter {

    private var counter: Int = 0

    /**
     * Count to 5 and return the value
     */
    fun count(): Int = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                if (!isActive) {
                    break
                }
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    counter++
                    nextPrintTime += 500L
                }
            }
        }

        delay(1300L)
        println("main: I'm tired of waiting!")

        job.cancelAndJoin()
        println("main: Now I can quit.")
        counter
    }
}