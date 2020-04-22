package com.esafirm.androidplayground.dummy

import kotlin.random.Random

class StringDummyGenerator(private val random: Random = Random(1)) {
    fun generate() = random.nextInt().toString()
}