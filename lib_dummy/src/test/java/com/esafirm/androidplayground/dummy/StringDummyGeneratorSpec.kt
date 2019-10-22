package com.esafirm.androidplayground.dummy

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.random.Random

class StringDummyGeneratorSpec : StringSpec({
    val mockRandom = mockk<Random>(relaxed = true)
    val tested = StringDummyGenerator(mockRandom)

    "It should trigger random" {
        every { mockRandom.nextInt() } returns 1

        tested.generate() shouldBe "1"

        verify {
            mockRandom.nextInt()
        }
    }
})