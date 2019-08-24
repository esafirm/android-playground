package com.esafirm.androidplayground.utils

import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin

object CrashHandler {
    fun logCrash(e: Exception) {
        CrashReporterPlugin.getInstance().sendExceptionMessage(Thread.currentThread(), e)
    }
}