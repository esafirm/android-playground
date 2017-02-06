package com.esafirm.androidplayground.reductor;

import com.yheriatovych.reductor.Reducer;
import com.yheriatovych.reductor.annotations.AutoReducer;

@AutoReducer
abstract class CounterReducer implements Reducer<Integer> {

    @AutoReducer.InitialState
    int initialState() {
        return 0;
    }

    @AutoReducer.Action(
            value = CounterActions.INCREMENT,
            from = CounterActions.class
    )
    int increment(int state) {
        return state + 1;
    }

    @AutoReducer.Action(
            value = CounterActions.ADD,
            from = CounterActions.class
    )
    int add(int state, int value) {
        return state + value;
    }

    public static CounterReducer create() {
        return new CounterReducerImpl(); //Note: usage of generated class
    }
}