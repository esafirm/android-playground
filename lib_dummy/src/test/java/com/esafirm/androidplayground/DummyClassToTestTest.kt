package com.esafirm.androidplayground

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class DummyClassToTestTest : StringSpec({

    val tested = DummyClassToTest()

    "It should generate string" {
        tested.generateString() shouldBe "String"
    }
})