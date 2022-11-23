package com.esafirm.androidplayground.dagger.example


import com.esafirm.androidplayground.dagger.ControllerScope

import javax.inject.Inject
import kotlin.random.Random

/**
 * A sample of class that is scoped in class declaration
 */
@ControllerScope
class ScopedInClass @Inject constructor() {

    private val id = Random.nextInt()

    override fun toString(): String = "Scoped in class -- $id"
}
