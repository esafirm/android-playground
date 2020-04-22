package com.esafirm.androidplayground.dummy

import io.kotlintest.specs.StringSpec
import io.mockk.mockk

class EventTracker {

    fun trackNotExtension() {
        println("Not extension $this")
    }

    companion object {
        val instance by lazy {
            EventTracker()
        }
    }
}

fun EventTracker.trackExtension() {
    println("Extension $this")
}

class ExtensionFunctionSpec : StringSpec({

    val tracker = mockk<EventTracker>(relaxed = true)

    "It should trigger its extension function" {
        tracker.trackExtension()
    }

    "It should not trigger its function" {
        tracker.trackNotExtension()
    }
})