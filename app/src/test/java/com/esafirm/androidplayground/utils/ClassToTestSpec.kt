package com.esafirm.androidplayground.utils

import com.esafirm.androidplayground.test.ClassToTest
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ClassToTestSpec : StringSpec({
    "It should return combined value" {
        val result = ClassToTest("abc", "def").combine()
        result shouldBe "abcdef"
    }
})