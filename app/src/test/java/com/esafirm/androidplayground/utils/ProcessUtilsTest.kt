package com.esafirm.androidplayground.utils

import android.app.ActivityManager
import android.content.Context
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk


class ProcessUtilsSpec : StringSpec({

    fun createMainProcess(): List<ActivityManager.RunningAppProcessInfo> {
        return listOf(
            ActivityManager.RunningAppProcessInfo().apply {
                pid = 100
                processName = "com.bl"
            },
            ActivityManager.RunningAppProcessInfo().apply {
                pid = 200
                processName = "com.insta"
            }
        )
    }

    fun createBackgroundProcess(): List<ActivityManager.RunningAppProcessInfo> {
        return listOf(
            ActivityManager.RunningAppProcessInfo().apply {
                pid = 100
                processName = "background"
            }
        )
    }

    val mockContext = mockk<Context>(relaxed = true)
    val mockManager = mockk<ActivityManager>(relaxed = true)

    every { mockContext.applicationContext } returns mockContext
    every { mockContext.packageName } returns "com.bl"

    "main process should be true if the process exist" {
        every { mockManager.runningAppProcesses } returns createMainProcess()

        ProcessUtils.isMainProcess(mockContext, 100, mockManager) shouldBe true
    }

    "main process should be true if the process null" {
        every { mockManager.runningAppProcesses } returns null

        ProcessUtils.isMainProcess(mockContext, 100, mockManager) shouldBe true
    }

    "main process should be false if the current doesn't exist" {
        every { mockManager.runningAppProcesses } returns createBackgroundProcess()

        ProcessUtils.isMainProcess(mockContext, 100, mockManager) shouldBe false
    }
})
