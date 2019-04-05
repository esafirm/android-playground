package com.esafirm.androidplayground.coroutines

import kotlinx.coroutines.*

typealias Consumer<T> = (T) -> Unit

class BehaviorSubjectCoroutine<T> {

    private val consumers = mutableListOf<Pair<Long, Consumer<T>>>()
    private var currentJob: Job? = null

    var value: T? = null

    fun subscribe(throttle: Long = 0, consumer: Consumer<T>) {
        val isFirstConsumer = consumers.isEmpty()
        if (isFirstConsumer) {
            value?.let { post(it) }
        }
        consumers.add(throttle to consumer)
    }

    fun post(value: T) {
        if (consumers.isEmpty()) {
            this.value = value
            return
        }

        currentJob?.cancel()
        currentJob = GlobalScope.launch {
            consumers.forEach { (time, consumer) ->
                if (time > 0) {
                    delay(time)
                }
                consumer.invoke(value)
                this@BehaviorSubjectCoroutine.value = value
            }
        }
    }
}