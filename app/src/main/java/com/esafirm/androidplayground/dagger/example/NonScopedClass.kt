package com.esafirm.androidplayground.dagger.example

import kotlin.random.Random

/**
 * This class is not scoped anywhere
 */
class NonScopedClass {

    private val id = Random.nextInt()

    override fun toString(): String = "Non scoped class -- $id"
}
