package com.esafirm.androidplayground.dummy

import android.content.Intent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClassThatIncludeIntentTest {
    private val sut = ClassThatIncludeIntent()

    @Test
    fun `will return the right value`() {
        val result1 = sut.getIntentValueForKey(Intent().apply {
            putExtra("key", "test")
        }, "key")

        val result2 = sut.getIntentValueForKey(Intent(), "key2")

        assert(result1 == "test")
        assert(result2 == ClassThatIncludeIntent.DEFAULT_VALUE)
    }
}