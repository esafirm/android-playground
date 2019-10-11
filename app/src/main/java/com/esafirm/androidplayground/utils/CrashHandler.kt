package com.esafirm.androidplayground.utils

import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin

open class CrashHandlerImpl(private vararg val handlers: (Exception) -> Unit) {
    fun logCrash(e: Exception) {
        handlers.forEach {
            it.invoke(e)
        }
    }
}

object CrashHandler : CrashHandlerImpl(
    { CrashReporterPlugin.getInstance().sendExceptionMessage(Thread.currentThread(), it) }
)