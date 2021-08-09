package com.esafirm.androidplayground.ui.glide

import android.util.Log

object GlideRequestTrackerStore {
    private val tracked = mutableListOf<Pair<EventType, Long>>()

    fun clear() {
        tracked.clear()
    }

    fun add(type: EventType, time: Long) {
        tracked.add(type to time)
    }

    fun printResult() {
        val request = tracked.filter { it.first == EventType.REQUEST }
        val whole = tracked.filter { it.first == EventType.WHOLE }
        val preDraw = tracked.filter { it.first == EventType.PREDRAW }

        val requestAverage = request.average()
        val wholeAverage = whole.average()

        Log.d("GlideMeasure", "Requests avg: $requestAverage ms")
        Log.d("GlideMeasure", "Whole avg: $wholeAverage ms")
        Log.d("GlideMeasure", "PreDraw avg: ${preDraw.average()} ms")
        Log.d("GlideMeasure", "---------------")
        Log.d("GlideMeasure", "Requests median: ${request.median()} ms")
        Log.d("GlideMeasure", "Whole median: ${whole.median()} ms")
        Log.d("GlideMeasure", "PreDraw median: ${preDraw.median()} ms")
    }

    private fun List<Pair<EventType, Long>>.average(): Long {
        return if (isEmpty()) 0 else map { it.second }.reduce { acc, l -> acc + l } / size
    }

    private fun List<Pair<EventType, Long>>.median(): Long {
        return if (isEmpty()) 0 else map { it.second }[size / 2]
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