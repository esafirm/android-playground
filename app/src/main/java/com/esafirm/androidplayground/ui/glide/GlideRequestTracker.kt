package com.esafirm.androidplayground.ui.glide

import android.util.Log

object GlideRequestTrackerStore {
    private val tracked = mutableListOf<Pair<EventType, Long>>()

    @Synchronized
    fun add(type: EventType, time: Long) {
        tracked.add(type to time)
    }

    fun printResult() {
        val request = tracked.filter { it.first == EventType.REQUEST }
        val whole = tracked.filter { it.first == EventType.WHOLE }
        val preDraw = tracked.filter { it.first == EventType.PREDRAW }

        val requestAverage = request.map { it.second }.reduce { acc, l -> acc + l } / request.size
        val wholeAverage = whole.map { it.second }.reduce { acc, l -> acc + l } / request.size

        Log.d("GlideMeasure", "Requests avg: $requestAverage ms")
        Log.d("GlideMeasure", "Whole avg: $wholeAverage ms")

        if(preDraw.isEmpty().not()) {

        }
    }
}

enum class EventType {
    REQUEST,
    WHOLE,
    PREDRAW
}

class GlideRequestTracker(
    private val store: GlideRequestTrackerStore,
    private val currentStartTime: Long,
) {

    fun log(eventType: EventType) {
        val delta = System.currentTimeMillis() - currentStartTime
        Log.d("GlideMeasure", "$eventType took $delta ms")
        store.add(eventType, delta)
    }

    companion object {
        fun start(): GlideRequestTracker {
            return GlideRequestTracker(
                GlideRequestTrackerStore,
                System.currentTimeMillis()
            )
        }
    }
}