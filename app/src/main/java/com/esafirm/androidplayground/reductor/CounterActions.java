package com.esafirm.androidplayground.reductor;

import com.yheriatovych.reductor.Action;
import com.yheriatovych.reductor.annotations.ActionCreator;

@ActionCreator
public interface CounterActions {
    String INCREMENT = "INCREMENT";
    String ADD = "ADD";

    @ActionCreator.Action(INCREMENT)
    Action increment();

    @ActionCreator.Action(ADD)
    Action add(int value);
}
