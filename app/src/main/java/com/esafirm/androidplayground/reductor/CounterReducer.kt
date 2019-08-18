package com.esafirm.androidplayground.reductor

import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer

@AutoReducer
internal abstract class CounterReducer : Reducer<Int> {

    @AutoReducer.InitialState
    fun initialState(): Int {
        return 0
    }

    @AutoReducer.Action(value = CounterActions.INCREMENT, from = CounterActions::class)
    fun increment(state: Int): Int {
        return state + 1
    }

    @AutoReducer.Action(value = CounterActions.ADD, from = CounterActions::class)
    fun add(state: Int, value: Int): Int {
        return state + value
    }

    companion object {

        fun create(): CounterReducer {
            return CounterReducerImpl() //Note: usage of generated class
        }
    }
}