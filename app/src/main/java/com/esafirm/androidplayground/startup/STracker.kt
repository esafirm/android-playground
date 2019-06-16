package com.esafirm.androidplayground.startup

import android.util.Log

object STracker {
    private var startTime: Long? = null
    private var endTime: Long? = null
    private var appOnCreate: Long? = null

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun appOnCreate() {
        appOnCreate = System.currentTimeMillis()
    }

    fun end() {
        endTime = System.currentTimeMillis()

        Log.d("Startup", """
            Start: $startTime
            OnCreate: $appOnCreate
            End: $endTime
            StartupTime: ${endTime!! - startTime!!}
            Start to onCreate: ${appOnCreate!! - startTime!!}
            onCreate to End: ${endTime!! - appOnCreate!!}
        """.trimIndent())
    }
}