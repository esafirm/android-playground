package com.esafirm.androidplayground.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object WorkerThreadFactory : ThreadFactory {
    private const val ANDROID_BG_PRIO = 4

    private val poolNumber = AtomicInteger(1)
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    init {
        val s = System.getSecurityManager()
        group = if (s != null)
            s.threadGroup
        else
            Thread.currentThread().threadGroup
        namePrefix = "pool-" +
            poolNumber.getAndIncrement() +
            "-thread-"
    }

    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r,
            namePrefix + threadNumber.getAndIncrement(),
            0)
        if (t.isDaemon)
            t.isDaemon = false
        if (t.priority != ANDROID_BG_PRIO)
            t.priority = ANDROID_BG_PRIO
        return t
    }
}

fun main() {
    val backgroundDispatcher = Executors
        .newSingleThreadExecutor(WorkerThreadFactory)
        .asCoroutineDispatcher()

    val normalDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    GlobalScope.launch {
        launch(backgroundDispatcher) {
            repeat(10_000){
                async {
                    println("Prio: ${Thread.currentThread().priority}")
                    println("Low - $it")
                }
            }
        }
        launch(normalDispatcher) {
            repeat(10_000){
                async {
                    println("Prio: ${Thread.currentThread().priority}")
                    println("Normal - $it")
                }
            }
        }
    }

    Thread.sleep(2_000)
}
