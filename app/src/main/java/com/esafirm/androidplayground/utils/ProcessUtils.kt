package com.esafirm.androidplayground.utils

import android.app.ActivityManager
import android.content.Context

object ProcessUtils {

    private fun getCurrentProcessName(
        pid: Int,
        manager: ActivityManager
    ): String? {
        var processName = ""
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                processName = processInfo.processName
                break
            }
        }
        return processName
    }

    fun isMainProcess(
        ctx: Context,
        pid: Int = android.os.Process.myPid(),
        manager: ActivityManager? = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    ): Boolean {
        if (manager?.runningAppProcesses == null) return true

        val context = ctx.applicationContext
        val packageName = context.packageName
        val currentProcessName = getCurrentProcessName(pid, manager)
        return !packageName.isNullOrEmpty() && packageName == currentProcessName
    }
}