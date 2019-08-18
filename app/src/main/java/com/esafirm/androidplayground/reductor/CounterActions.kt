package com.esafirm.androidplayground.reductor

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator

@ActionCreator
interface CounterActions {

    @ActionCreator.Action(INCREMENT)
    fun increment(): Action

    @ActionCreator.Action(ADD)
    fun add(value: Int): Action

    companion object {
        const val INCREMENT = "INCREMENT"
        const val ADD = "ADD"
    }
}
