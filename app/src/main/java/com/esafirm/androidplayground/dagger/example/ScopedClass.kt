package com.esafirm.androidplayground.dagger.example

import kotlin.random.Random

/**
 * This class is scoped in [com.esafirm.androidplayground.dagger.modules.AppModule]
 */
class ScopedClass {

    private val id = Random.nextInt()

    override fun toString(): String = "Scoped Class -- $id"
}
