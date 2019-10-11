package com.esafirm.androidplayground.utils

import io.kotlintest.specs.StringSpec
import io.mockk.mockk
import io.mockk.verify

class CrashHandlerImplSpec : StringSpec({
    "it should dispatch to its members" {
        val mockHandler = mockk<(Exception) -> Unit>(relaxed = true)
        val tested = CrashHandlerImpl(mockHandler)

        tested.logCrash(IllegalArgumentException())

        verify { mockHandler.invoke(any()) }
    }
})