package com.esafirm.androidplayground.utils

import android.app.ActivityManager
import android.content.Context

object ProcessUtils {

    fun getCurrentProcessName(context: Context): String {
        var processName = ""
        val pid = android.os.Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                processName = processInfo.processName
                break
            }
        }
        return processName
    }

    fun isMainProcess(ctx: Context): Boolean {
        val context = ctx.applicationContext
        val packageName = context.packageName
        val currentProcessName = getCurrentProcessName(context)
        return !packageName.isNullOrEmpty() && packageName == currentProcessName
    }
}